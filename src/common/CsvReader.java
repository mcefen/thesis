package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cast.CastEntry;
import squore.SquoreEntriesProcessor;
import squore.SquoreEntry;
/***
 * Read from CSV files create entries and return all entries mapped by project */
public class CsvReader {

	private final String CSV_SQUORE_READ_PATH = "C:/Users/Nasia/Desktop/squore_csv/";
	private final String CSV_CAST_READ_PATH = "C:/Users/Nasia/Desktop/cast_csv/";
	private final String CSV_CAST_READ_PATH_WITH_FIX_EFFORD = "C:/Users/Nasia/Desktop/cast_csv/csv_with_fix_efford/";
	private final String CSV_SEPARATOR = ";";
	private final String CSV_SEPARATOR_ALT = ",";

	private Map<String, List<SquoreEntry>> squoreProjectsMap;

	private Map<String, List<String>> castObjectsMap;
	private Map<String, List<CastEntry>> castObjectsMapWithFixEffort;

	public CsvReader(){
		super();
		squoreProjectsMap = new HashMap<String, List<SquoreEntry>>();
		castObjectsMap = new HashMap<String, List<String>>();
		castObjectsMapWithFixEffort = new HashMap<String, List<CastEntry>>();
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
			String finalPath = CSV_SQUORE_READ_PATH + projects[i]+".csv";

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

	/** Get a csv file and create a list with squore entries in it
	 *  csv files have to be named after the project name
	 *  @param  projects  A list of all project names
	 *  @return map       Map of project and a list of squore entries for each
	 *  */
	public Map<String, List<String>> readFromCastCsv(String[] projects){

		String line = "";        

		for(int i=0;i<projects.length;i++){
			List<String> entries = new ArrayList<>();

			String folderPath = CSV_CAST_READ_PATH + projects[i];
			System.out.println(folderPath);

			File folder = new File(folderPath);
			File[] listOfFiles = folder.listFiles();

			if(listOfFiles!=null){
				for (int j = 0; j < listOfFiles.length; j++){
					if (listOfFiles[i].isFile()){
						String fileToRead = listOfFiles[j].getName();

						String finalPath = folderPath+"/"+fileToRead;

						try (BufferedReader br = new BufferedReader(new FileReader(finalPath))) {
							br.readLine();

							while ((line = br.readLine()) != null) {
								String[] file = line.split(CSV_SEPARATOR);
								if(file.length>1){
									entries.add(file[2]);
								}

							}

						} catch (IOException e) {
							System.out.println("Unable to find file");
							e.printStackTrace();
						}        			
					}
				}
			}        	

			castObjectsMap.put(projects[i], entries);
		}
		return castObjectsMap;
	}	

	public Map<String, List<CastEntry>> readFromCastCsvWithFixEfford(String[] projects){

		String line = "";        

		for(int i=0;i<projects.length;i++){
			List<CastEntry> entries = new ArrayList<>();

			String folderPath = CSV_CAST_READ_PATH_WITH_FIX_EFFORD + projects[i];


			String finalPath = folderPath+"_metrics_sql.csv";

			try (BufferedReader br = new BufferedReader(new FileReader(finalPath))) {
				br.readLine();

				while ((line = br.readLine()) != null) {
					String[] file = line.split(CSV_SEPARATOR_ALT);
					if(file.length>2){
						CastEntry ce = new CastEntry();
						
						//Remove all previous details
						String[] croppedPath = file[0].split("src");
						
						//Check if it is in the actual path of src
						if(croppedPath.length>1){
							//The path should contain .java to be a java class							
							if(croppedPath[1].contains(".java")){
								ce.setPath(croppedPath[1]);
								ce.setFixEffort(file[2]);
								entries.add(ce);
								//We have filter java classes 
								//but we should also check if
								//the class is in sonar filtered classes
								//to eliminate test classes
							}
						}						
					}
				}

			} catch (IOException e) {
				System.out.println("Unable to find file");
				e.printStackTrace();
			}  
			
			Collections.sort(entries, 
	                (o1, o2) -> Double.valueOf(o2.getFixEffort()).compareTo(Double.valueOf(o1.getFixEffort())));
			
			/*for(CastEntry cent : entries){
				System.out.println("Path: "+cent.getPath());
				System.out.println("Efford: "+cent.getFixEffort());
			}
*/
			castObjectsMapWithFixEffort.put(projects[i], entries);
		}
		return castObjectsMapWithFixEffort;
	}	
}
