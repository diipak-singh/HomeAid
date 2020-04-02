package com.tecnosols.homeaid;

public class ServiceDetail {
    String userImg, userName, userPhone, userCity, userAddress, itemImg, itemName, itemCatg, itemPrice, serviceDay, serviceTime, currentDay, currentTime, otherDetail, serviceId, userId, paymentStatus, isCompleted,rating;

    public ServiceDetail() {
    }

    public ServiceDetail(String userImg, String userName, String userPhone, String userCity, String userAddress, String itemImg, String itemName, String itemCatg, String itemPrice, String serviceDay, String serviceTime, String currentDay, String currentTime, String otherDetail, String serviceId, String userId, String paymentStatus, String isCompleted, String rating) {
        this.userImg = userImg;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userCity = userCity;
        this.userAddress = userAddress;
        this.itemImg = itemImg;
        this.itemName = itemName;
        this.itemCatg = itemCatg;
        this.itemPrice = itemPrice;
        this.serviceDay = serviceDay;
        this.serviceTime = serviceTime;
        this.currentDay = currentDay;
        this.currentTime = currentTime;
        this.otherDetail = otherDetail;
        this.serviceId = serviceId;
        this.userId = userId;
        this.paymentStatus = paymentStatus;
        this.isCompleted = isCompleted;
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getItemCatg() {
        return itemCatg;
    }

    public void setItemCatg(String itemCatg) {
        this.itemCatg = itemCatg;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
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

    public String getServiceDay() {
        return serviceDay;
    }

    public void setServiceDay(String serviceDay) {
        this.serviceDay = serviceDay;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(String currentDay) {
        this.currentDay = currentDay;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getOtherDetail() {
        return otherDetail;
    }

    public void setOtherDetail(String otherDetail) {
        this.otherDetail = otherDetail;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
