package common;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cast.CastEntry;
import sonar.SonarEntryWithIssues;
import squore.SquoreEntryWithIssues;

public class Comparators {
	
	public static final DecimalFormat f = new DecimalFormat("##.00");

	public static List<String> compareAllThree(List<SonarEntryWithIssues> sonarPart,
			List<SquoreEntryWithIssues> squoresPart,
			List<CastEntry> castsPart){

		System.out.println("Compare all three");
		List<String> classNames = new ArrayList<>();
		int sameCounter = 0;

		//Find sonars
		for(SonarEntryWithIssues sewi : sonarPart){
			for(SquoreEntryWithIssues sqewi : squoresPart){
				
				String[] sonarClass = sewi.getKey_path().split("src");
				String[] squoreClass = sqewi.getPath().split("src");

				//Similarities with squore
				if(sonarClass[sonarClass.length-1].equals(squoreClass[squoreClass.length-1])){
					//System.out.println("Sonar path: "+sonarClass[sonarClass.length-1]);
					//System.out.println("Squore path: "+squoreClass[squoreClass.length-1]);
					for(CastEntry ce : castsPart){

						//Similarities with cast
						if(sewi.getKey_path().split("src")[1].equals(ce.getPath())){
							sameCounter++;
							classNames.add(sewi.getKey_path().split("src")[1]);
						}
					}
				}
			}
		}
		System.out.println("Same:" +sameCounter);
		
		Double sonarSimilarityPerCent =  ((double) sameCounter / sonarPart.size())*100;
		Double squoreSimilarityPerCent = ((double) sameCounter / squoresPart.size())*100;
		Double castSimilarityPerCent = ((double) sameCounter / castsPart.size())*100;
		
		System.out.println("Sonar Similarity Per Cent: "+f.format(sonarSimilarityPerCent));
		System.out.println("Squore Similarity Per Cent: "+f.format(squoreSimilarityPerCent));
		System.out.println("Cast Similarity Per Cent: "+f.format(castSimilarityPerCent));
	
		return classNames;
	}

	public static List<String> compareSonarWithSquore(List<SonarEntryWithIssues> sonarPart,
			List<SquoreEntryWithIssues> squoresPart){
		System.out.println("Sonar with Squore");
		List<String> classNames = new ArrayList<>();
		int sameCounter = 0;

		//Find sonars
		for(SonarEntryWithIssues sewi : sonarPart){
			for(SquoreEntryWithIssues sqewi : squoresPart){
				String[] sonarClass = sewi.getKey_path().split("src");
				String[] squoreClass = sqewi.getPath().split("src");

				//Similarities with squore
				if(sonarClass[sonarClass.length-1].equals(squoreClass[squoreClass.length-1])){
					sameCounter++;
					classNames.add(sonarClass[sonarClass.length-1]);
				}
			}
		}
		System.out.println("Same:" +sameCounter);	
		
		Double sonarSimilarityPerCent = ((double) sameCounter / sonarPart.size()) * 100;
		Double squoreSimilarityPerCent = ((double) sameCounter / squoresPart.size()) * 100;
		
		System.out.println("Sonar Similarity Per Cent: "+f.format(sonarSimilarityPerCent));
		System.out.println("Squore Similarity Per Cent: "+f.format(squoreSimilarityPerCent));
	
		return classNames;
	}

	public static List<String> compareSonarWithCast(List<SonarEntryWithIssues> sonarPart,
			List<CastEntry> castsPart){
		System.out.println("Sonar with Cast");
		List<String> classNames = new ArrayList<>();
		int sameCounter = 0;

		//Find sonars
		for(SonarEntryWithIssues sewi : sonarPart){
			for(CastEntry ce : castsPart){
				//Similarities with cast
				if(sewi.getKey_path().split("src")[1].equals(ce.getPath())){
					sameCounter++;
					classNames.add(sewi.getKey_path().split("src")[1]);
				}
			}
		}
		System.out.println("Same:" +sameCounter);
		
		Double sonarSimilarityPerCent = ((double) sameCounter / sonarPart.size()) * 100;
		Double castSimilarityPerCent = ((double) sameCounter / castsPart.size()) * 100;
		
		System.out.println("Sonar Similarity Per Cent: "+f.format(sonarSimilarityPerCent));
		System.out.println("Cast Similarity Per Cent: "+f.format(castSimilarityPerCent));
		
		return classNames;
	}
	
	public static List<String> compareSquoreWithCast(List<SquoreEntryWithIssues> squoresPart,
			List<CastEntry> castsPart){
		System.out.println("Squore with Cast");
		List<String> classNames = new ArrayList<>();
		int sameCounter = 0;
		
		for(SquoreEntryWithIssues sqewi : squoresPart){
			String[] squoreClass = sqewi.getPath().split("src");
			
			for(CastEntry ce : castsPart){
				
				if(squoreClass[squoreClass.length-1].equals(ce.getPath())){
					sameCounter++;
					classNames.add(squoreClass[squoreClass.length-1]);
				}			
			}
		}
		
		System.out.println("Same:" +sameCounter);
		
		Double castSimilarityPerCent = ((double) sameCounter / castsPart.size()) * 100;
		Double squoreSimilarityPerCent = ((double) sameCounter / squoresPart.size()) * 100;
		
		System.out.println("Cast Similarity Per Cent: "+f.format(castSimilarityPerCent));
		System.out.println("Squore Similarity Per Cent: "+f.format(squoreSimilarityPerCent));
	
		return classNames;
	}
}
