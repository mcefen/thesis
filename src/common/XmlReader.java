package common;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sonar.SonarEntryWithIssues;
import squore.SquoreEntryWithIssues;

public class XmlReader {
	
	public static final String TOTAL_ISSUES = "Total Issues";
	public static final String METRICS = "Metrics";
	
	

	public void ReadAllSonarXml(String projectName){
		List<SonarEntryWithIssues> sonarEntries = new ArrayList<>();

		try {
			org.w3c.dom.Document doc = getNodelistFromSonarXmlFile(projectName);
						
			NodeList nList = doc.getElementsByTagName("File");
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				
				org.w3c.dom.Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					SonarEntryWithIssues sonarEntry = createSonarNode(doc, nNode, temp);
					sonarEntries.add(sonarEntry);
													
				}
			}			
			
			XmlWriter writer = new XmlWriter();
			writer.writeSonarsWithIssuesToXML(projectName, sonarEntries);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<SonarEntryWithIssues> ReadAllSonarXmlForInfo(String projectName){
		List<SonarEntryWithIssues> sonarEntries = new ArrayList<>();

		try {
			org.w3c.dom.Document doc = getNodelistFromSonarXmlFile(projectName);
						
			NodeList nList = doc.getElementsByTagName("File");
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				
				org.w3c.dom.Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					SonarEntryWithIssues sonarEntry = createSonarNode(doc, nNode, temp);
					
					String[] clearPath = sonarEntry.getKey_path().split("src");
					sonarEntry.setSrcPath(clearPath[1]);
					
					sonarEntries.add(sonarEntry);
													
				}
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sonarEntries;
	}
	
