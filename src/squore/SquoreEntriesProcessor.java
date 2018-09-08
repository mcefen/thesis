package squore;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import sonar.FileSonarQubeEntry;

public class SquoreEntriesProcessor {
	
	private final String PATH_SEPARATOR = "src";
	
	public SquoreEntriesProcessor(){
		super();
	}
	
	public List<SquoreEntry> compareSonarWithSquoreProjects(List<SquoreEntry> squores, List<FileSonarQubeEntry> sonars) throws FileNotFoundException, UnsupportedEncodingException{
		System.out.println("Sonars: "+sonars.size());

		List<SquoreEntry> matchingSquores = new ArrayList<>();
		
		for(SquoreEntry sq: squores){

			String squorePath = sq.getPath()+"/"+ sq.getFileName();
			String[] squorePartialPath = squorePath.split(PATH_SEPARATOR);
			for(FileSonarQubeEntry sqe : sonars){
				if(sqe !=null){

					String[] sonarPartialPath = sqe.getKey().split(PATH_SEPARATOR);

					if((squorePartialPath.length>1)&&(sonarPartialPath.length>1)){

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

}
