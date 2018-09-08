package common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sonar.MainPageEntry;
import sonar.SonarEntriesProcessor;
import squore.SquoreEntriesProcessor;
import squore.SquoreEntry;

public class Main {
	
	private static Map<String, List<SquoreEntry>> squoreProjectsMap = new HashMap<String, List<SquoreEntry>>();

	public static void main(String argv[]) throws Exception {

		String[] projects = {"mina", "deltaspike", "opennlp", "maven", "wss4j", "pdfbox", "fop", "cayenne", "jclouds", "openjpa"};

		CsvReader csvReader = new CsvReader();
		squoreProjectsMap = csvReader.readFromCsv(projects);		

		SonarEntriesProcessor sonarEntriesProcessor = new SonarEntriesProcessor();
		for (int i = 0; i < projects.length;i++){
			System.out.println(projects[i]);
			
			//Read from sonar and format a single entry to write in xml
			MainPageEntry mainPageEntry = sonarEntriesProcessor.getMainEntry(projects[i]);

			//Read from csv squore files
			SquoreEntriesProcessor squoreEntriesProcessor = new SquoreEntriesProcessor();
			List<SquoreEntry> squoreEntriesToWrite = squoreEntriesProcessor.compareSonarWithSquoreProjects(squoreProjectsMap.get(projects[i]), Arrays.asList(mainPageEntry.getComponents()));
			System.out.println("Squores: "+squoreEntriesToWrite.size());
			
			//Write to XML as one object
			XmlWriter xmlWriter = new XmlWriter();
			xmlWriter.writeSonarsToXML(projects[i], mainPageEntry);	
			xmlWriter.writeSquoresToXML(projects[i], squoreEntriesToWrite);	

		}	
	}	
}
