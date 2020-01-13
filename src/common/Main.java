package common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cast.CastEntriesProcessor;
import cast.CastEntry;
import sonar.MainPageEntry;
import sonar.MainPageWithEntries;
import sonar.SonarEntriesProcessor;
import sonar.SonarEntryWithIssues;
import squore.SquoreEntriesProcessor;
import squore.SquoreEntry;
import squore.SquoreEntryWithIssues;

public class Main {

	private static Map<String, List<SquoreEntry>> squoreProjectsMap = new HashMap<String, List<SquoreEntry>>();
	private static Map<String, List<String>> castProjectMap = new HashMap<String, List<String>>();
	private static Map<String, List<CastEntry>> castObjectsMapWithFixEffort = new HashMap<String, List<CastEntry>>();
	private static Map<String, List<CastEntry>> castObjectsMapWithTDinMinutes = new HashMap<String, List<CastEntry>>();

	public static void main(String argv[]) throws Exception {

		String[] projects = {"mina", "deltaspike", "opennlp", "maven", "wss4j", "pdfbox", "fop", "cayenne", "jclouds", "openjpa", 
				"quartz", "ksql", "zuul", "zipkin", "xxl-job", "vassonic", "tink", "testng", 
				"stetho", "spark", "sentinel", "seata", "redisson", "presto", "openrefine", 
				"neo4j", "nacos", "mockito", "metrics", "logger", "libgdx", "junit", "joda-time", 
				"jenkins", "javacv", "java-jwt", "gson", "grpc-java", "fresco", "filedownloader", 
				"exoplayer", "eureka", "easypermissions", "disruptor", "caffeine", "azkaban", 
				"arthas", "arduino", "antlr4", "elasticsearch", };

		//Read from XML of sonar and write only issues
		//XmlReader reader = new XmlReader();
		//CsvReader csvReader = new CsvReader();
		//CastEntriesProcessor castEntriesProcessor = new CastEntriesProcessor();

		// Read cast files with with technical debt in minutes
		// And create xmls
		// castEntriesProcessor.readCastsAndWriteXML(projects);

		//Just read cast files for comparison
		//castObjectsMapWithTDinMinutes = castEntriesProcessor.readCastsAndFilterWithSonar(projects);

		//castObjectsMapWithTDinMinutes = castEntriesProcessor.filterCastEntriesWithSonar(projects, castObjectsMapWithTDinMinutes);

		//**************** Start Of Total Error Cast CSV ****************//

		//Read from csv with total error number
		//castProjectMap = csvReader.readFromCastCsv(projects);

		//Handle total error number entries


		//		List<List<CastEntry>> listOfCasts = new ArrayList();
		//		Map<String, List<CastEntry>> casts = new HashMap<String, List<CastEntry>>();
		//		for(Map.Entry<String, List<String>> entry : castProjectMap.entrySet()) {
		//			List<CastEntry> castEntriesSorted = castEntriesProcessor.sumViolations(entry.getValue());
		//			casts.put(entry.getKey(), castEntriesSorted);
		//		}

		//**************** End Of Total Error Cast CSV ****************//

		//Read from csv with fix effort metric
		//We get in one map the metrics of all projects (no need for further loop)
		//We get already sorted the list no more processing is needed
		//castObjectsMapWithFixEffort = csvReader.readFromCastCsvWithFixEfford(projects);

		//castObjectsMapWithFixEffort = castEntriesProcessor.filterCastEntriesWithSonar(projects, castObjectsMapWithFixEffort);


//** Uncomment
		for(int i=0;i<projects.length;i++){
	/*		int perCent = 100;

			//Lists only with issues
			List<SonarEntryWithIssues> sonars = reader.getSonarXmlSortedList(projects[i],"Metrics");
			List<SquoreEntryWithIssues> squores = reader.ReadSquoreXml(projects[i]);
			List<CastEntry> cast = castObjectsMapWithTDinMinutes.get(projects[i]);

			TotalEntriesProcessor totalEntriesProcessor = new TotalEntriesProcessor();
			totalEntriesProcessor.writeAllEntriesToXML(sonars, squores, cast, projects[i]);

			System.out.println("For project "+projects[i]);

			int sonarTenPerCent = (sonars.size()*perCent) / 100 ;
			System.out.println("Sonar per cent: " + sonarTenPerCent);
			int squoreTenPerCent = (squores.size()*perCent)/100;
			System.out.println("Squore per cent: " + squoreTenPerCent);
			int castTenPerCent = (cast.size()*perCent)/100;
			System.out.println("Cast per cent: "+castTenPerCent);

			List<SonarEntryWithIssues> sonarPart = new ArrayList<>();

			for(int j=0; j<sonarTenPerCent;j++){
				sonarPart.add(sonars.get(j));
			}

			List<SquoreEntryWithIssues> squoresPart = new ArrayList<>();

			for(int k=0;k<squoreTenPerCent;k++){
				squoresPart.add(squores.get(k));
			}

			List<CastEntry> castsPart = new ArrayList<>();

			for(int k=0;k<castTenPerCent;k++){
				castsPart.add(cast.get(k));
			}			

			List<String> overall = Comparators.compareAllThree(sonarPart, squoresPart, castsPart);

			//writer.writeClassNames(projects[i]+"_Same_Classes_"+perCent+"_All", "Compare All Three", overall);
			//List<String> sonarVsSquore =Comparators.compareSonarWithSquore(sonarPart, squoresPart);
			//writer.writeClassNames(projects[i]+"_Same_Classes_"+perCent+"_SonarVsSquore", "Compare Sonar With Squore", sonarVsSquore);
			//List<String> sonarVsCast =Comparators.compareSonarWithCast(sonarPart, castsPart);
			//writer.writeClassNames(projects[i]+"_Same_Classes_"+perCent+"_SonarVsCast", "Compare Sonar With Cast", sonarVsCast);
			//List<String> squoreVsCast =Comparators.compareSquoreWithCast(squoresPart, castsPart);
			//writer.writeClassNames(projects[i]+"_Same_Classes_"+perCent+"_SquoreVsCast", "Compare Squore With Cast", squoreVsCast);


			//CsvReader csvReader = new CsvReader();
			//squoreProjectsMap = csvReader.readFromCsv(projects);	

			//castProjectMap = csvReader.readFromCastCsv(projects);

			//CastEntriesProcessor castEntriesProcessor = new CastEntriesProcessor();
			//List<List<CastEntry>> listOfCasts = new ArrayList();
			//Map<String, List<CastEntry>> casts = new HashMap<String, List<CastEntry>>();
			//for(Map.Entry<String, List<String>> entry : castProjectMap.entrySet()) {
			//	List<CastEntry> castEntriesSorted = castEntriesProcessor.sumViolations(entry.getValue());
			//	System.out.println(entry.getKey() + " List sortedSize: "+castEntriesSorted.size());
			//	casts.put(entry.getKey(), castEntriesSorted);
			//}
*/
			SonarEntriesProcessor sonarEntriesProcessor = new SonarEntriesProcessor();

			//Read from sonar and format a single entry to write in xml
				MainPageEntry mainPageEntry = sonarEntriesProcessor.getMainEntry(projects[i]);

			//Read from sonar api with metrics and write to xml file in one method
			sonarEntriesProcessor.readFromAPIAndWriteToXML(projects[i]);

			//Read from csv squore files
			//SquoreEntriesProcessor squoreEntriesProcessor = new SquoreEntriesProcessor();
			//List<SquoreEntry> squoreEntriesToWrite = squoreEntriesProcessor.compareSonarWithSquoreProjects(squoreProjectsMap.get(projects[i]), Arrays.asList(mainPageEntry.getComponents()));
			//System.out.println("Squores: "+squoreEntriesToWrite.size());

			//Write to XML as one object
			//XmlWriter xmlWriter = new XmlWriter();
			//xmlWriter.writeCastsToXML(projects[i],casts.get(projects[i]));
			//xmlWriter.writeSonarsToXML(projects[i], mainPageEntry);	
			//xmlWriter.writeSquoresToXML(projects[i], squoreEntriesToWrite);

			//}	
		}	

	}
}
