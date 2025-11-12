package model;

import java.sql.Date;

public class Rider
{
    private int rider_id;
    private String rider_name;
    private Date hire_date;
    private String contact_no;

    public Rider(int rider_id, String rider_name, Date hire_date, String contact_no)
                {
                    this.rider_id = rider_id;
                    this.rider_name = rider_name;
                    this.hire_date = hire_date;
                    this.contact_no = contact_no;
                }

    public Rider() {}

    public int getRiderID()
    {
        return rider_id;
    }

    public String getRiderName()
    {
        return rider_name;
    }

    public Date getHireDate()
    {
        return hire_date;
    }

    public String getContactNo()
    {
        return contact_no;
    }

    public void setRiderID(int rider_id)
    {
        this.rider_id = rider_id;
    }

    public void setRiderName(String rider_name)
    {
        this.rider_name = rider_name;
    }

    public void setHireDate(Date hire_date)
    {
        this.hire_date = hire_date;
    }

    public void setContactNo(String contact_no)
    {
        this.contact_no = contact_no;
    }
}