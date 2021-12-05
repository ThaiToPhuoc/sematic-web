package vn.tinhoc.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import vn.lanhoang.ontology.configuration.OntologyVariables;
import vn.lanhoang.ontology.model.query.QueryParser;
import vn.tinhoc.tokenizer.TokenObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@Component
public class MySystemConfig {

	@Value("${spring.onto.base-uri}")
	private String baseUri;
	@Value("${spring.onto.path}")
	private String path;
	@Value("${spring.onto.max-nested-count:5}")
	private Integer maxNestedCount;
	@Value("${spring.onto.prefix}")
	private String preffix;
	@Value("${spring.application.media-path}")
	String mediaPath;
	
	@Bean
	public OntologyVariables ontologyVariables() {
		// Important
		OntologyVariables ontologyVariables = new OntologyVariables();
		ontologyVariables.setBaseUri(baseUri.endsWith("#") ? baseUri : baseUri + "#");
		ontologyVariables.setPath(path);
		ontologyVariables.setMaxNestedCount(maxNestedCount);
		ontologyVariables.setPreffix(preffix + ":");
		
		ontologyVariables.getModel().setNsPrefix(preffix, ontologyVariables.getBaseUri());
		// Querying (Optional but recommended)
		ontologyVariables.setPreffixes(
			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"
			+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\r\n"
			+ "PREFIX " + preffix + ": <" + ontologyVariables.getBaseUri() + ">\r\n"
		);
		return ontologyVariables;
	}

	@Bean
	public StreamProperty streamProperty() throws IOException {
		String mediaPath = this.mediaPath;
		if (StringUtils.isBlank(mediaPath)) {
			mediaPath = "./media";
		}

		File directory = new File(mediaPath);

		if (directory.mkdirs()) {
			System.out.println("Tạo thư mục " + directory.getAbsolutePath());
		}

		return new StreamProperty(directory.getAbsolutePath());
	}

	@Bean
	public QueryParser queryParser(OntologyVariables ontologyVariables) {
		return new QueryParser(ontologyVariables);
	}

	@Bean
	TokenObject tokenObject() {
		return new TokenObject();
	}
}
