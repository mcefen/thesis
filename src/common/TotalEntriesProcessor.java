package common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import cast.CastEntry;
import sonar.SonarEntryWithIssues;
import squore.SquoreEntryWithIssues;

public class TotalEntriesProcessor {
	
	public void writeAllEntriesToXML(List<SonarEntryWithIssues> sonars,
			List<SquoreEntryWithIssues> squores,
			List<CastEntry> cast,
			String project) throws IOException{
		
		//Merge entries from all tools into one single list
		//Get all sonar entries
		List<ProjectEntry> totalEntries = getSonarEntries(sonars, project);
		//If squore entry already exists in sonar then update entry adding squore
		//metrics, else add new entry and set sonar metrics to zero
		totalEntries = getSquoreEntries(totalEntries, squores, project);	
		//If cast entry already exists in previous tools update entry adding cast
		//metrics else add new entry and set previous tools metrics to zero
		totalEntries = getCastEntries(totalEntries, cast, project);
		
		writeToFile(project, totalEntries);		
	}
	
	public List<ProjectEntry> getSonarEntries(List<SonarEntryWithIssues> sonars, String project){
		List<ProjectEntry> totalEntries = new ArrayList();

		for(SonarEntryWithIssues sEntry : sonars){

			ProjectEntry projectEntry = new ProjectEntry();

			projectEntry.setProjectName(project);
			
			//System.out.println("Sonar path: " + sEntry.getKey_path());
			if(!sEntry.getPath().contains("/test/")){
				//String[] sonarClass = sEntry.getKey_path().split("src");

				boolean found = false;
				//Avoid duplication in names
				for(ProjectEntry pe : totalEntries){
					if(pe.getPath().equals(sEntry.getPath())){
						found = true;
					}
				}

				if(!found){
					projectEntry.setPath(sEntry.getPath());
					projectEntry.setDebtSonar(sEntry.getSqualeIndex());	
					projectEntry.setTotalIssuesSonar(sEntry.getTotalIssues());
					totalEntries.add(projectEntry);
				}
			}

			
		}		
		return totalEntries;
	}
	
	public List<ProjectEntry> getSquoreEntries(List<ProjectEntry> totalEntries, 
			List<SquoreEntryWithIssues> squores, 
			String project){		
		
		for(SquoreEntryWithIssues sqEntry : squores){
			
		
			//System.out.println("Squore path: " + sqEntry.getPath());
			if(!sqEntry.getPath().contains("/test/")){
				String[] squoreClass = sqEntry.getPath().split("src");

				ProjectEntry projectEntry = new ProjectEntry();

				boolean found = false;
				//If found then update entry
				for(ProjectEntry pe : totalEntries){
					String[] pathSplit = pe.getPath().split("src");
					if(squoreClass[squoreClass.length-1].equals(pathSplit[pathSplit.length-1])){
						pe.setDebtSquore(sqEntry.getTechnicalDebt());
						pe.setCanonicalDebtSquore(sqEntry.getCanonicalDebpt());
						pe.setTotalIssuesSquore(sqEntry.getTotalIssues());
						found = true;
					}
				}

				//If not found add new entry
				if(!found){
					projectEntry.setProjectName(project);
					projectEntry.setPath(squoreClass[squoreClass.length-1]);
					projectEntry.setDebtSonar("0");
					projectEntry.setCanonicalDebtSonar("0");
					projectEntry.setTotalIssuesSonar("0");
					projectEntry.setDebtSquore(sqEntry.getTechnicalDebt());
					projectEntry.setCanonicalDebtSquore(sqEntry.getCanonicalDebpt());
					projectEntry.setTotalIssuesSquore(sqEntry.getTotalIssues());

					totalEntries.add(projectEntry);
				}	
			}
			
						
		}
		
		return totalEntries;		
	}
	
	public List<ProjectEntry> getCastEntries(List<ProjectEntry> totalEntries,
			List<CastEntry> cast,
			String project){
		
		for(CastEntry castEntry: cast){
			ProjectEntry projectEntry = new ProjectEntry();

			
			//System.out.println("Cast path: " + castEntry.getPath());
			if(!castEntry.getPath().contains("/test/")){
				boolean found = false;
				//If found then update entry
				for(ProjectEntry pe : totalEntries){

					if(pe.getPath().contains(castEntry.getPath())){

						BigDecimal bd = new BigDecimal(castEntry.getTdContributionInMinutes());
						bd = bd.setScale(2, RoundingMode.HALF_UP);
						bd.doubleValue();

						pe.setDebtCast(bd.doubleValue()+"");
						pe.setTotalIssuesCast(castEntry.getTotalErrors());
						found = true;
					}								
				}	

				//If not found then add new entry
				if(!found){
					projectEntry.setProjectName(project);
					projectEntry.setPath(castEntry.getPath());
					projectEntry.setDebtSonar("0");
					projectEntry.setTotalIssuesSonar("0");
					projectEntry.setCanonicalDebtSonar("0");
					projectEntry.setDebtSquore("0");
					projectEntry.setCanonicalDebtSquore("0");
					projectEntry.setTotalIssuesSquore("0");

					BigDecimal bd = new BigDecimal(castEntry.getTdContributionInMinutes());
					bd = bd.setScale(2, RoundingMode.HALF_UP);
					bd.doubleValue();

					projectEntry.setDebtCast(bd.doubleValue()+"");
					projectEntry.setTotalIssuesCast(castEntry.getTotalErrors());

					totalEntries.add(projectEntry);
				}
			}
			
				
		}
		return totalEntries;		
	}
	
	public void writeToFile(String project, List<ProjectEntry> totalEntries) throws IOException{
		TxtWriter writer = new TxtWriter();

		File logFile = new File(project+"_totalEntries");

		BufferedWriter textWriter = new BufferedWriter(new FileWriter(logFile));

		for(ProjectEntry pe: totalEntries){

			textWriter.write(pe.getProjectName() + "," 
					+pe.getPath() +"," 
					+pe.getTotalIssuesSonar() + ","
					+pe.getDebtSonar() + ","
					+pe.getTotalIssuesSquore() + ","
					+pe.getDebtSquore() + ","
					+pe.getCanonicalDebtSquore() + ","
					+pe.getTotalIssuesCast() + ","
					+pe.getDebtCast()
					);

			textWriter.newLine();

		}

		textWriter.close();
	}

}
