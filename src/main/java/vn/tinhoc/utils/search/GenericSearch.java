package vn.tinhoc.utils.search;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.WordUtils;
import vn.lanhoang.ontology.annotation.OntologyObject;
import vn.lanhoang.ontology.configuration.OntologyVariables;
import vn.tinhoc.domain.dto.BasicDTO;
import vn.tinhoc.tokenizer.Dictionary;
import vn.tinhoc.tokenizer.TokenObject;
import vn.tinhoc.utils.StringNormalizedUtils;
import vn.tinhoc.utils.search.parser.BasicParser;
import vn.tinhoc.utils.search.parser.ComplexParser;
import vn.tinhoc.utils.search.parser.Parser;
import vn.tinhoc.utils.search.parser.WithParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static vn.tinhoc.utils.StringNormalizedUtils.findWordsInMixedCase;

public class GenericSearch {

    private final String preffix;
    private final TokenObject tken;
    private final Dictionary dictionary;

    public GenericSearch(OntologyVariables vars, TokenObject tken, Dictionary dictionary) {
        this(vars.getPreffix(), tken, dictionary);
    }

    public GenericSearch(String preffix, TokenObject tken, Dictionary dictionary) {
        this.tken = tken;
        this.preffix = preffix;
        this.dictionary = dictionary;
    }

    public <T> Pair<String, List<BasicDTO>> parse(String text, Class<T> type) {
        String typeUri = type.getAnnotation(OntologyObject.class).uri();
        String lowerCaseSearch = text.toLowerCase(Locale.ROOT);
        List<BasicDTO> keyWords = new ArrayList<>();

        Set<String> orTexts = new HashSet<>(Arrays.asList(lowerCaseSearch.split("hoặc")));

        Set<String> andTexts = new HashSet<>();
        Set<String> removable = new HashSet<>();
        for (String orText: orTexts) {
            String[] strs = orText.split("và");
            if (strs.length > 1) {
                andTexts.addAll(Arrays.asList(strs));
                removable.add(orText);
            }
        }
        orTexts.removeAll(removable);

        String typeName = type.getSimpleName();
        Set<String> tokens = tken.getObjectTokens()
                .computeIfAbsent(typeName, k-> new HashSet<>());

        AtomicInteger integer = new AtomicInteger(0);

        List<String> orSparql = parse(parse(orTexts, tokens), keyWords, integer, text, !(andTexts.isEmpty() && orTexts.isEmpty()));
        String orSelect = StringUtils.join(orSparql, "\nUNION\n");

        List<String> andSparql = parse(parse(andTexts, tokens), keyWords, integer, text, !(orSparql.isEmpty()));
        String andSelect = StringUtils.join(andSparql, "\n.\n");

        return Pair.of(
        		"SELECT ?s\n" +
                "WHERE {\n\t" +
                    "{\t?s rdf:type " + preffix + typeUri + "\t} .\n\t" +
                    join(andSelect, orSelect)
                    .replace("\n", "\n\t") +
                "\n}" +
                "\nGROUP BY ?s",
                
                keyWords
            );
    }

    private String join(String a, String o) {
        if (a.isEmpty() && o.isEmpty()) {
            return "";
        }

        if (a.isEmpty()) {
            return o;
        }

        if (o.isEmpty()) {
            return a;
        }

        return StringUtils.join(Arrays.asList(a, o), "\nUNION\n");
    }

    private List<String> parse(List<Pair<String, String>> queries, List<BasicDTO> keyWords, AtomicInteger integer, String text, boolean isOr) {
        List<String> sparqls = new ArrayList<>();

        if (queries.size() == 1 && queries.get(0).getKey().contains("#")) {
            Pair<String, String> old = queries.get(0);
            queries.add(Pair.of("", text));
            queries.set(0, Pair.of(old.getKey().replace("#", ""), text));
        }

        if (queries.isEmpty() && !isOr) {
            Parser<Pair<String, String>> parser = new BasicParser(tken.getObjectTokens(), preffix, dictionary);
            sparqls.add(parser.parse(Pair.of("", text), integer));
        } else {
            for (Pair<String, String> query: queries) {
                Parser<Pair<String, String>> parser;
                if (query.getKey().contains("~")) {
                    if (query.getValue().contains("với")) {
                        parser = new WithParser(tken.getObjectTokens(), preffix, dictionary);
                    } else {
                        parser = new ComplexParser(tken.getObjectTokens(), preffix, dictionary);
                    }
                } else {
                    parser = new BasicParser(tken.getObjectTokens(), preffix, dictionary);
                }

                sparqls.add(parser.parse(Pair.of(query.getKey().replace("#", ""), query.getValue()), integer));
                keyWords.add(parser.keyWord());
            }
        }

        return sparqls;
    }

    private List<Pair<String, String>> parse(Set<String> words, Set<String> tokens) {
        List<Pair<String, String>> list = new ArrayList<>();
        for (String word: words) {
            String text = word.trim().replaceAll(" +", " ");
            text = WordUtils.capitalizeFully(text);

            for (String token: tokens) {
                Pair<String, String> p = getValueFromToken(token, text);
                if (p != null) {
                    list.add(Pair.of(p.getKey(), p.getValue()));
                } else if (token.contains("#")) {
                    String textAfter = StringNormalizedUtils.removeAccent(
                                text.trim().replaceAll(" +", " ")
                            )
                            .toLowerCase(Locale.ROOT);

                    list.add(Pair.of(token, textAfter));
                }
            }
        }

        return list;
    }

    private Pair<String, String> getValueFromToken(String token, String text) {
        text = text.trim().replaceAll(" +", " ");
        String initialValue = text;
        text = StringNormalizedUtils.removeAccent(text);
        String tokenAfter = StringUtils.split(token, "~")[0];
        tokenAfter = findWordsInMixedCase(tokenAfter);
        tokenAfter = tokenAfter
                .toLowerCase(Locale.ROOT)
                .replace("#", "");

        String textAfter = StringNormalizedUtils.removeAccent(text)
                .toLowerCase(Locale.ROOT);

        try {
            if (textAfter.contains(tokenAfter)) {
                int index = textAfter.indexOf(tokenAfter);
                String value = initialValue.substring(index + tokenAfter.length() + 1);

                return Pair.of(token, value.toLowerCase(Locale.ROOT));
            }
        } catch (StringIndexOutOfBoundsException ignored) { }

        return null;
    }
}
