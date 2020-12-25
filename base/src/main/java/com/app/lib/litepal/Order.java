package com.app.lib.litepal;

public enum Order{
    ASC(" asc"),
    DESC(" desc");
    private String value;

    Order(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
