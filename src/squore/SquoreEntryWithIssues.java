package squore;

public class SquoreEntryWithIssues {
	
	private String path;
	private String blockerIssues;
	private String criticalIssues;
	private String majorIssues;
	private String totalIssues;
	private String violationDensity;
	private String canonicalDebpt;
	private String technicalDebt;
	
	public SquoreEntryWithIssues(){
		super();
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
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
	public String getMajorIssues() {
		return majorIssues;
	}
	public void setMajorIssues(String majorIssues) {
		this.majorIssues = majorIssues;
	}
	public String getTotalIssues() {
		return totalIssues;
	}
	public void setTotalIssues(String totalIssues) {
		this.totalIssues = totalIssues;
	}
	
	
	public String getViolationDensity() {
		return violationDensity;
	}

	public void setViolationDensity(String violationDensity) {
		this.violationDensity = violationDensity;
	}

	public void calculateTotal(){
		Integer blocker = Integer.valueOf(blockerIssues);
		Integer critical = Integer.valueOf(criticalIssues);
		Integer major = Integer.valueOf(majorIssues);
		
		Integer total = blocker + critical + major;
		
		totalIssues = ""+total;
	}

	public String getCanonicalDebpt() {
		return canonicalDebpt;
	}

	public void setCanonicalDebpt(String canonicalDebpt) {
		this.canonicalDebpt = canonicalDebpt;
	}

	public String getTechnicalDebt() {
		return technicalDebt;
	}

	public void setTechnicalDebt(String technicalDebt) {
		this.technicalDebt = technicalDebt;
	}
	
	
	
	

}
