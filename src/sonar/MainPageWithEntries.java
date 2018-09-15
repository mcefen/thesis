package sonar;

public class MainPageWithEntries {
	private SonarProjectWithMetrics baseComponent;

    private SonarFileWithMeasures[] components;

    private Paging paging;

   

    public SonarProjectWithMetrics getBaseComponent() {
		return baseComponent;
	}

	public void setBaseComponent(SonarProjectWithMetrics baseComponent) {
		this.baseComponent = baseComponent;
	}

	public SonarFileWithMeasures[] getComponents() {
		return components;
	}

	public void setComponents(SonarFileWithMeasures[] components) {
		this.components = components;
	}

	public Paging getPaging ()
    {
        return paging;
    }

    public void setPaging (Paging paging)
    {
        this.paging = paging;
    }

}
