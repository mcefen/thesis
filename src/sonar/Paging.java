package sonar;

public class Paging
{
    private String total;

    private String pageSize;

    private String pageIndex;

    public String getTotal ()
    {
        return total;
    }

    public void setTotal (String total)
    {
        this.total = total;
    }

    public String getPageSize ()
    {
        return pageSize;
    }

    public void setPageSize (String pageSize)
    {
        this.pageSize = pageSize;
    }

    public String getPageIndex ()
    {
        return pageIndex;
    }

    public void setPageIndex (String pageIndex)
    {
        this.pageIndex = pageIndex;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [total = "+total+", pageSize = "+pageSize+", pageIndex = "+pageIndex+"]";
    }
}