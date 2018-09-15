package sonar;

public class SonarFileWithMeasures {
	private String id;

    private String name;

    private String path;

    private String qualifier;

    private Measures[] measures;

    private String language;

    private String key;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getPath ()
    {
        return path;
    }

    public void setPath (String path)
    {
        this.path = path;
    }

    public String getQualifier ()
    {
        return qualifier;
    }

    public void setQualifier (String qualifier)
    {
        this.qualifier = qualifier;
    }

    public Measures[] getMeasures ()
    {
        return measures;
    }

    public void setMeasures (Measures[] measures)
    {
        this.measures = measures;
    }

    public String getLanguage ()
    {
        return language;
    }

    public void setLanguage (String language)
    {
        this.language = language;
    }

    public String getKey ()
    {
        return key;
    }

    public void setKey (String key)
    {
        this.key = key;
    }

    
    
    public Integer getTotalIssues(String blocking, String critical, String major, String minor){
    	Integer totalIssues = 0;
    	
    	try{
    		totalIssues = Integer.parseInt(blocking)+
    				Integer.parseInt(critical)+
    				Integer.parseInt(major)+
    				Integer.parseInt(minor);
    	} catch (Exception ex){
    		totalIssues = 0;
    	}
    	
    	return totalIssues;
    }
}
