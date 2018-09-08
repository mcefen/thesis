package xmltest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

		}	
	}	
}
