package com.weareone.findlost.entities;

public class RowItem {
    private String key;
    private String value;

    public RowItem(String key, String value) {
        super();
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
