package xmltest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import squore.SquoreEntry;
/***
 * Read from CSV files create entries and return all entries mapped by project */
public class CsvReader {

	private final String CSV_READ_PATH = "C:/Users/Nasia/Desktop/csv-20180830T180525Z-001/csv/";
	private final String CSV_SEPARATOR = ";";
	
	private Map<String, List<SquoreEntry>> squoreProjectsMap;
	
	public CsvReader(){
		super();
		squoreProjectsMap = new HashMap<String, List<SquoreEntry>>();
	}
	
	public Map<String, List<SquoreEntry>> readFromCsv(String[] projects){
		
        String line = "";        

        for(int i=0;i<projects.length;i++){
        	List<SquoreEntry> entries = new ArrayList<>();
        	
        	String finalPath = CSV_READ_PATH + projects[i]+".csv";
        	
        	try (BufferedReader br = new BufferedReader(new FileReader(finalPath))) {

        		//Create squore entries for each of the file lines
                while ((line = br.readLine()) != null) {

                    String[] file = line.split(CSV_SEPARATOR);
                    
                    SquoreEntry squoreEntry = new SquoreEntry();
                    squoreEntry.setRating(file[0]);
                    squoreEntry.setFileName(file[1]);
                    squoreEntry.setTechnicalDept(file[2]);
                    squoreEntry.setLineCount(file[3]);
                    squoreEntry.setViolationsDensity(file[4]);
                    squoreEntry.setBlockerIssues(file[5]);
                    squoreEntry.setCriticalIssues(file[6]);
                    squoreEntry.setAverageCyclomaticComplexity(file[7]);
                    squoreEntry.setCyclomaticComplexity(file[8]);
                    squoreEntry.setPath(file[9]);
                    
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
