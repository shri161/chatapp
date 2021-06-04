package com.example.whatsapp;

public class users {
    public String name;
    public String image;
    public String status;
    public users()
    {

    }
    public users(String name,String status,String image)
    {
        this.image=image;
        this.status=status;
        this.name=name;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getStatus()
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status=status;
    }

    public String getImage()
    {
        return image;
    }
    public void setImage(String image)
    {
        this.image=image;
    }

}
