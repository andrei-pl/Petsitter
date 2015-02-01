package com.example.andrey.petsitter.Models;

/**
 * Created by Andrey on 28.1.2015 г..
 */
public class NavDrawerItem {
    private String title;
    private int icon;

    public NavDrawerItem(){}

    public NavDrawerItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public int getIcon(){
        return this.icon;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }
}
