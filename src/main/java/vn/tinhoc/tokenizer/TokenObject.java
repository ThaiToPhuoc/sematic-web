package vn.tinhoc.tokenizer;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TokenObject {
    private Set<String> andTokens;
    private Set<String> orTokens;
    private Set<String> withTokens;
    private Map<String, Set<String>> objectTokens;

    public String objectTokens() {
        return objectTokens
                .entrySet()
                .stream()
                .map(e -> e.getKey() + ":" + StringUtils.join(e.getValue(), ","))
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String toString() {
        return "and:" + StringUtils.join(andTokens, ",") + "\n" +
                "or:" + StringUtils.join(orTokens, ",") + "\n" +
                "with:" + StringUtils.join(withTokens, ",") + "\n" +
                objectTokens();
    }

    public TokenObject() {
    }

    public TokenObject(Set<String> andTokens, Set<String> orTokens, Set<String> withTokens, Map<String, Set<String>> objectTokens) {
        this.andTokens = andTokens;
        this.orTokens = orTokens;
        this.withTokens = withTokens;
        this.objectTokens = objectTokens;
    }

    public Set<String> getAndTokens() {
        return andTokens;
    }

    public void setAndTokens(Set<String> andTokens) {
        this.andTokens = andTokens;
    }

    public Set<String> getOrTokens() {
        return orTokens;
    }

    public void setOrTokens(Set<String> orTokens) {
        this.orTokens = orTokens;
    }

    public Set<String> getWithTokens() {
        return withTokens;
    }

    public void setWithTokens(Set<String> withTokens) {
        this.withTokens = withTokens;
    }

    public Map<String, Set<String>> getObjectTokens() {
        return objectTokens;
    }

    public void setObjectTokens(Map<String, Set<String>> objectTokens) {
        this.objectTokens = objectTokens;
    }
}
