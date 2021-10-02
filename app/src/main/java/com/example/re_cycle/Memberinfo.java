package com.example.re_cycle;

public class Memberinfo
{
    private String name;
    private String birthday;
    private String phone;

    public Memberinfo(String name, String birthday, String phonenumber)
    {
        this.name = name;
        this.birthday = birthday;
        this.phone = phonenumber;
    }

    public String getname()
    {
        return this.name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getBirthday()
    {
        return this.birthday;
    }
    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }

    public String getPhonenumber()
    {
        return this.phone;
    }
    public void setPhonenumber(String phonenumber)
    {
        this.phone = phonenumber;
    }

}
