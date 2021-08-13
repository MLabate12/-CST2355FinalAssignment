package com.example.cst2355finalassignment;

import java.io.Serializable;

public class SearchResult implements Serializable {
    private String title;
    private String URL;
    private String sectionName;
    private long id;

    public static final long serialVersionUID = 1L;

    public SearchResult(long id, String title, String URL, String sectionName) {
        this.title = title;
        this.URL = URL;
        this.sectionName = sectionName;
        this.id = id;
    }

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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public void setId(long id) { this.id = id; }

    public long getId() { return id; }

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
