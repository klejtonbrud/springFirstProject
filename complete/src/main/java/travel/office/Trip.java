package travel.office;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Trip
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String dest;
    private String time;

    public String getDest()
    {
        return dest;
    }

    public void setDest(String dest)
    {
        this.dest = dest;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }
}