package sonar;

public class ProjectSonarQubeEntry
{
    private String[] tags;

    private String id;

    private String organization;

    private String visibility;

    private String description;

    private String name;

    private String qualifier;

    private String key;

    public String[] getTags ()
    {
        return tags;
    }

    public void setTags (String[] tags)
    {
        this.tags = tags;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getOrganization ()
    {
        return organization;
    }

    public void setOrganization (String organization)
    {
        this.organization = organization;
    }

    public String getVisibility ()
    {
        return visibility;
    }

    public void setVisibility (String visibility)
    {
        this.visibility = visibility;
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

    public String getKey ()
    {
        return key;
    }

    public void setKey (String key)
    {
        this.key = key;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [tags = "+tags+", id = "+id+", organization = "+organization+", visibility = "+visibility+", description = "+description+", name = "+name+", qualifier = "+qualifier+", key = "+key+"]";
    }
}