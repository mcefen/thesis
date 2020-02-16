package cast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.CsvReader;
import common.XmlReader;
import common.XmlWriter;
import sonar.SonarEntryWithIssues;

public class CastEntriesProcessor {
	
	Map<String, Integer> violations;
	List<CastEntry> castEntries;

	public List<CastEntry> sumViolations(List<String> violationList){
		
		violations = new HashMap<String, Integer>();
		castEntries = new ArrayList();
		
		for(String violation : violationList){
			
			String violationClass = "";
			
			if (null != violation && violation.length() > 0 )
			{
			    int endIndex = violation.lastIndexOf(".");
			    if (endIndex != -1)  
			    {
			    	violationClass = violation.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
			    }
			} 
			
			if(!violations.containsKey(violationClass)){
				violations.put(violationClass, 0);
			}
			
			violations.put(violationClass, violations.get(violationClass) + 1);
		}
		
		for(Map.Entry<String, Integer> entry : violations.entrySet()) {
		    String key = entry.getKey();
		    Integer value = entry.getValue();
		    
		    CastEntry cEntry = new CastEntry();
		    cEntry.setPath(key);
		    cEntry.setTotalErrors(String.valueOf(value));
		    
		    castEntries.add(cEntry);
		    //System.out.println("Path : " + cEntry.getPath());
		    //System.out.println("Errors : " + cEntry.getTotalErrors());
		}
		
		return castEntries;
	}
	
	public List<CastEntry> getSortedList(List<CastEntry> castEntries, String mode){
		if(mode.equals("TotalErrors")){
			Collections.sort(castEntries, 
	                (o1, o2) -> Integer.valueOf(o2.getTotalErrors()).compareTo(Integer.valueOf(o1.getTotalErrors())));
			return castEntries;
		} else if(mode.equals("TDinMinutes")){
			Collections.sort(castEntries, 
	                (o1, o2) -> Double.valueOf(o2.getTdContributionInMinutes()).compareTo(Double.valueOf(o1.getTdContributionInMinutes())));
			
			return castEntries;
		} else {
			return castEntries;
		}
		
	}
	
	public Map<String, List<CastEntry>> groupEntriesByName(String[] projects, Map<String, List<CastEntry>> castObjectsMap){
		XmlReader reader = new XmlReader();
		
		Map<String, List<CastEntry>> castObjectsMapFiltered = new HashMap<>();
		for(int j=0;j<projects.length;j++){

			List<CastEntry> castsToFilter = castObjectsMap.get(projects[j]);

			List<CastEntry> castsGrouped = new ArrayList<>();
						
			for(CastEntry castEntry : castsToFilter){
				boolean found = false;
				for(CastEntry groupedEntry : castsGrouped){
					
					if(castEntry.getPath().trim().equals(groupedEntry.getPath().trim())){
						
						//System.out.println("Error Debt: "+castEntry.getTdContributionInMinutes());
						double currentDebtInMinutes = Double.parseDouble(groupedEntry.getTdContributionInMinutes());
						//System.out.println("Error: "+castEntry.getTdContributionInMinutes());
						double newDebtInMinutes = Double.parseDouble(castEntry.getTdContributionInMinutes());
						double newTotalDebtInMinutes = currentDebtInMinutes + newDebtInMinutes;
						groupedEntry.setTdContributionInMinutes(String.valueOf(newTotalDebtInMinutes));
						
//						double currentFixEffordInMinutes = Double.parseDouble(groupedEntry.getFixEffortInMinutes());
//						double newFixEffordInMinutes = Double.parseDouble(castEntry.getFixEffortInMinutes());
//						double newTotalFixEffordInMinutes = currentFixEffordInMinutes + newFixEffordInMinutes;
//						groupedEntry.setFixEffortInMinutes(String.valueOf(newTotalFixEffordInMinutes));
//						
						//System.out.println("Error Total Errors: "+castEntry.getTotalErrors());
						int currentTotalErrors = Integer.parseInt(groupedEntry.getTotalErrors());
						int newTotalErrors = Integer.parseInt(castEntry.getTotalErrors());
						int newTotalTotalErrors = currentTotalErrors + newTotalErrors;
						groupedEntry.setTotalErrors(String.valueOf(newTotalTotalErrors));
						
						found = true;
					} 					
				}
				
				if(!found){
					castsGrouped.add(castEntry);
				}				
			}
			
			//System.out.println("My Size: " + castsGrouped.size());
			castObjectsMapFiltered.put(projects[j], castsGrouped);			
		}
		return castObjectsMapFiltered;
	}
	
	public Map<String, List<CastEntry>> filterCastEntriesWithSonar(String[] projects, Map<String, List<CastEntry>> castObjectsMap){
		XmlReader reader = new XmlReader();
		
		Map<String, List<CastEntry>> castObjectsMapFiltered = new HashMap<>();
		for(int j=0;j<projects.length;j++){
			List<SonarEntryWithIssues> sonars = reader.ReadAllSonarXmlForInfo(projects[j]);
			System.out.println("Sonars size list" + sonars.size());
			List<CastEntry> castsToFilter = castObjectsMap.get(projects[j]);

			List<CastEntry> castsFiltered = new ArrayList<>();

			for(CastEntry castEntry : castsToFilter){
				
				//Cast stores paths with backslash
				castEntry.setPath(castEntry.getPath().replace("\\", "/"));
				//System.out.println("Cast Entry Path: "+ castEntry.getPath());
				for(SonarEntryWithIssues sewi : sonars){
					//System.out.println("Sonar Entry Path: "+sewi.getPath());
					//String pathToCompare = sewi.getPath().split("src")[1];
					if(sewi.getPath().contains(castEntry.getPath())){
						//System.out.println("found match");
						castsFiltered.add(castEntry);
						break;
					}
				}				
			}
			
			List<CastEntry> entries = getSortedList(castsFiltered, "TDinMinutes");

			castObjectsMapFiltered.put(projects[j], entries);			
		}
		return castObjectsMapFiltered;
	}
	
	public void readCastsAndWriteXML(String[] projects){
		System.out.println("Cast");
		CsvReader csvReader = new CsvReader();
		Map<String, List<CastEntry>> castObjectsMapWithTDinMinutes = new HashMap<String, List<CastEntry>>();
		
		castObjectsMapWithTDinMinutes = csvReader.readFromCastCsvWithTDinMinutes(projects);
		
		castObjectsMapWithTDinMinutes = groupEntriesByName(projects, castObjectsMapWithTDinMinutes);
		
		castObjectsMapWithTDinMinutes = filterCastEntriesWithSonar(projects, castObjectsMapWithTDinMinutes);

		for(int i=0;i<projects.length;i++){
			XmlWriter xmlWriter = new XmlWriter();
			xmlWriter.writeCastsWithTDinMinutesToXML(projects[i],castObjectsMapWithTDinMinutes.get(projects[i]));
		}
	}
	
	public Map<String, List<CastEntry>> readCastsAndFilterWithSonar(String[] projects){
		CsvReader csvReader = new CsvReader();
		Map<String, List<CastEntry>> castObjectsMapWithTDinMinutes = new HashMap<String, List<CastEntry>>();
		
		castObjectsMapWithTDinMinutes = csvReader.readFromCastCsvWithTDinMinutes(projects);
		
		castObjectsMapWithTDinMinutes = groupEntriesByName(projects, castObjectsMapWithTDinMinutes);
		
		castObjectsMapWithTDinMinutes = filterCastEntriesWithSonar(projects, castObjectsMapWithTDinMinutes);

		return castObjectsMapWithTDinMinutes;
	}
}
