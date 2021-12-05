package vn.tinhoc.tokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class Dictionary {
    private final File DICTIONARY_FILE;
    private final Logger log = LoggerFactory.getLogger(Dictionary.class);

    public Dictionary(@Value("${spring.application.dictionary:./}") String dic_path) {
        DICTIONARY_FILE = Paths.get(dic_path).toFile();
    }

    public List<String> extract(String text) {
        text = " " + text.toLowerCase(Locale.ROOT) + " ";
        List<String> words = new ArrayList<>();
        try(LineIterator it = FileUtils.lineIterator(DICTIONARY_FILE, "UTF-8")) {
            while (it.hasNext()) {
                String line = it.nextLine();
                if (text.contains(" " + line + " ")) {
                    words.add(line);
                }
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        words = words.stream().distinct().collect(Collectors.toList());
        for (String word: words) {
            text = text.replace(word, "");
        }

        if (!text.isBlank()) {
            words.add(text.trim().replaceAll(" +", " "));
        }

        return words.stream().distinct().collect(Collectors.toList());
    }
}
