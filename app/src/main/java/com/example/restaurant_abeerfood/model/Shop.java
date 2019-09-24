package com.example.restaurant_abeerfood.model;

public class Shop {
    private int id, isOpen;
    private String name, slug, phoneNumber, traderLicense, openAt, closeAt, image, location;

    public Shop(int id, int isOpen, String name, String slug, String phoneNumber, String traderLicense, String openAt, String closeAt, String image, String location) {
        this.id = id;
        this.isOpen = isOpen;
        this.name = name;
        this.slug = slug;
        this.phoneNumber = phoneNumber;
        this.traderLicense = traderLicense;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.image = image;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTraderLicense() {
        return traderLicense;
    }

    public void setTraderLicense(String traderLicense) {
        this.traderLicense = traderLicense;
    }

    public String getOpenAt() {
        return openAt;
    }

    public void setOpenAt(String openAt) {
        this.openAt = openAt;
    }

    public String getCloseAt() {
        return closeAt;
    }

    public void setCloseAt(String closeAt) {
        this.closeAt = closeAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
