package vn.tinhoc.utils.search.parser;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import vn.tinhoc.domain.dto.BasicDTO;
import vn.tinhoc.tokenizer.Dictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static vn.tinhoc.utils.StringNormalizedUtils.findWordsInMixedCase;


public class ComplexParser extends Parser<Pair<String, String>> {

    public ComplexParser(Map<String, Set<String>> tokens, String prefix) {
        super(tokens, prefix);
    }

    public ComplexParser(Map<String, Set<String>> tokens, String prefix, Dictionary dictionary) {
        super(tokens, prefix, dictionary);
    }

    private List<String> getToken(Pair<String, String> queries) {
        List<String> unComputed = new ArrayList<>();
        unComputed.add(queries.getValue());

        return getToken(unComputed, queries);
    }

    private List<String> getToken(List<String> computedVals, Pair<String, String> queries) {
        String reqToken = queries.getKey();
        if (reqToken.contains("~")) {
            String[] split = StringUtils.split(reqToken, "~");
            String actualToken = split[0];
            add(computedVals, actualToken);

            Optional<String> optional = tokens.getOrDefault(split[1], new HashSet<>())
                    .stream().filter(s -> s.startsWith("#"))
                    .findFirst();

            if (optional.isPresent()) {
                String refToken = optional.get().replace("#", "");

                if (refToken.contains("~")) {
                    computedVals = getToken(
                        computedVals,
                        Pair.of(refToken, queries.getValue())
                    );
                } else {
                    add(computedVals, refToken);
                }
            } else  {
                add(computedVals, "id");
            }
        }

        return computedVals;
    }

    private void add(List<String> computed, String token) {
        computed.add(computed.size() == 1 ? 0 : computed.size() - 2, token);
    }

    @Override
    public String parse(Pair<String, String> queries, AtomicInteger integer) {
        List<String> computed = getToken(queries);
        String value = computed.get(computed.size() - 1);
        List<String> values = dictionary != null
                ? dictionary.extract(value)
                : Collections.singletonList(value);
        int size = computed.size() <= 1 ? 0 : computed.size() - 2;
        String firstKey = computed.get(size);
        
        this.keyWord = new BasicDTO(findWordsInMixedCase(firstKey), StringUtils.join(values, ", "));

        StringBuilder sb = new StringBuilder();
        sb.append("{\r\n\t");
        int nextIndex = integer.get();
        for (int i = size; i >= 0; i--) {
            String predicate = computed.get(i).equals("id")
                    ? "?p" + nextIndex
                    : prefix + computed.get(i);

            if (i == size) {
                sb.append(String.format("%s %s ?o%d .\n\t", subject, predicate, nextIndex));
            } else {
                sb.append(String.format("?o%d %s ?o%d .\n\t", nextIndex, predicate, integer.incrementAndGet()));
            }

            nextIndex = integer.get();
        }

        String format = "regex(?o%d, \"%s\", \"i\")";
        int valIndex = integer.getAndIncrement();

        if (values.size() >= 1) {
            sb.append("FILTER(").append(
                values.stream()
                    .map(val -> String.format(format, valIndex, val))
                    .collect(Collectors.joining(" || "))
            ).append(")\n");
        } else {
            sb.append(String.format("FILTER(regex(?o%d, \"%s\", \"i\"))\n", valIndex, value));
        }

        sb.append("}");

        return sb.toString();
    }
}
