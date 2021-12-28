package com.example.newsapp.model;

public class FilterData {

    public String name;
    public String code;
    public boolean selected;

    public FilterData(String name, String code, boolean selected) {
        this.name = name;
        this.code = code;
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
