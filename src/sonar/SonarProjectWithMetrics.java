package sonar;

public class SonarProjectWithMetrics {

	private String id;

    private String description;

    private String name;

    private String qualifier;

    private String[] measures;

    private String key;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getQualifier ()
    {
        return qualifier;
    }

    public void setQualifier (String qualifier)
    {
        this.qualifier = qualifier;
    }

    public String[] getMeasures ()
    {
        return measures;
    }

    public void setMeasures (String[] measures)
    {
        this.measures = measures;
    }

    public String getKey ()
    {
        return key;
    }

    public void setKey (String key)
    {
        this.key = key;
    }

   
	
}
