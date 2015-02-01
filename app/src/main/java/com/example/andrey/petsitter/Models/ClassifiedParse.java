package com.example.andrey.petsitter.Models;

import android.graphics.Bitmap;
import android.location.Location;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by Andrey on 1.2.2015 г..
 */

@ParseClassName("Classified")
public class ClassifiedParse extends ParseObject {

    private String title;
    private String description;
    private String authorName;
    private String phone;
    private String address;
    private AnimalType animalType;
    private Bitmap image;
    private ParseGeoPoint location;

    public ClassifiedParse(){}

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setLocation(Location location) {
        put("location", new ParseGeoPoint(location.getLatitude(), location.getLongitude()));
    }

    public String getAnimalType() {
        return getString("animalType");
    }

    public void setAnimalType(AnimalType animalType) {
        put("animalType", animalType.toString());
    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public String getAuthorName() {
        return getString("name");
    }

    public void setAuthorName(String authorName) {
        put("name", authorName);
    }

    public String getPhone() {
        return getString("phone");
    }

    public void setPhone(String phone) {
        put("phone", phone);
    }

    public String getAddress() {
        return getString("address");
    }

    public void setAddress(String address) {
        put("address", address);
    }

    public ParseFile getImage() {
        return getParseFile("image");
    }

    public void setImage(Bitmap image, String name) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        ParseFile parseImage = new ParseFile(name, byteArray);
        put("image", parseImage);
    }
}
