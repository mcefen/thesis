package sonar;

/** Sonar API allows us to get max 500 per page
 * However we need to merge all these call results 
 * in one single object to write it into XML*/
public class MainPageEntry
{
	//Project
    private ProjectSonarQubeEntry baseComponent;

    //Files in project
    private FileSonarQubeEntry[] components;

    //Shows total files - need for counter
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
			
			

