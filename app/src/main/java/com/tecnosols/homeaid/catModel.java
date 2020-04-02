package com.tecnosols.homeaid;

public class catModel {
    String catName;
    String catImg;
    String catId;

    public catModel(String catName, String catImg, String catId) {
        this.catName = catName;
        this.catImg = catImg;
        this.catId = catId;
    }

    public catModel(String catName, String catImg) {
        this.catName = catName;
        this.catImg = catImg;
    }

    public catModel() {
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatImg() {
        return catImg;
    }

    public void setCatImg(String catImg) {
        this.catImg = catImg;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }
}
