package squore;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import sonar.FileSonarQubeEntry;
/** Method to handle Squore entries*/
public class SquoreEntriesProcessor {

	private final String PATH_SEPARATOR = "src";

	public SquoreEntriesProcessor(){
		super();
	}

	/** Checks which squore entries also exist in sonar files
	 * @param  squores          The list of squore entries 
	 * @param  sonars           The list of sonar entries 
	 * @return matchinsSquores  The list of squore entries that also exists in sonar files*/
	public List<SquoreEntry> compareSonarWithSquoreProjects(List<SquoreEntry> squores, List<FileSonarQubeEntry> sonars) throws FileNotFoundException, UnsupportedEncodingException{
		System.out.println("Sonars: "+sonars.size());

		List<SquoreEntry> matchingSquores = new ArrayList<>();

		for(SquoreEntry sq: squores){

			//Squore keeps in different variables the java file name from the previous path
			//We have to merge them to get the full path
			String squorePath = sq.getPath()+"/"+ sq.getFileName();

			//Paths are formatted differently for some projects
			//However from src and after the paths are formatted the same
			//So we use only this part of path to avoid false negative comparisons
			//due to different reference to module names
			String[] squorePartialPath = squorePath.split(PATH_SEPARATOR);
			for(FileSonarQubeEntry sqe : sonars){
				if(sqe !=null){

					//We have to split sonar path the same way from src
					String[] sonarPartialPath = sqe.getKey().split(PATH_SEPARATOR);


					if((squorePartialPath.length>1)&&(sonarPartialPath.length>1)){
						//Form a list with squore entries that exist also in sonar
						if(squorePartialPath[squorePartialPath.length-1].trim().toUpperCase().equals(sonarPartialPath[sonarPartialPath.length-1].trim().toUpperCase())){
							matchingSquores.add(sq);
							break;
						}
					}
				}
			}
		}
		return matchingSquores;
	}

	/** Calculates the technical debt in minutes
	 *  If there is a need to calculate the debt in the same units for comparison
	 *  @param techicalDebt  The string representing technical debt with the the numbers and units
	 *  @return totalMins    Total minutes of technical debt */
	public double calculateCanonicalTechnicalDebt(String technicalDebt){
		String[] splitStr = technicalDebt.split("\\s+");
		double totalMins = 0;
		for(int i=0;i<splitStr.length;i++){
			if(splitStr[i].contains("M/d")){
				String[] debtNum = splitStr[i].split("M/d");
				totalMins = totalMins + (Integer.parseInt(debtNum[0])*60*8);
			} else if(splitStr[i].contains("h")){
				String[] debtNum = splitStr[i].split("h");
				totalMins = totalMins + (Integer.parseInt(debtNum[0])*60);
			} else if(splitStr[i].contains("m")){
				String[] debtNum = splitStr[i].split("m");
				totalMins = totalMins + Integer.parseInt(debtNum[0]);
			} else if(splitStr[i].contains("s")){
				String[] debtNum = splitStr[i].split("s");
				totalMins = totalMins + 0.5;
			}
		}
		
		return totalMins;
	}


}
