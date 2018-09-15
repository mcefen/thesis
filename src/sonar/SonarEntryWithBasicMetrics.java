package sonar;

public class SonarEntryWithBasicMetrics {

	private Long ncloc;
	private Double fileComplexity;
	private Double complexity;
	private Integer blockingIssues;
	private Integer criticalIssues;
	private Integer majorIssues;
	private Integer minorIssues;
	private Integer totalIssues;
	private Double sqaleIndex;
	private Double sqaleDebtRatio;
	
	public SonarEntryWithBasicMetrics(){
		super();
	}
	
	public void populateEntryAttributes(Measures[] measures){
		
		for(int i=0;i<measures.length;i++){
			populateAttribute(measures[i]);
		}
		calculateTotalIssues();
	}
	
	public void populateAttribute(Measures measure){
		switch(measure.getMetric()){
		case "ncloc": 
			ncloc = Long.parseLong(measure.getValue());
			break;
		case "complexity": 
			complexity = Double.parseDouble(measure.getValue());
			break;
		case "sqale_debt_ratio": 
			sqaleDebtRatio = Double.parseDouble(measure.getValue());
			break;
		case "minor_violations": 
			minorIssues = Integer.parseInt(measure.getValue());
			break;
		case "blocker_violations": 
			blockingIssues = Integer.parseInt(measure.getValue());
			break;
		case "critical_violations": 
			criticalIssues = Integer.parseInt(measure.getValue());
			break;
		case "sqale_index" : 
			sqaleIndex = Double.parseDouble(measure.getValue());
			break;
		case "file_complexity" : 
			fileComplexity = Double.parseDouble(measure.getValue());
			break;
		case "major_violations" : 
			majorIssues = Integer.parseInt(measure.getValue());
			break;
		default: break;
		}
	}
	
	public void calculateTotalIssues(){
		totalIssues = blockingIssues + criticalIssues + majorIssues + minorIssues;
	}

	public Long getNcloc() {
		return ncloc;
	}

	public void setNcloc(Long ncloc) {
		this.ncloc = ncloc;
	}

	public Double getFileComplexity() {
		return fileComplexity;
	}

	public void setFileComplexity(Double fileComplexity) {
		this.fileComplexity = fileComplexity;
	}

	public Double getComplexity() {
		return complexity;
	}

	public void setComplexity(Double complexity) {
		this.complexity = complexity;
	}

	public Integer getBlockingIssues() {
		return blockingIssues;
	}

	public void setBlockingIssues(Integer blockingIssues) {
		this.blockingIssues = blockingIssues;
	}

	public Integer getCriticalIssues() {
		return criticalIssues;
	}

	public void setCriticalIssues(Integer criticalIssues) {
		this.criticalIssues = criticalIssues;
	}

	public Integer getMajorIssues() {
		return majorIssues;
	}

	public void setMajorIssues(Integer majorIssues) {
		this.majorIssues = majorIssues;
	}

	public Integer getMinorIssues() {
		return minorIssues;
	}

	public void setMinorIssues(Integer minorIssues) {
		this.minorIssues = minorIssues;
	}

	public Integer getTotalIssues() {
		return totalIssues;
	}

	public void setTotalIssues(Integer totalIssues) {
		this.totalIssues = totalIssues;
	}

	public Double getSqaleIndex() {
		return sqaleIndex;
	}

	public void setSqaleIndex(Double sqaleIndex) {
		this.sqaleIndex = sqaleIndex;
	}

	public Double getSqaleDebtRatio() {
		return sqaleDebtRatio;
	}

	public void setSqaleDebtRatio(Double sqaleDebtRatio) {
		this.sqaleDebtRatio = sqaleDebtRatio;
	}

	
	
}