	public org.w3c.dom.Document getNodelistFromSonarXmlFile(String projectName) throws ParserConfigurationException, SAXException, IOException{
		File fXmlFile = new File("sonar_baseMetrics_"+projectName+".xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();

		
		
		return doc;
	}
	
	public SonarEntryWithIssues createSonarNode(org.w3c.dom.Document doc, 
			org.w3c.dom.Node nNode,
			int temp){
		SonarEntryWithIssues sonarEntry = new SonarEntryWithIssues();
		Element eElement = (Element) nNode;
		
		if(doc == null){
			System.out.println("I am null");
		}

		NodeList key = doc.getElementsByTagName("Key");
		org.w3c.dom.Node keyNode = key.item(temp);
		Element keyElement = (Element) keyNode;
		sonarEntry.setKey(keyElement.getTextContent());

		NodeList path = doc.getElementsByTagName("Path");
		org.w3c.dom.Node pathNode = path.item(temp);
		Element pathElement = (Element) pathNode;
		sonarEntry.setPath(pathElement.getTextContent());

		NodeList keyPath = doc.getElementsByTagName("Key_Path");
		org.w3c.dom.Node keyPathNode = keyPath.item(temp);
		Element keyPathElement = (Element) keyPathNode;
		sonarEntry.setKey_path(keyPathElement.getTextContent());

		NodeList blocker = doc.getElementsByTagName("Blocker_Issues");
		org.w3c.dom.Node blockerNode = blocker.item(temp);
		Element blockerElement = (Element) blockerNode;
		sonarEntry.setBlocker(blockerElement.getTextContent());

		NodeList critical = doc.getElementsByTagName("Critical_Issues");
		org.w3c.dom.Node criticalNode = critical.item(temp);
		Element criticalElement = (Element) criticalNode;
		sonarEntry.setCritical(criticalElement.getTextContent());
		
		NodeList major = doc.getElementsByTagName("Major_Issues");
		org.w3c.dom.Node majorNode = major.item(temp);
		Element majorElement = (Element) majorNode;
		sonarEntry.setMajor(majorElement.getTextContent());
		
		NodeList minor = doc.getElementsByTagName("Minor_Issues");
		org.w3c.dom.Node minorNode = minor.item(temp);
		Element minorElement = (Element) minorNode;
		sonarEntry.setMinor(minorElement.getTextContent());
		
		NodeList info = doc.getElementsByTagName("Info_Issues");
		org.w3c.dom.Node infoNode = info.item(temp);
		Element infoElement = (Element) infoNode;
		sonarEntry.setInfo(infoElement.getTextContent());
		
		NodeList squaleIndex = doc.getElementsByTagName("Sqale_Index");
		org.w3c.dom.Node squaleIndexNode = squaleIndex.item(temp);
		Element squaleIndexElement = (Element) squaleIndexNode;
		sonarEntry.setSqualeIndex(squaleIndexElement.getTextContent());
		
		return sonarEntry;
	}
	
	public List<SonarEntryWithIssues> getSonarXmlSortedList(String projectName, String sortType){
		List<SonarEntryWithIssues> sonarEntries = new ArrayList<>();

		try {
			org.w3c.dom.Document doc = getNodelistFromSonarXmlFile(projectName);
			
			NodeList nList = doc.getElementsByTagName("File");
			
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				
				org.w3c.dom.Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					SonarEntryWithIssues sonarEntry = createSonarNode(doc, nNode, temp);
					sonarEntry.calculateTotalIssues();
					
					if(sortType.equals(TOTAL_ISSUES)){
						sonarEntries = calculateSonarTotalIssues(sonarEntry, sonarEntries);
					} else {
						sonarEntries = calculateSonarSqualeIndex(sonarEntry, sonarEntries);
					}									
				}
			}
			
			sonarEntries = sortSonarList(sonarEntries, sortType);
			
			//printSonarList(sonarEntries);		
			
			return sonarEntries;
						
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	//Use in total issues calculation
	public List<SonarEntryWithIssues> calculateSonarTotalIssues(SonarEntryWithIssues sonarEntry, 
			List<SonarEntryWithIssues> sonarEntries){		
		
		sonarEntry.calculateTotalIssues();
		Integer totalIssues = Integer.valueOf(sonarEntry.getTotalIssues());
		
		if(totalIssues>0){
			sonarEntries.add(sonarEntry);
		}	
		
		return sonarEntries;
	}
	
	//Use in squale calculation
	public List<SonarEntryWithIssues> calculateSonarSqualeIndex(SonarEntryWithIssues sonarEntry,
			List<SonarEntryWithIssues> sonarEntries){
		
		//Double squale = Double.valueOf(sonarEntry.getSqualeIndex());
		//if(squale>0){
			sonarEntries.add(sonarEntry);
		//}
		
		return sonarEntries;
	}
	
	public List<SonarEntryWithIssues> sortSonarList(List<SonarEntryWithIssues> sonarEntries, String sortType){
		//Use in total issues sorting
		if(sortType.equals(TOTAL_ISSUES)){
			Collections.sort(sonarEntries, 
			        (o1, o2) -> Integer.valueOf(o2.getTotalIssues()).compareTo(Integer.valueOf(o1.getTotalIssues())));
					
		} else {
			Collections.sort(sonarEntries,
					(o1,o2)-> 
					Double.valueOf(o2.getSqualeIndex()).compareTo(Double.valueOf(o1.getSqualeIndex())));
		}

		return sonarEntries;		
	}
	
	public void printSonarList(List<SonarEntryWithIssues> sonarEntries){
		System.out.println("Sonars");
		for(SonarEntryWithIssues se : sonarEntries){
			String[] sonarClass = se.getKey_path().split("/");
			System.out.println("Name: " + sonarClass[sonarClass.length-1]);
			System.out.println("Squale Index: " + se.getSqualeIndex());
			System.out.println("Issues: " + se.getTotalIssues());
		}
	}
	
	public List<SquoreEntryWithIssues> ReadSquoreXml(String projectName){
	//public void ReadSquoreXml(String projectName){
		List<SquoreEntryWithIssues> squoreEntries = new ArrayList<>();

		try {
			File fXmlFile = new File("squore_"+projectName+".xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("File");

			//System.out.println("----------------------------");
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				SquoreEntryWithIssues squoreEntry = new SquoreEntryWithIssues();
				org.w3c.dom.Node nNode = nList.item(temp);

				//System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					NodeList path = doc.getElementsByTagName("Path");
					org.w3c.dom.Node pathNode = path.item(temp);
					Element pathElement = (Element) pathNode;
					squoreEntry.setPath(pathElement.getTextContent());

					NodeList blocker = doc.getElementsByTagName("Blocker_Issues");
					org.w3c.dom.Node blockerNode = blocker.item(temp);
					Element blockerElement = (Element) blockerNode;
					squoreEntry.setBlockerIssues(blockerElement.getTextContent());

					NodeList critical = doc.getElementsByTagName("Critical_Issues");
					org.w3c.dom.Node criticalNode = critical.item(temp);
					Element criticalElement = (Element) criticalNode;
					squoreEntry.setCriticalIssues(criticalElement.getTextContent());
					
					NodeList major = doc.getElementsByTagName("Major_Issues");
					org.w3c.dom.Node majorNode = major.item(temp);
					Element majorElement = (Element) majorNode;
					squoreEntry.setMajorIssues(majorElement.getTextContent());
					
					NodeList violationsDensity = doc.getElementsByTagName("Violations_Density");
					org.w3c.dom.Node violationDensityNode = violationsDensity.item(temp);
					Element violationDensityElement = (Element) violationDensityNode;
					squoreEntry.setViolationDensity(violationDensityElement.getTextContent());
					
					NodeList canonicalDebpt = doc.getElementsByTagName("Canonical_Technical_Debt");
					org.w3c.dom.Node canonicalDebptNode = canonicalDebpt.item(temp);
					Element canonicalDebptElement = (Element) canonicalDebptNode;
					squoreEntry.setCanonicalDebpt(canonicalDebptElement.getTextContent());
					
					NodeList debpt = doc.getElementsByTagName("Technical_Debt");
					org.w3c.dom.Node debptNode = debpt.item(temp);
					Element debptElement = (Element) debptNode;
					squoreEntry.setTechnicalDebt(debptElement.getTextContent());
					
					//System.out.println("- " + squoreEntry.getViolationDensity().trim() + " -");
					//Use in tota issues calculations
					//*************************************//
					squoreEntry.calculateTotal();

					/*Integer totalIssues = Integer.valueOf(squoreEntry.getTotalIssues());
					
					if(totalIssues>0){
						squoreEntries.add(squoreEntry);
					}*/	
					
					//Use in violation density calculation
					//****************************************//
					
					/*Long violationDensity = (Long) NumberFormat.getNumberInstance(Locale.UK).parse(squoreEntry.getViolationDensity().trim());
					if(violationDensity>0){
						squoreEntry.setViolationDensity(""+violationDensity);
						squoreEntries.add(squoreEntry);
					}*/
					
					//Double canonicalDebptNum = NumberFormat.getNumberInstance(Locale.UK).parse(squoreEntry.getCanonicalDebpt().trim()).doubleValue();
					//if(canonicalDebptNum>0){
					//	squoreEntry.setCanonicalDebpt(""+canonicalDebptNum);
						squoreEntries.add(squoreEntry);
					//}
					
					//Use in critical issues calculation
					//**************************************//
					/*Integer criticalIssues = Integer.valueOf(squoreEntry.getCriticalIssues());
					if(criticalIssues > 0){
						squoreEntries.add(squoreEntry);
					}*/
				}
			}
			
			//Use in total issues sorting
			//************************************//
			//Collections.sort(squoreEntries, 
	        //        (o1, o2) -> Integer.valueOf(o2.getTotalIssues()).compareTo(Integer.valueOf(o1.getTotalIssues())));
			
			//Use in violation density sorting
			//*******************************//
			/*Collections.sort(squoreEntries, 
					(o1,o2)-> Float.valueOf(o2.getViolationDensity().trim()).compareTo(Float.valueOf(o1.getViolationDensity().trim())));
			*/
			
			Collections.sort(squoreEntries, 
					(o1,o2)-> Float.valueOf(o2.getCanonicalDebpt().trim()).compareTo(Float.valueOf(o1.getCanonicalDebpt().trim())));
			
			
			//Use in critical issues sorting
			//********************************//
			//Collections.sort(squoreEntries, 
			//        (o1, o2) -> Integer.valueOf(o2.getCriticalIssues()).compareTo(Integer.valueOf(o1.getCriticalIssues())));
					
			//System.out.println("Total Critical: "+squoreEntries.size());
			//System.out.println("Squores");
			//for(SquoreEntryWithIssues se : squoreEntries){
			//	String[] squoreClass = se.getPath().split("/");
			//	System.out.println("Name: " + squoreClass[squoreClass.length-1]);
			//	System.out.println("Density: " + se.getViolationDensity());
			//}
			
			return squoreEntries;
			//XmlWriter writer = new XmlWriter();
			//writer.writeSquoresWithIssuesToXML(projectName, squoreEntries);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	



}
