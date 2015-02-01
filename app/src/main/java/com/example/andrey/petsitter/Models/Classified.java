package com.example.andrey.petsitter.Models;

import android.graphics.Bitmap;
import android.location.Location;

import java.lang.String;

/**
 * Created by Andrey on 25.1.2015 г..
 */
public class Classified {

    private String id;
    private String title;
    private String description;
    private String authorName;
    private String phone;
    private String address;
    private AnimalType animalType;
    private Bitmap image;
    private Location location;

    public Classified(String title, String description, String authorName, String phone, String address, Bitmap image, AnimalType type, Location location) {
        this(title, description, authorName, phone, address, image, type);

        this.location = location;
    }

    public Classified(String title, String description, String authorName, String phone, String address, Bitmap image, AnimalType type) {
        this(title, description, authorName, phone, address, image);

        this.animalType = type;
    }

    public Classified(String title, String description, String authorName, String phone, String address, Bitmap image) {
        this.title = title;
        this.description = description;
        this.authorName = authorName;
        this.phone = phone;
        this.address = address;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
