package vn.tinhoc.utils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DataUtils {
	static Pattern WORD_FINDER = Pattern.compile("(([A-Z]?[a-z]+)|([A-Z]))");
	final static String[] metaCharacters = {"\\","^","$","{","}","[","]","(",")",".","*","+","?","|","<",">","-","&","%"};
	
	public static String normalized(String s) {
		return removeAccent(removeSpecialCharsWithSpace(s));
	}

    public static String removeAccent(String s) {
    	String temp = Normalizer.normalize(s, Normalizer.Form.NFD); 
    	Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+"); 
    	temp = pattern.matcher(temp).replaceAll(""); 
        return temp.replaceAll("đ", "d").replaceAll("Đ", "D");
    }
    
    public static String removeSpecialChars(String str) {
    	return str.replaceAll("[^a-zA-Z0-9]", "");
    }
    
    public static String removeSpecialCharsWithSpace(String str) {
    	return str.replaceAll("[^a-zA-Z0-9 ]", "");
    }
    
    public static String splitCamelCase(String s) {
	   return s.replaceAll(
	      String.format("%s|%s|%s",
	         "(?<=[A-Z])(?=[A-Z][a-z])",
	         "(?<=[^A-Z])(?=[A-Z])",
	         "(?<=[A-Za-z])(?=[^A-Za-z])"
	      ),
	      " "
	   );
	}
    
    public static String findWordsInMixedCase(String text) {
        Matcher matcher = WORD_FINDER.matcher(text);
        List<String> words = new ArrayList<>();
        while (matcher.find()) {
            words.add(matcher.group(0));
        }
        return String.join(" ", words);
    }

	public static int smallestMissingNumber(Integer... numbers) {
		Arrays.sort(numbers);

		if (numbers.length == 0 || numbers[0] != 1) {
			return 1;
		}

		for (int i = 0; i < numbers.length; i++) {
			int nextIndex = i + 1;
			if (nextIndex < numbers.length && numbers[i] + 1 != numbers[nextIndex]) {
				return numbers[i] + 1;
			}
		}

		return numbers[numbers.length - 1] + 1;
	}

	public static String escapeMetaCharacters(String inputString){
		for (int i = 0 ; i < metaCharacters.length ; i++){
			if(inputString.contains(metaCharacters[i])){
				inputString = inputString.replace(metaCharacters[i],"\\\\" + metaCharacters[i]);
			}
		}
		return inputString;
	}

	public static String removeSharp(String id) {
		return id.contains("#") ? id.substring(id.indexOf("#") + 1) : id;
	}
}