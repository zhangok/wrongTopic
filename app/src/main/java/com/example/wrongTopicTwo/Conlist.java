package com.example.wrongTopicTwo;

/**
 * Created by 15601_000 on 2019/1/29.
 * PrintTitle
 */

public class Conlist {
    private Boolean checBox;
    private String number;
    private String label;
    private String content;

    public Conlist(String number, String label, String content,  Boolean checBox){
        this.number = number;
        this.label = label;
        this.content=content;
        this.checBox=checBox;
    }
    public String getNum(){
        return number;
    }
    public String getLabel(){
        return label;
    }
    public String getContent(){
        return content;
    }
    public Boolean getChecBox(){
        return checBox;
    }
    public void setChecBox(Boolean chec){checBox=chec;}
}
