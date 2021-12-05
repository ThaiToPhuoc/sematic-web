package vn.tinhoc.tokenizer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import vn.lanhoang.ontology.ModelManager;
import vn.lanhoang.ontology.annotation.Default;
import vn.lanhoang.ontology.annotation.OntologyObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Configuration
@EnableScheduling
public class TokenizerCronJob {

    Logger log = LoggerFactory.getLogger(TokenizerCronJob.class);

    @Autowired
    TokenObject tokenObject;

    @Scheduled(initialDelay = 0, fixedRate = 60*60*1000)
    void tokenize() {
        String fileName = "tokens.txt";
        File file = new File(fileName);
        try {
            if (file.createNewFile()) {
                log.info("Tokenizer file not found. Created a new file in {}!", file.getAbsolutePath());
            }

            Set<Class<?>> classes = ModelManager.instance().getExecutors().keySet();
            BufferedReader br = new BufferedReader(new FileReader(file));
            for (int i = 1; i <= 3; i++) {
                read(i, br.readLine());
            }

            br.close();

            Map<String, Set<String>> tokens = new HashMap<>();
            classes.forEach(type -> {
                for (Field field: type.getDeclaredFields()) {
                    tokens.computeIfAbsent(type.getSimpleName(), k -> new HashSet<>())
                            .add(parseTokens(field));
                }
            });

            tokenObject.setObjectTokens(tokens);
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(tokenObject.toString());

            writer.close();

        } catch (IOException ex) {
            log.error("Tokenizer error: {}", ex.getMessage());
        }
    }

    private void read(int at, String line) {
        switch (at) {
            case 1:
                tokenObject.setAndTokens(readSimpleTokens(line, "&&"));
                break;
            case 2:
                tokenObject.setOrTokens(readSimpleTokens(line, "||"));
                break;
            case 3:
                tokenObject.setWithTokens(readSimpleTokens(line, "=>"));
                break;
        }
    }

    private String parseTokens(Field[] fields) {
        Optional<Field> option = Arrays.stream(fields)
                .filter(f -> f.getType().isAnnotationPresent(Default.class))
                .findFirst();

        return option.map(Field::getName).orElse("id");
    }

    private String parseTokens(Field field) {
        String pt;
        if (field.isAnnotationPresent(Default.class)) {
            pt = "#" + field.getName();
        } else {
            pt = field.getName();
        }

        if (field.getType().isAnnotationPresent(OntologyObject.class)) {
            pt += "~" + field.getType().getSimpleName();
        } else if (field.getType().equals(List.class)) {
            Class<?> listType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];

            if (listType.isAnnotationPresent(OntologyObject.class)) {
                pt += "~" + listType.getSimpleName();
            }
        }

        return pt;
    }

    private Set<String> readSimpleTokens(String line, String ...defaultValues) {
        try {
            String vString = StringUtils.split(line, ":")[1];
            String[] values = StringUtils.split(vString, ",");

            return values.length <= 0
                    ? new HashSet<>(Arrays.asList(defaultValues))
                    : new HashSet<>(Arrays.asList(values));
        } catch (IndexOutOfBoundsException | NullPointerException ex) {
            log.error("Tokenizer error: {}", ex.getMessage());
            return new HashSet<>(Arrays.asList(defaultValues));
        }
    }
}
