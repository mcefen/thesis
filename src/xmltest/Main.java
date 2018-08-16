package xmltest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

	private final static String USER_AGENT = "Mozilla/5.0";
	
	public static void main(String argv[]) throws Exception {
		
		String[] projects = {"mina", "deltaspike", "opennlp", "maven", "wss4j", "pdfbox", "fop", "cayenne", "jclouds", "openjpa"};
		
		for (int i = 0; i < projects.length;i++){
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
				String getResponse = sendGet(projects[i],pageNum, pageElements);
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
						
			for(MainPageEntry sEntry : projectObjects){
				int totalCounter = 0;
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
			
			//Write it to XML as one object
			writeToXML(projects[i], mainPageEntry);			
		}	  
	}
	
	private static String sendGet(String component, int page, int pageSize) throws Exception {

		String url = "http://se.uom.gr:9907/api/components/tree?component="+component+"&p="+page+"&ps="+pageSize+"&qualifiers=FIL";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

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
	
	private static void writeToXML(String projectName, MainPageEntry entry){
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("project");
			doc.appendChild(rootElement);
			
			for(int i = 0; i< entry.getComponents().length;i++){

				if((entry.getComponents()[i]!=null) && (entry.getComponents()[i].getId()!=null)){
					// staff elements
					Element staff = doc.createElement("File");
					rootElement.appendChild(staff);

					// set attribute to staff element
					Attr attr = doc.createAttribute("id");
					attr.setValue(entry.getComponents()[i].getId());
					staff.setAttributeNode(attr);


					// firstname elements
					Element firstname = doc.createElement("key");
					firstname.appendChild(doc.createTextNode(entry.getComponents()[i].getKey()));
					staff.appendChild(firstname);

					// lastname elements
					Element lastname = doc.createElement("path");
					lastname.appendChild(doc.createTextNode(entry.getComponents()[i].getPath()));
					staff.appendChild(lastname);

					// nickname elements
					Element nickname = doc.createElement("language");
					nickname.appendChild(doc.createTextNode(entry.getComponents()[i].getLanguage()));
					staff.appendChild(nickname);
				}				
			}
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("./"+projectName+".xml"));
			
			transformer.transform(source, result);

			System.out.println("File saved!");

		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
	}	
	
	private static MainPageEntry parse(String responseToParse) throws JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		try {

			// Convert JSON string from file to Object
			MainPageEntry entry = mapper.readValue(responseToParse, MainPageEntry.class);
			
			//Pretty print
			String prettyStaff1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(entry);
			System.out.println(prettyStaff1);
			
			return entry;
			
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			return new MainPageEntry();
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return new MainPageEntry();
		} 
	}
}
