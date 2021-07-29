package com.example.cst2355finalassignment;

import java.io.Serializable;

public class SearchResult implements Serializable {
    private String title;
    private String URL;
    private String sectionName;

    public static final long serialVersionUID = 1L;

    public SearchResult(String title, String URL, String sectionName) {
        this.title = title;
        this.URL = URL;
        this.sectionName = sectionName;
    }

    public SearchResult(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = title;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String picture) {
        this.URL = URL;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String songs) {
        this.sectionName = sectionName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SearchResult)
            return super.equals(obj) || getTitle().equals(((SearchResult) obj).getTitle());

        return false;
    }

    @Override
    public String toString(){
        return getTitle();
    }

}
