package xmltest;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sonar.MainPageEntry;

public class XmlWriter {
	
	public XmlWriter(){
		super();
	}
	
	/** Create an XML file for each list of Sonar Entries
	 *  @param  projectName  The name of our project 
	 *  @param  entry        The MainPageEntry for the sonar project*/
	public void writeSonarsToXML(String projectName, MainPageEntry entry){
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("project");
			doc.appendChild(rootElement);
			
			//Total elements in the project
			Element totalFiles = doc.createElement("total");
			totalFiles.appendChild(doc.createTextNode(entry.getPaging().getTotal()));
			rootElement.appendChild(totalFiles);
			
			for(int i = 0; i< entry.getComponents().length;i++){

				if((entry.getComponents()[i]!=null) && (entry.getComponents()[i].getId()!=null)){
					// file element
					Element staff = doc.createElement("File");
					rootElement.appendChild(staff);
					
					Element id = doc.createElement("id");
					id.appendChild(doc.createTextNode(entry.getComponents()[i].getId()));
					staff.appendChild(id);
					
					//Attr attr = doc.createAttribute("id");
					//attr.setValue(entry.getComponents()[i].getId());
					//staff.setAttributeNode(attr);


					// key elements
					Element key = doc.createElement("key");
					key.appendChild(doc.createTextNode(entry.getComponents()[i].getKey()));
					staff.appendChild(key);

					// path elements
					Element path = doc.createElement("path");
					path.appendChild(doc.createTextNode(entry.getComponents()[i].getPath()));
					staff.appendChild(path);
					
					// key path elements
					Element keyPath = doc.createElement("key_path");
					keyPath.appendChild(doc.createTextNode(entry.getComponents()[i].getKey().substring(11)));
					staff.appendChild(keyPath);

					// language elements
					Element language = doc.createElement("language");
					language.appendChild(doc.createTextNode(entry.getComponents()[i].getLanguage()));
					staff.appendChild(language);
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

}
