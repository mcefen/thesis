package common;

public class ProjectEntry {

	private String projectName;
	private String path;
	private String debtSonar;
	private String totalIssuesSonar;
	private String canonicalDebtSonar;
	private String debtSquore;
	private String totalIssuesSquore;
	private String canonicalDebtSquore;
	private String debtCast;
	private String totalIssuesCast;
	private String canonicalDebtCast;
	
	
	public ProjectEntry(){
		super();
	}


	public String getProjectName() {
		return projectName;
	}


	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public String getDebtSonar() {
		return debtSonar;
	}


	public void setDebtSonar(String debtSonar) {
		this.debtSonar = debtSonar;
	}


	public String getCanonicalDebtSonar() {
		return canonicalDebtSonar;
	}


	public void setCanonicalDebtSonar(String canonicalDebtSonar) {
		this.canonicalDebtSonar = canonicalDebtSonar;
	}


	public String getDebtSquore() {
		return debtSquore;
	}


	public void setDebtSquore(String debtSquore) {
		this.debtSquore = debtSquore;
	}


	public String getCanonicalDebtSquore() {
		return canonicalDebtSquore;
	}


	public void setCanonicalDebtSquore(String canonicalDebtSquore) {
		this.canonicalDebtSquore = canonicalDebtSquore;
	}


	public String getDebtCast() {
		return debtCast;
	}


	public void setDebtCast(String debtCast) {
		this.debtCast = debtCast;
	}


	public String getCanonicalDebtCast() {
		return canonicalDebtCast;
	}


	public void setCanonicalDebtCast(String canonicalDebtCast) {
		this.canonicalDebtCast = canonicalDebtCast;
	}


	public String getTotalIssuesSonar() {
		return totalIssuesSonar;
	}


	public void setTotalIssuesSonar(String totalIssuesSonar) {
		this.totalIssuesSonar = totalIssuesSonar;
	}


	public String getTotalIssuesSquore() {
		return totalIssuesSquore;
	}


	public void setTotalIssuesSquore(String totalIssuesSquore) {
		this.totalIssuesSquore = totalIssuesSquore;
	}


	public String getTotalIssuesCast() {
		return totalIssuesCast;
	}


	public void setTotalIssuesCast(String totalIssuesCast) {
		this.totalIssuesCast = totalIssuesCast;
	}
	
	
	
}
