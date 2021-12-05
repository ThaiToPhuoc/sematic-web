package vn.tinhoc.utils.search.parser;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import vn.tinhoc.domain.dto.BasicDTO;
import vn.tinhoc.tokenizer.Dictionary;
import vn.tinhoc.utils.StringNormalizedUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static vn.tinhoc.utils.StringNormalizedUtils.findWordsInMixedCase;

public class WithParser extends Parser<Pair<String, String>> {
    public WithParser(Map<String, Set<String>> tokens, String prefix) {
        super(tokens, prefix);
    }

    public WithParser(Map<String, Set<String>> tokens, String prefix, Dictionary dictionary) {
        super(tokens, prefix, dictionary);
    }

    private List<String> getToken(Pair<String, String> queries) {
        List<String> unComputed = new ArrayList<>();

        return getToken(unComputed, queries);
    }

    private List<String> getToken(List<String> computedVals, Pair<String, String> queries) {
        String lowercaseValue = queries.getValue().toLowerCase(Locale.ROOT);
        String[] split = StringUtils.split(queries.getKey(), "~" );
        String key = split[0];
        String nextToken = split.length > 1 ? split[1].trim() : "";

        if (!nextToken.isEmpty()) {
            if (lowercaseValue.startsWith("với")) {
                lowercaseValue = lowercaseValue.substring(lowercaseValue.indexOf("với") + 4).trim();
            }

            Pair<String, String> tokenValue = getTokenFromValue(lowercaseValue, tokens.getOrDefault(nextToken, new HashSet<>()));
            computedVals.add(getActualKey(key));

            getToken(computedVals, tokenValue);
        } else {
            computedVals.add(getActualKey(queries.getKey()));
            computedVals.add(queries.getValue());
        }

        return computedVals;
    }

    private String getActualKey(String key) {
        return StringUtils.split(key.replace("#", ""), "~")[0];
    }

    private Pair<String, String> getTokenFromValue(String lowercaseValue, Set<String> tokens) {
        for (String token: tokens) {
            String simpleToken = StringUtils.split(token, "~")[0];
            simpleToken = findWordsInMixedCase(simpleToken).toLowerCase(Locale.ROOT);

            String simpleValue = lowercaseValue.trim().replaceAll(" +", " ");
            String noAccentValue = StringNormalizedUtils.removeAccent(simpleValue);

            if (noAccentValue.contains(simpleToken)) {
                return Pair.of(token, simpleValue.substring(
                        noAccentValue.indexOf(simpleToken) + simpleToken.length() + 1)
                );
            }
        }

        Optional<String> optional = tokens.stream().filter(s -> s.startsWith("#")).findFirst();

        return Pair.of(optional.orElse("id"), lowercaseValue);
    }

    @Override
    public String parse(Pair<String, String> queries, AtomicInteger integer) {
        List<String> computed = getToken(queries);
        int computedSize = computed.size();
        String firstKey = computed.get(0);
        String value = computed.get(computedSize - 1);

        List<String> values = dictionary != null
                ? dictionary.extract(value)
                : Collections.singletonList(value);
        int size = computed.size() <= 1 ? 0 : computed.size() - 2;

        this.keyWord = new BasicDTO(findWordsInMixedCase(firstKey), StringUtils.join(values, ", "));

        StringBuilder sb = new StringBuilder();
        sb.append("{\n\t");
        int nextIndex = integer.get();
        for (int i = 0; i < computed.size() - 1; i++) {
            String predicate = computed.get(i).equals("id")
                    ? "?p" + nextIndex
                    : prefix + computed.get(i);

            if (i == 0) {
                sb.append(String.format("%s %s ?o%d .\n\t", subject, predicate, nextIndex));
            } else {
                sb.append(String.format("?o%d %s ?o%d .\n\t", nextIndex, predicate, integer.incrementAndGet()));
            }

            nextIndex = integer.get();
        }

        int valIndex = integer.getAndIncrement();
        if (values.size() >= 1) {
            sb.append("FILTER(").append(
                    values.stream()
                            .map(val -> String.format("regex(?o%d, \"%s\", \"i\")", valIndex, val))
                            .collect(Collectors.joining(" || "))
            ).append(")\n");
        } else {
            sb.append(String.format("FILTER(regex(?o%d, \"%s\", \"i\"))\n", valIndex, value));
        }

        sb.append("}");

        return sb.toString();
    }
}
