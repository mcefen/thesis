package common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import cast.CastEntry;
import sonar.SonarEntryWithIssues;
import squore.SquoreEntryWithIssues;

public class TxtWriter {

	private BufferedWriter writer = null;

	public void writeSonars(String projectName, List<SonarEntryWithIssues> sonars){
		try {
			
			File logFile = new File(projectName);

			// This will output the full path where the file will be written to...
			System.out.println(logFile.getCanonicalPath());

			writer = new BufferedWriter(new FileWriter(logFile));
			
			writer.write("Total Sonar Classes");
			writer.newLine();
			writer.newLine();
			for(SonarEntryWithIssues sewi : sonars){
				writer.write(sewi.getKey_path().split("src")[1]);
				writer.newLine();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			}
		}
	}
	
	public void writeSquores(String projectName, List<SquoreEntryWithIssues> squores){
		try {
			//create a temporary file
			File logFile = new File(projectName);

			// This will output the full path where the file will be written to...
			System.out.println(logFile.getCanonicalPath());

			writer = new BufferedWriter(new FileWriter(logFile));
			
			writer.write("Total Squore Classes");
			writer.newLine();
			writer.newLine();
			for(SquoreEntryWithIssues sewi : squores){
				writer.write(sewi.getPath().split("src")[1]);
				writer.newLine();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			}
		}
	}
	
	public void writeCasts(String projectName, List<CastEntry> casts){
		try {
			//create a temporary file
			File logFile = new File(projectName);

			// This will output the full path where the file will be written to...
			System.out.println(logFile.getCanonicalPath());

			writer = new BufferedWriter(new FileWriter(logFile));
			
			writer.write("Total Squore Classes");
			writer.newLine();
			writer.newLine();
			for(CastEntry sewi : casts){
				writer.write(sewi.getPath());
				writer.newLine();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			}
		}
	}
	
	public void writeClassNames(String projectName,
			String title,
			List<String> classNames){
		try {
			
			File logFile = new File(projectName);

			writer = new BufferedWriter(new FileWriter(logFile));
			
			writer.write(title);
			writer.newLine();
			writer.newLine();
			for(String sewi : classNames){
				writer.write(sewi);
				writer.newLine();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			}
		}
	}


}
