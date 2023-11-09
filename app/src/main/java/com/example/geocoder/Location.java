package com.example.geocoder;

public class Location {
    Integer id;
    String latitude;
    String longitude;
    String address;
    public Location(String location) {
        // Create Location object using a String (this will be read from a file)
        String[] parts = location.split(";");
        latitude = parts[0];
        longitude = parts[1];
        address = parts[2];
    }
    public Location(Integer id, String latitude, String longitude, String address) {
        // Create Location object by providing each individual field
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }
    public Integer getId() {
        return id;
    }
    public String getLatitude() {
        return latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
