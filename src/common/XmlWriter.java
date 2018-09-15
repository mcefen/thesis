package common;

import java.io.File;
import java.util.List;

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
import sonar.MainPageWithEntries;
import sonar.SonarEntryWithBasicMetrics;
import squore.SquoreEntry;

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
			StreamResult result = new StreamResult(new File("./sonar_"+projectName+".xml"));

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}	
	
	/** Create an XML file for each list of Sonar Entries
	 *  @param  projectName  The name of our project 
	 *  @param  entry        The MainPageEntry for the sonar project*/
	public void writeSonarsWithMetricsToXML(String projectName, MainPageWithEntries entry){
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

					Element id = doc.createElement("Id");
					id.appendChild(doc.createTextNode(entry.getComponents()[i].getId()));
					staff.appendChild(id);

					// key elements
					Element key = doc.createElement("Key");
					key.appendChild(doc.createTextNode(entry.getComponents()[i].getKey()));
					staff.appendChild(key);

					// path elements
					Element path = doc.createElement("Path");
					path.appendChild(doc.createTextNode(entry.getComponents()[i].getPath()));
					staff.appendChild(path);

					// key path elements
					Element keyPath = doc.createElement("Key_Path");
					keyPath.appendChild(doc.createTextNode(entry.getComponents()[i].getKey().substring(11)));
					staff.appendChild(keyPath);

					// language elements
					Element language = doc.createElement("Language");
					language.appendChild(doc.createTextNode(entry.getComponents()[i].getLanguage()));
					staff.appendChild(language);
					
					SonarEntryWithBasicMetrics sEntry = new SonarEntryWithBasicMetrics();
					sEntry.populateEntryAttributes(entry.getComponents()[i].getMeasures());
					// ncloc elements
					Element ncloc = doc.createElement("Ncloc");
					ncloc.appendChild(doc.createTextNode(String.valueOf(sEntry.getNcloc())));
					staff.appendChild(ncloc);
					
					// file complexity elements
					Element file_complexity = doc.createElement("File_Complexity");
					file_complexity.appendChild(doc.createTextNode(String.valueOf(sEntry.getFileComplexity())));
					staff.appendChild(file_complexity);
					
					// complexity elements
					Element complexity = doc.createElement("Complexity");
					complexity.appendChild(doc.createTextNode(String.valueOf(sEntry.getComplexity())));
					staff.appendChild(complexity);
					
					// blocker elements
					Element blocker = doc.createElement("Blocker_Issues");
					blocker.appendChild(doc.createTextNode(String.valueOf(sEntry.getBlockingIssues())));
					staff.appendChild(blocker);
					
					// major elements
					Element critical = doc.createElement("Critical_Issues");
					critical.appendChild(doc.createTextNode(String.valueOf(sEntry.getCriticalIssues())));
					staff.appendChild(critical);
					
					// major elements
					Element major = doc.createElement("Major_Issues");
					major.appendChild(doc.createTextNode(String.valueOf(sEntry.getMajorIssues())));
					staff.appendChild(major);
					
					// minor elements
					Element minor = doc.createElement("Minor_Issues");
					minor.appendChild(doc.createTextNode(String.valueOf(sEntry.getMinorIssues())));
					staff.appendChild(minor);
					
					// total elements
					Element total = doc.createElement("Total_Issues");
					total.appendChild(doc.createTextNode(String.valueOf(sEntry.getTotalIssues())));
					staff.appendChild(total);
					
					// sqale_index elements
					Element sqale_index = doc.createElement("Sqale_Index");
					sqale_index.appendChild(doc.createTextNode(String.valueOf(sEntry.getSqaleIndex())));
					staff.appendChild(sqale_index);
					
					// sqale_debt_ratio elements
					Element sqale_debt_ratio = doc.createElement("Sqale_Debt_Ratio");
					sqale_debt_ratio.appendChild(doc.createTextNode(String.valueOf(sEntry.getSqaleDebtRatio())));
					staff.appendChild(sqale_debt_ratio);
				}				
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("./sonar_baseMetrics_"+projectName+".xml"));

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}	

	/** Create an XML file for each list of Squore Entries
	 *  @param  projectName  The name of our project 
	 *  @param  squores      List of squore entries*/
	public void writeSquoresToXML(String projectName, List<SquoreEntry> squores){
		try{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("project");
			doc.appendChild(rootElement);

			//Total elements in the project
			Element totalFiles = doc.createElement("total");
			totalFiles.appendChild(doc.createTextNode(String.valueOf(squores.size())));
			rootElement.appendChild(totalFiles);

			for(SquoreEntry sq : squores){
				Element staff = doc.createElement("File");
				rootElement.appendChild(staff);

				Element artefact = doc.createElement("Artefact");
				artefact.appendChild(doc.createTextNode(sq.getFileName()));
				staff.appendChild(artefact);
				
				Element rating = doc.createElement("Rating");
				rating.appendChild(doc.createTextNode(sq.getRating()));
				staff.appendChild(rating);
				
				Element techDebt = doc.createElement("Technical_Debt");
				techDebt.appendChild(doc.createTextNode(sq.getTechnicalDept()));
				staff.appendChild(techDebt);
				
				Element canonicalTechDebt = doc.createElement("Canonical_Technical_Debt");
				canonicalTechDebt.appendChild(doc.createTextNode(String.valueOf(sq.getCanonicalTechnicalDept())));
				staff.appendChild(canonicalTechDebt);
				
				Element lineCount = doc.createElement("Line_Count");
				lineCount.appendChild(doc.createTextNode(sq.getLineCount()));
				staff.appendChild(lineCount);
				
				Element violations = doc.createElement("Violations_Density");
				violations.appendChild(doc.createTextNode(sq.getViolationsDensity()));
				staff.appendChild(violations);
				
				Element blocker = doc.createElement("Blocker_Issues");
				blocker.appendChild(doc.createTextNode(sq.getBlockerIssues()));
				staff.appendChild(blocker);
				
				Element critical = doc.createElement("Critical_Issues");
				critical.appendChild(doc.createTextNode(sq.getCriticalIssues()));
				staff.appendChild(critical);
				
				Element major = doc.createElement("Major_Issues");
				major.appendChild(doc.createTextNode(sq.getMajorIssues()));
				staff.appendChild(major);
				
				Element minor = doc.createElement("Minor_Issues");
				minor.appendChild(doc.createTextNode(sq.getMinorIssues()));
				staff.appendChild(minor);
				
				Element total = doc.createElement("Total_Issues");
				total.appendChild(doc.createTextNode(sq.getTotalIssues()));
				staff.appendChild(total);
				
				Element avgCyclComplexity = doc.createElement("Average_Cyclomatic_Complexity");
				avgCyclComplexity.appendChild(doc.createTextNode(sq.getAverageCyclomaticComplexity()));
				staff.appendChild(avgCyclComplexity);
				
				Element cyclComplexity = doc.createElement("Cyclomatic_Complexity");
				cyclComplexity.appendChild(doc.createTextNode(sq.getCyclomaticComplexity()));
				staff.appendChild(cyclComplexity);
				
				//TODO: full path with file name at the end
				Element path = doc.createElement("Path");
				path.appendChild(doc.createTextNode(sq.getPath()+"/"+sq.getFileName()));
				staff.appendChild(path);
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("./squore_"+projectName+".xml"));

			transformer.transform(source, result);

			System.out.println("File saved!");


		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

}
