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

	public static void main(String argv[]) throws Exception {

		String[] projects = {"mina", "deltaspike", "opennlp", "maven", "wss4j", "pdfbox", "fop", "cayenne", "jclouds", "openjpa"};

		//Read from XML of sonar and write only issues
		XmlReader reader = new XmlReader();

		
		//Just read from XML - remove comments from reader classes
		for(int i=0;i<projects.length;i++){
			List<SonarEntryWithIssues> allSonars = new ArrayList();			
			allSonars = reader.ReadAllSonarXmlForInfo(projects[i]);
			
			
			List<SquoreEntryWithIssues> allSquores = new ArrayList();
			allSquores = reader.ReadSquoreXml(projects[i]);
		}

		/*All 3 with total issues*/

		CsvReader csvReader = new CsvReader();

		//**************** Start Of Total Error Cast CSV ****************//
		
		//Read from csv with total error number
		castProjectMap = csvReader.readFromCastCsv(projects);

		//Handle total error number entries
		CastEntriesProcessor castEntriesProcessor = new CastEntriesProcessor();
		List<List<CastEntry>> listOfCasts = new ArrayList();
		Map<String, List<CastEntry>> casts = new HashMap<String, List<CastEntry>>();
		for(Map.Entry<String, List<String>> entry : castProjectMap.entrySet()) {
			List<CastEntry> castEntriesSorted = castEntriesProcessor.sumViolations(entry.getValue());
			casts.put(entry.getKey(), castEntriesSorted);
		}
		
		//**************** End Of Total Error Cast CSV ****************//

		//Read from csv with fix effort metric
		//We get in one map the metrics of all projects (no need for further loop)
		//We get already sorted the list no more processing is needed
		castObjectsMapWithFixEffort = csvReader.readFromCastCsvWithFixEfford(projects);

		//Filter cast entries with sonars to eliminate tests
		for(int j=0;j<projects.length;j++){
			List<SonarEntryWithIssues> sonars = reader.ReadAllSonarXmlForInfo(projects[j]);
			List<CastEntry> castsToFilter = castObjectsMapWithFixEffort.get(projects[j]);
			
			List<CastEntry> castsFiltered = new ArrayList<>();
			
			for(CastEntry castEntry : castsToFilter){
				//Cast stores paths with backslash
				castEntry.setPath(castEntry.getPath().replace("\\", "/"));
				for(SonarEntryWithIssues sewi : sonars){
					if(castEntry.getPath().equals(sewi.getSrcPath())){
						castsFiltered.add(castEntry);
						break;
					}
				}				
			}
			
			System.out.println("My Size: " + castsFiltered.size());
			castObjectsMapWithFixEffort.put(projects[j], castsFiltered);			
		}


		for(int i=0;i<projects.length;i++){
			int perCent = 100;
			
			//We read the sonar entries and the squore entries per project
			
			List<SonarEntryWithIssues> sonars = new ArrayList();			
			sonars = reader.ReadAllSonarXmlForInfo(projects[i]);
			
			
			List<SquoreEntryWithIssues> squores = new ArrayList();
			squores = reader.ReadSquoreXml(projects[i]);
			
			//Lists only with issues
			//List<SonarEntryWithIssues> sonars = reader.getSonarXmlSortedList(projects[i],"Metrics");
			//List<SquoreEntryWithIssues> squores = reader.ReadSquoreXml(projects[i]);
			List<CastEntry> cast = castObjectsMapWithFixEffort.get(projects[i]);

			
			//Write All Entries//
			
			List<ProjectEntry> totalEntries = new ArrayList();
			
			for(SonarEntryWithIssues sEntry : sonars){
				
				ProjectEntry projectEntry = new ProjectEntry();
				
				projectEntry.setProjectName(projects[i]);
				
				String[] sonarClass = sEntry.getKey_path().split("src");
				projectEntry.setPath(sonarClass[sonarClass.length-1]);
				
				projectEntry.setDebtSonar(sEntry.getSqualeIndex());				
				totalEntries.add(projectEntry);
			}
			
			for(SquoreEntryWithIssues sqEntry : squores){
				
				String[] squoreClass = sqEntry.getPath().split("src");
				
				ProjectEntry projectEntry = new ProjectEntry();
				
				boolean found = false;
				for(ProjectEntry pe : totalEntries){
					if(squoreClass[squoreClass.length-1].equals(pe.getPath())){
						pe.setDebtSquore(sqEntry.getTechnicalDebt());
						pe.setCanonicalDebtSquore(sqEntry.getCanonicalDebpt());
						found = true;
					}
				}
				
				if(!found){
					projectEntry.setProjectName(projects[i]);
					projectEntry.setPath(squoreClass[squoreClass.length-1]);
					projectEntry.setDebtSonar("0");
					projectEntry.setCanonicalDebtSonar("0");
					projectEntry.setDebtSquore(sqEntry.getTechnicalDebt());
					projectEntry.setCanonicalDebtSquore(sqEntry.getCanonicalDebpt());
				
					totalEntries.add(projectEntry);
				}				
			}
			
			for(CastEntry castEntry: cast){
				ProjectEntry projectEntry = new ProjectEntry();
				
				boolean found = false;
				
				for(ProjectEntry pe : totalEntries){
					
					if(castEntry.getPath().equals(pe.getPath())){
						
						BigDecimal bd = new BigDecimal(castEntry.getFixEffort());
					    bd = bd.setScale(2, RoundingMode.HALF_UP);
					    bd.doubleValue();
						
					    pe.setDebtCast(bd.doubleValue()+"");
						
						found = true;
					}								
				}	
				
				if(!found){
					projectEntry.setProjectName(projects[i]);
					projectEntry.setPath(castEntry.getPath());
					projectEntry.setDebtSonar("0");
					projectEntry.setCanonicalDebtSonar("0");
					projectEntry.setDebtSquore("0");
					projectEntry.setCanonicalDebtSquore("0");
					
					BigDecimal bd = new BigDecimal(castEntry.getFixEffort());
				    bd = bd.setScale(2, RoundingMode.HALF_UP);
				    bd.doubleValue();
					
					projectEntry.setDebtCast(bd.doubleValue()+"");
				
					totalEntries.add(projectEntry);
				}	
			}
			
			
			TxtWriter writer = new TxtWriter();
			
			File logFile = new File(projects[i]+"_totalEntries");
			
			BufferedWriter textWriter = new BufferedWriter(new FileWriter(logFile));
			
			for(ProjectEntry pe: totalEntries){
				
				textWriter.write(pe.getProjectName() + "," 
						+pe.getPath() +"," 
						+pe.getDebtSonar() + ","
						+pe.getDebtSquore() + ","
						+pe.getCanonicalDebtSquore() + ","
						+pe.getDebtCast());
				
				textWriter.newLine();
				
			}
			
			textWriter.close();
			
			//writer.writeSonars(projects[i]+"_Total_Sonars",sonars);
			//writer.writeSquores(projects[i]+"_Total_Squores",squores);
			//writer.writeCasts(projects[i]+"_Total_Casts",cast);
			/*System.out.println("For project "+projects[i]);
			
			int sonarTenPerCent = (sonars.size()*perCent) / 100 ;
			System.out.println("Sonar per cent: " + sonarTenPerCent);
			int squoreTenPerCent = (squores.size()*perCent)/100;
			System.out.println("Squore per cent: " + squoreTenPerCent);
			int castTenPerCent = (cast.size()*perCent)/100;
			System.out.println("Cast per cent: "+castTenPerCent);
			
			System.out.println("Sonar top: " + sonars.get(0).getKey_path());
			System.out.println("Squore top: " + squores.get(0).getPath());
			System.out.println("Cast top: " + cast.get(0).getPath());

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
			
			writer.writeSonars(projects[i]+"_"+perCent+"_Sonars",sonarPart);
			writer.writeSquores(projects[i]+"_"+perCent+"_Squores",squoresPart);
			writer.writeCasts(projects[i]+"_"+perCent+"_Casts",castsPart);
			
			List<String> overall = Comparators.compareAllThree(sonarPart, squoresPart, castsPart);
			writer.writeClassNames(projects[i]+"_Same_Classes_"+perCent+"_All", "Compare All Three", overall);
			List<String> sonarVsSquore =Comparators.compareSonarWithSquore(sonarPart, squoresPart);
			writer.writeClassNames(projects[i]+"_Same_Classes_"+perCent+"_SonarVsSquore", "Compare Sonar With Squore", sonarVsSquore);
			List<String> sonarVsCast =Comparators.compareSonarWithCast(sonarPart, castsPart);
			writer.writeClassNames(projects[i]+"_Same_Classes_"+perCent+"_SonarVsCast", "Compare Sonar With Cast", sonarVsCast);
			List<String> squoreVsCast =Comparators.compareSquoreWithCast(squoresPart, castsPart);
			writer.writeClassNames(projects[i]+"_Same_Classes_"+perCent+"_SquoreVsCast", "Compare Squore With Cast", squoreVsCast);

*/
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

		//SonarEntriesProcessor sonarEntriesProcessor = new SonarEntriesProcessor();
		//for (int i = 0; i < projects.length;i++){
		//	System.out.println(projects[i]);

		//Read from sonar and format a single entry to write in xml
		//	MainPageEntry mainPageEntry = sonarEntriesProcessor.getMainEntry(projects[i]);

		//	MainPageWithEntries mainPageWithMetrics = sonarEntriesProcessor.getMainPageWithMetrics(projects[i]);

		//Read from csv squore files
		//SquoreEntriesProcessor squoreEntriesProcessor = new SquoreEntriesProcessor();
		//List<SquoreEntry> squoreEntriesToWrite = squoreEntriesProcessor.compareSonarWithSquoreProjects(squoreProjectsMap.get(projects[i]), Arrays.asList(mainPageEntry.getComponents()));
		//System.out.println("Squores: "+squoreEntriesToWrite.size());

		//Write to XML as one object
		//	XmlWriter xmlWriter = new XmlWriter();
		//	xmlWriter.writeCastsToXML(projects[i],casts.get(projects[i]));
		//xmlWriter.writeSonarsToXML(projects[i], mainPageEntry);	
		//xmlWriter.writeSonarsWithMetricsToXML(projects[i], mainPageWithMetrics);
		//xmlWriter.writeSquoresToXML(projects[i], squoreEntriesToWrite);

		//}	
	}	
	
	}
}
