package ca.concordia.pga.main;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.dom4j.DocumentException;

import ca.concordia.pga.utils.DocumentParser;
import ca.concordia.pga.utils.IndexBuilder;
import ca.concordia.pga.models.Concept;
import ca.concordia.pga.models.ForwardSearch;
import ca.concordia.pga.models.Output;
import ca.concordia.pga.models.Param;
import ca.concordia.pga.models.PlanningGraph;
import ca.concordia.pga.models.Service;
import ca.concordia.pga.models.Thing;

public class AutomatedMain {
	
	// change the Prefix URL according your environment
	static final String PREFIX_URL = "resources\\simpledataset\\";
	static final String TAXONOMY_URL = PREFIX_URL + "Taxonomy.owl";
	static final String SERVICES_URL = PREFIX_URL + "Services.wsdl";
	static final String CHALLENGE_URL = PREFIX_URL + "Challenge.wsdl";
	
	public static void main(String[] args) {
		
		Map<String, Concept> conceptMap = new HashMap<String, Concept>();
		Map<String, Thing> thingMap = new HashMap<String, Thing>();
		Map<String, Service> serviceMap = new HashMap<String, Service>();
		Map<String, Param> paramMap = new HashMap<String, Param>();
		PlanningGraph pg = new PlanningGraph();
		
		try {
			DocumentParser.parseTaxonomyDocument(conceptMap, thingMap, TAXONOMY_URL);
			DocumentParser.parseServicesDocument(serviceMap, paramMap, conceptMap, thingMap,
					SERVICES_URL);
			} catch (DocumentException e) {
			e.printStackTrace();
		}

		IndexBuilder.buildInvertedIndex(conceptMap, serviceMap);
		
		/**
		 * begin parsing process
		 */
		try {
			DocumentParser.parseChallengeDocument(paramMap, conceptMap, thingMap, pg,
					CHALLENGE_URL);
		} catch (DocumentException e) {
			e.printStackTrace();
		}	
		
		

		ForwardSearch fs = new ForwardSearch(serviceMap, pg);
		Vector<Service> solution = fs.perform();
		
		
		Output o = new Output(pg, solution);
		o.writeResult();
		
	}
	
	

}
