package xmltest;

import java.util.List;

public class SonarQubeEntry {

	private String id;
	private String key;
	private String path;
	private String language;
	
	private List<SonarQubeEntry> entries;
	
	public SonarQubeEntry(){
		
	}
	
	public SonarQubeEntry(String id, String key, String path, String language){
		this.id = id;
		this.key =key;
		this.path = path;
		this.language = language;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<SonarQubeEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<SonarQubeEntry> entries) {
		this.entries = entries;
	}
	
	
	
}
