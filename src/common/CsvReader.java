package common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import squore.SquoreEntriesProcessor;
import squore.SquoreEntry;
/***
 * Read from CSV files create entries and return all entries mapped by project */
public class CsvReader {

	private final String CSV_READ_PATH = "C:/Users/Nasia/Desktop/squore_csv/";
	private final String CSV_SEPARATOR = ";";
	
	private Map<String, List<SquoreEntry>> squoreProjectsMap;
	
	public CsvReader(){
		super();
		squoreProjectsMap = new HashMap<String, List<SquoreEntry>>();
	}
	
	/** Get a csv file and create a list with squore entries in it
	 *  csv files have to be named after the project name
	 *  @param  projects  A list of all project names
	 *  @return map       Map of project and a list of squore entries for each
	 *  */
	public Map<String, List<SquoreEntry>> readFromCsv(String[] projects){
		
        String line = "";        

        for(int i=0;i<projects.length;i++){
        	List<SquoreEntry> entries = new ArrayList<>();
        	SquoreEntriesProcessor squoreProcessor = new SquoreEntriesProcessor();
        	//Path to csv file
        	String finalPath = CSV_READ_PATH + projects[i]+".csv";
        	
        	try (BufferedReader br = new BufferedReader(new FileReader(finalPath))) {
        		br.readLine();
        		//Create squore entries for each of the file lines
                while ((line = br.readLine()) != null) {

                    String[] file = line.split(CSV_SEPARATOR);
                    //Pts/KLoc
                    SquoreEntry squoreEntry = new SquoreEntry();
                    squoreEntry.setRating(file[0]);
                    squoreEntry.setFileName(file[1]);
                    squoreEntry.setTechnicalDept(file[2]);
                    squoreEntry.setLineCount(file[3].replace(",", ""));
                    squoreEntry.setViolationsDensity(file[4].replace("Pts/KLoc", ""));
                    squoreEntry.setBlockerIssues(file[5]);
                    squoreEntry.setCriticalIssues(file[6]);
                    squoreEntry.setMajorIssues(file[7]);
                    squoreEntry.setMinorIssues(file[8]);
                    squoreEntry.calculateTotalIssues();
                    squoreEntry.setAverageCyclomaticComplexity(file[9]);
                    squoreEntry.setCyclomaticComplexity(file[10]);
                    squoreEntry.setPath(file[11]);                  
                    
                    squoreEntry.setCanonicalTechnicalDept(squoreProcessor.calculateCanonicalTechnicalDebt(file[2]));
                    
                    //System.out.println("TechDebpt: " + squoreEntry.getTechnicalDept());
                    
                    entries.add(squoreEntry);
                }

            } catch (IOException e) {
            	System.out.println("Unable to find file");
                e.printStackTrace();
            }
        	
        	squoreProjectsMap.put(projects[i], entries);
        }
        return squoreProjectsMap;
	}	
}
