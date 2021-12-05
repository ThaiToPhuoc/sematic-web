package vn.tinhoc.utils.search.parser;

import vn.tinhoc.domain.dto.BasicDTO;
import vn.tinhoc.tokenizer.Dictionary;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Parser<T> {
    protected String prefix;
    protected Map<String, Set<String>> tokens;
    protected BasicDTO keyWord;
    protected Dictionary dictionary;

    protected final String subject = "?s";

    public Parser(Map<String, Set<String>> tokens, String prefix) {
        this.tokens = tokens;
        this.prefix = prefix;
        this.keyWord = new BasicDTO("", "");
    }

    public Parser(Map<String, Set<String>> tokens, String prefix, Dictionary dictionary) {
        this(tokens, prefix);
        this.dictionary = dictionary;
    }

    public abstract String parse(T t, AtomicInteger integer);
    
    public final BasicDTO keyWord() {
    	return this.keyWord;
    }
}
