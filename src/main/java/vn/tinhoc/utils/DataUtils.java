package vn.tinhoc.utils;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Statement;
import org.springframework.util.ReflectionUtils;
import vn.lanhoang.ontology.annotation.Name;
import vn.lanhoang.ontology.annotation.OntologyObject;
import vn.lanhoang.ontology.configuration.OntologyVariables;
import vn.tinhoc.utils.model.LContainer;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
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

	public static <T> List<LContainer> getComments(Property property, Class<T> klass, OntologyVariables ontologyVariables) {
		Model db = ontologyVariables.getModel();
		List<LContainer> container = new ArrayList<>();
		List<LContainer.LLabel> comments = new ArrayList<>();

		Property key = db.getProperty(ontologyVariables.getBaseUri() + klass.getAnnotation(OntologyObject.class).uri());
		Statement stmt = key.getProperty(property);
		container.add(new LContainer(
				klass.getSimpleName(),
				stmt != null ? stmt.getString() : klass.getSimpleName(),
				comments));

		try {
			doMap(property, klass, ontologyVariables, db, container, comments);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}

		return container;
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}

	public static <T> List<LContainer> getComments(Property property, Class<T> klass, OntologyVariables ontologyVariables, Model db, List<LContainer> container) {
		if (container.stream().anyMatch(k -> k.getType().equals(klass.getSimpleName()))) {
			return container;
		}

		List<LContainer.LLabel> comments = new ArrayList<>();
		Property key = db.getProperty(ontologyVariables.getBaseUri() + klass.getAnnotation(OntologyObject.class).uri());
		Statement stmt = key.getProperty(property);
		container.add(new LContainer(
				klass.getSimpleName(),
				stmt != null ? stmt.getString() : klass.getSimpleName(),
				comments));

		doMap(property, klass, ontologyVariables, db, container, comments);

		return container;
	}

	private static <T> void doMap(Property property, Class<T> klass, OntologyVariables ontologyVariables, Model db, List<LContainer> container, List<LContainer.LLabel> comments) {
		ReflectionUtils.doWithFields(klass, (field) -> {
			try {
				String value = field.getName();
				if (field.isAnnotationPresent(Name.class)) return;
				Property key = db.getProperty(ontologyVariables.getBaseUri() + value);
				Statement stmt = key.getProperty(property);
				String type = "plain";
				if (field.getType().isAnnotationPresent(OntologyObject.class)) {
					type = field.getType().getSimpleName();
					getComments(property, field.getType(), ontologyVariables, db, container);

				} else if (field.getType().equals(List.class)) {
					Class<T> persistentClass = (Class<T>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
					if (persistentClass.isAnnotationPresent(OntologyObject.class)) {
						type = persistentClass.getSimpleName();
						getComments(property, persistentClass, ontologyVariables, db, container);
					}
				}

				comments.add(new LContainer.LLabel(type, value, stmt != null ? stmt.getString() : value));
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		});
	}
}