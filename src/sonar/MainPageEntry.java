package sonar;

public class MainPageEntry
{
    private ProjectSonarQubeEntry baseComponent;

    private FileSonarQubeEntry[] components;

    private Paging paging;

    public ProjectSonarQubeEntry getBaseComponent ()
    {
        return baseComponent;
    }

    public void setBaseComponent (ProjectSonarQubeEntry baseComponent)
    {
        this.baseComponent = baseComponent;
    }

    public FileSonarQubeEntry[] getComponents ()
    {
        return components;
    }

    public void setComponents (FileSonarQubeEntry[] components)
    {
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

    @Override
    public String toString()
    {
        return "ClassPojo [baseComponent = "+baseComponent+", components = "+components+", paging = "+paging+"]";
    }
}
			
			

