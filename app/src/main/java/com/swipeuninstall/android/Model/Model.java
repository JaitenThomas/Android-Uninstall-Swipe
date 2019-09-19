package com.swipeuninstall.android.Model;

import android.graphics.drawable.Drawable;

public class Model
{

    public String title;
    public String versionNumber;
    public Drawable icon;
    public String name;

    public Long getTime()
    {
        return time;
    }

    public void setTime(Long time)
    {
        this.time = time;
    }

    public Long time;


    public Model(String title, Drawable icon, String versionNumber, String name, Long time)
    {
        this.title = title;
        this.icon = icon;
        this.versionNumber = versionNumber;
        this.name = name;
        this.time = time;

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersionNumber()
    {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber)
    {
        this.versionNumber = versionNumber;
    }


    public Drawable getIcon()
    {
        return icon;
    }

    public void setIcon(Drawable icon)
    {
        this.icon = icon;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

}
