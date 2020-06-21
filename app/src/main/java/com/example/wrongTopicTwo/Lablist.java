package com.example.wrongTopicTwo;

/**
 * Created by 15601_000 on 2019/1/6.
 * ChooseLabel
 */

public class Lablist {
    private String label;
    private Boolean checBox;

    public Lablist(String label, Boolean checBox){
        this.label = label;
        this.checBox=checBox;
    }
    public String getLabel(){
        return label;
    }
    public Boolean getChecBox(){
        return checBox;
    }
    public void setChecBox(Boolean chec){checBox=chec;}
}
