package vn.tinhoc.sbjsp.config;

import org.apache.jena.util.FileManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import vn.lanhoang.ontology.configuration.OntologyVariables;

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
	
	@Bean
	public OntologyVariables ontologyVariables() {
		// Important
		OntologyVariables ontologyVariables = new OntologyVariables();
		ontologyVariables.setBaseUri(baseUri.endsWith("#") ? baseUri : baseUri + "#");
		ontologyVariables.setPath(path);
		ontologyVariables.setMaxNestedCount(maxNestedCount);
		ontologyVariables.setModel(FileManager.getInternal().loadModelInternal(path));
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
}
