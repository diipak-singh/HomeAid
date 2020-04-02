package com.tecnosols.homeaid;

public class MyBookingsModel {
    String itemImg,itemCatg, itemName,itemPrice, bookingDT, serviceDT,isCompleted,rating,paymentStatus,bookingID,serviceID,userID,assignedWid;

    public MyBookingsModel() {
    }

    public MyBookingsModel(String itemImg, String itemCatg, String itemName, String itemPrice, String bookingDT, String serviceDT, String isCompleted, String rating, String paymentStatus, String bookingID, String serviceID, String userID, String assignedWid) {
        this.itemImg = itemImg;
        this.itemCatg = itemCatg;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.bookingDT = bookingDT;
        this.serviceDT = serviceDT;
        this.isCompleted = isCompleted;
        this.rating = rating;
        this.paymentStatus = paymentStatus;
        this.bookingID = bookingID;
        this.serviceID = serviceID;
        this.userID = userID;
        this.assignedWid = assignedWid;
    }

    public String getAssignedWid() {
        return assignedWid;
    }

    public void setAssignedWid(String assignedWid) {
        this.assignedWid = assignedWid;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public String getItemCatg() {
        return itemCatg;
    }

    public void setItemCatg(String itemCatg) {
        this.itemCatg = itemCatg;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getBookingDT() {
        return bookingDT;
    }

    public void setBookingDT(String bookingDT) {
        this.bookingDT = bookingDT;
    }

    public String getServiceDT() {
        return serviceDT;
    }

    public void setServiceDT(String serviceDT) {
        this.serviceDT = serviceDT;
    }

    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }
}
