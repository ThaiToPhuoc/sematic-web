package vn.tinhoc.utils.search.parser;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import vn.tinhoc.domain.dto.BasicDTO;
import vn.tinhoc.tokenizer.Dictionary;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static vn.tinhoc.utils.StringNormalizedUtils.findWordsInMixedCase;

public class BasicParser extends Parser<Pair<String, String>> {

    public BasicParser(Map<String, Set<String>> tokens, String prefix) {
        super(tokens, prefix);
    }

    public BasicParser(Map<String, Set<String>> tokens, String prefix, Dictionary dictionary) {
        super(tokens, prefix, dictionary);
    }

    @Override
    public String parse(Pair<String, String> queries, AtomicInteger integer) {
        String key = queries.getKey().trim().replace("#", "");
        String value = queries.getValue();
        List<String> values = dictionary != null
                ? dictionary.extract(value)
                : Collections.singletonList(value);

        this.keyWord = new BasicDTO(findWordsInMixedCase(key), StringUtils.join(values, ", "));

        StringBuilder sb = new StringBuilder();
        sb.append("{\r\n\t");
        String predicate = key.equals("id") || key.isBlank()
                ? "?p" + integer.get()
                : prefix + key;

        sb.append(String.format("%s %s ?o%s .\n\t", subject, predicate, integer.get()));

        if (values.size() >= 1) {
            int valIndex = integer.getAndIncrement();
            sb.append("FILTER(").append(
                    values.stream()
                            .map(val -> String.format("regex(?o%d, \"%s\", \"i\")", valIndex, val))
                            .collect(Collectors.joining(" || "))
            ).append(")\n");
        } else {
            sb.append(String.format("FILTER(regex(?o%d, \"%s\", \"i\"))\n", integer.getAndIncrement(), value));
        }

        sb.append("}");

        return sb.toString();
    }
}
