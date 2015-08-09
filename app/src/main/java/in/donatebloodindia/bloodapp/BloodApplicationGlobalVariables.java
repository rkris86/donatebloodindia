package in.donatebloodindia.bloodapp;

/**
 * Created by krishnagurram on 26/07/15.
 */

import android.app.Application;

public class BloodApplicationGlobalVariables extends Application
{
    private String sessionid;
    private String name;
    private String password;
    private String mobile;
    private String city;
    private String bloodgroup;
    private String location;
    private int age;

    public String getsessionid()
    {
        return this.sessionid;
    }
    public String getname()
    {
        return this.name;
    }
    public String getpassword()
    {
        return this.password;
    }
    public String getmobile()
    {
        return this.mobile;
    }
    public String getbloodgroup()
    {
        return this.bloodgroup;
    }
    public int getage()
    {
        return this.age;
    }
    public String getlocation()
    {
        return this.location;
    }


    public void setsessionid(String id)
    {
        this.sessionid=id;
    }
    public void setpassword(String id)
    {
        this.password=id;
    }
    public void setname(String id)
    {
        this.name=id;
    }
    public void setcity(String id)
    {
        this.city=id;
    }
    public void setmobile(String id)
    {
        this.mobile=id;
    }
    public void setbloodgroup(String id)
    {
        this.bloodgroup=id;
    }
    public void setage(int ag)
    {
        this.age = ag;
    }
    public void setlocation(String loc)
    {
        this.location = loc;
    }

}
