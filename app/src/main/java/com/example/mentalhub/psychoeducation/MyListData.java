package com.example.mentalhub.psychoeducation;

public class MyListData{

    private String description;
    private int imgId;
    private String url;

    public MyListData(String description, int imgId, String url) {
        this.description = description;
        this.imgId = imgId;
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
