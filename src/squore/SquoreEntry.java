package squore;

public class SquoreEntry {

	String rating;
	String fileName;
	String technicalDept;
	Double canonicalTechnicalDept;
	String lineCount;
	String violationsDensity;
	String blockerIssues;
	String criticalIssues;
	String averageCyclomaticComplexity;
	String cyclomaticComplexity;
	String path;
	
	public SquoreEntry(){
		super();
	}
	
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getTechnicalDept() {
		return technicalDept;
	}
	public void setTechnicalDept(String technicalDept) {
		this.technicalDept = technicalDept;
	}
	public Double getCanonicalTechnicalDept() {
		return canonicalTechnicalDept;
	}
	public void setCanonicalTechnicalDept(Double canonicalTechnicalDept) {
		this.canonicalTechnicalDept = canonicalTechnicalDept;
	}
	public String getLineCount() {
		return lineCount;
	}
	public void setLineCount(String lineCount) {
		this.lineCount = lineCount;
	}
	public String getViolationsDensity() {
		return violationsDensity;
	}
	public void setViolationsDensity(String violationsDensity) {
		this.violationsDensity = violationsDensity;
	}
	public String getBlockerIssues() {
		return blockerIssues;
	}
	public void setBlockerIssues(String blockerIssues) {
		this.blockerIssues = blockerIssues;
	}
	public String getCriticalIssues() {
		return criticalIssues;
	}
	public void setCriticalIssues(String criticalIssues) {
		this.criticalIssues = criticalIssues;
	}
	public String getAverageCyclomaticComplexity() {
		return averageCyclomaticComplexity;
	}
	public void setAverageCyclomaticComplexity(String averageCyclomaticComplexity) {
		this.averageCyclomaticComplexity = averageCyclomaticComplexity;
	}
	public String getCyclomaticComplexity() {
		return cyclomaticComplexity;
	}
	public void setCyclomaticComplexity(String cyclomaticComplexity) {
		this.cyclomaticComplexity = cyclomaticComplexity;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
	
	
}
