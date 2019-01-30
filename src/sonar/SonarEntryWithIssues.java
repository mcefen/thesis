package sonar;

public class SonarEntryWithIssues {
	
	private String key;
	private String path;
	private String key_path;
	private String blocker;
	private String critical;
	private String major;
	private String totalIssues;
	private String squaleIndex;
	
	private String srcPath;
	
	
	
	public SonarEntryWithIssues() {
		super();
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
	public String getKey_path() {
		return key_path;
	}
	public void setKey_path(String key_path) {
		this.key_path = key_path;
	}
	public String getBlocker() {
		return blocker;
	}
	public void setBlocker(String blocker) {
		this.blocker = blocker;
	}
	public String getCritical() {
		return critical;
	}
	public void setCritical(String critical) {
		this.critical = critical;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getTotalIssues() {
		return totalIssues;
	}
	public void setTotalIssues(String totalIssues) {
		this.totalIssues = totalIssues;
	}
	
	
	
	public String getSqualeIndex() {
		return squaleIndex;
	}
	public void setSqualeIndex(String squaleIndex) {
		this.squaleIndex = squaleIndex;
	}
	public void calculateTotalIssues(){
		Integer blockerIssues = Integer.valueOf(blocker);
		Integer criticalIssues = Integer.valueOf(critical);
		Integer majorIssues = Integer.valueOf(major);
		
		Integer totalIssuesInt = blockerIssues+criticalIssues+majorIssues;
		
		totalIssues = totalIssuesInt.toString();
	}
	public String getSrcPath() {
		return srcPath;
	}
	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}
	
	

}
