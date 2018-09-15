package sonar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/** Methods to handle sonar entries */
public class SonarEntriesProcessor {

	private final static String USER_AGENT = "Mozilla/5.0";
	private final static String SONAR_BASE_URL="http://se.uom.gr:9907/api/components/tree?component=";
	private final static String SONAR_BASE_METRICS_URL = "http://se.uom.gr:9907/api/measures/component_tree?metricKeys=complexity,sqale_index,sqale_debt_ratio,file_complexity,ncloc,blocker_violations,critical_violations,major_violations,minor_violations&component=";

	public SonarEntriesProcessor(){
		super();
	}

	/** Use sonar API to get the results for the current component 
	 *  We format the URL for our component
	 *  Get the response and merge it into a single string
	 *  @param  component  The name of the project used in URL 
	 *  @param  page       The current number of the page to ask 
	 *  @param  pageSize   How many entries should the requested page contains
	 *  @return response   The response of the get request as a single string*/
	public String sendGetRequest(String component, int page, int pageSize, String requestType) throws Exception {
		String url="";
		//Get only files with names
		if(requestType.equals("Basic")){
			url =SONAR_BASE_URL +component+"&p="+page+"&ps="+pageSize+"&qualifiers=FIL";
		}
		//Get files with some metrics to start with
		else if (requestType.equals("Starter Metrics")){
			url = SONAR_BASE_METRICS_URL + component+"&p="+page+"&ps="+pageSize+"&qualifiers=FIL";
		}
		 

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		con.getResponseCode();

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		return response.toString();
	}

	/** Get the JSON string and convert it to our MainPageEntry object for Sonar
	 * @param  responseToParse  The JSON string which contains our information
	 * @return entry            The converted JSON into a MainPageEntry object
	 * */
	public MainPageEntry parse(String responseToParse) throws JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		try {

			// Convert JSON string from file to Object
			MainPageEntry entry = mapper.readValue(responseToParse, MainPageEntry.class);

			//Pretty print
			mapper.writerWithDefaultPrettyPrinter().writeValueAsString(entry);

			return entry;

		} catch (JsonGenerationException e) {
			e.printStackTrace();
			return new MainPageEntry();
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return new MainPageEntry();
		} 
	}

	/** Get the JSON string and convert it to our MainPageEntry object for Sonar
	 * @param  responseToParse  The JSON string which contains our information
	 * @return entry            The converted JSON into a MainPageEntry object
	 * */
	public MainPageWithEntries parseWithMetrics(String responseToParse) throws JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		try {

			// Convert JSON string from file to Object
			MainPageWithEntries entry = mapper.readValue(responseToParse, MainPageWithEntries.class);

			//Pretty print
			mapper.writerWithDefaultPrettyPrinter().writeValueAsString(entry);

			return entry;

		} catch (JsonGenerationException e) {
			e.printStackTrace();
			return new MainPageWithEntries();
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return new MainPageWithEntries();
		} 
	}

	
	/** Format a single MainPageEntry 
	 *  Get all converted responses for a project and then merge them
	 *  into a single object
	 *  @param  project        Project name
	 *  @return mainPageEntry  All pages results under a single object*/
	public MainPageEntry getMainEntry(String project) throws Exception{
		int pageNum = 1;
		int pageElements = 500;
		boolean hasMore = true;

		List<MainPageEntry> projectObjects = new ArrayList<>();

		//TODO: list of objects
		//We should get a root object for each page we get
		//We add all of them into a list
		//At the end we keep only one root object and add every element of the 
		//others under this one object
		while (hasMore){
			String getResponse;

			getResponse = sendGetRequest(project,pageNum, pageElements,"Basic");
			MainPageEntry sQEntry = parse(getResponse);

			if(sQEntry.getComponents().length>0){
				projectObjects.add(sQEntry);
			} else {
				hasMore = false;
				break;
			}
			pageNum++;
		}

		//At this point we get all objects from each page
		//Merge them into one list
		MainPageEntry mainPageEntry = new MainPageEntry();
		mainPageEntry.setBaseComponent(projectObjects.get(0).getBaseComponent());
		mainPageEntry.setPaging(projectObjects.get(0).getPaging());

		mainPageEntry.setComponents(new FileSonarQubeEntry[Integer.parseInt(mainPageEntry.getPaging().getTotal())]);
		int totalCounter = 0;			
		for(MainPageEntry sEntry : projectObjects){

			for(int j=0;j<sEntry.getComponents().length;j++){
				if(sEntry.getComponents()[j]!=null){
					if(sEntry.getComponents()[j].getLanguage().equals("java")){
						sEntry.getComponents()[j].setKey(sEntry.getComponents()[j].getKey().replace(':', '/'));
						mainPageEntry.getComponents()[totalCounter]=sEntry.getComponents()[j];
						totalCounter++;
					}						
				}
			}

		}
		return mainPageEntry;
	}

	public MainPageWithEntries getMainPageWithMetrics(String project) throws Exception{
		int pageNum = 1;
		int pageElements = 500;
		boolean hasMore = true;

		List<MainPageWithEntries> projectObjects = new ArrayList<>();

		while (hasMore){
			String getResponse;

			getResponse = sendGetRequest(project,pageNum, pageElements,"Starter Metrics");
			MainPageWithEntries sQEntry = parseWithMetrics(getResponse);
			//System.out.println("Starter metrics: " + sQEntry.getComponents().length);
			if(sQEntry.getComponents().length>0){
				projectObjects.add(sQEntry);
			} else {
				hasMore = false;
				break;
			}
			pageNum++;
		}

		//At this point we get all objects from each page
		//Merge them into one list
		MainPageWithEntries mainPageEntry = new MainPageWithEntries();
		mainPageEntry.setBaseComponent(projectObjects.get(0).getBaseComponent());
		mainPageEntry.setPaging(projectObjects.get(0).getPaging());

		mainPageEntry.setComponents(new SonarFileWithMeasures[Integer.parseInt(mainPageEntry.getPaging().getTotal())]);
		int totalCounter = 0;			
		for(MainPageWithEntries sEntry : projectObjects){

			for(int j=0;j<sEntry.getComponents().length;j++){
				if(sEntry.getComponents()[j]!=null){
					if(sEntry.getComponents()[j].getLanguage().equals("java")){
						sEntry.getComponents()[j].setKey(sEntry.getComponents()[j].getKey().replace(':', '/'));
						mainPageEntry.getComponents()[totalCounter]=sEntry.getComponents()[j];
						totalCounter++;
					}						
				}
			}

		}
		return mainPageEntry;

	}


}
