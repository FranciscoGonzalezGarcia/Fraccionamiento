package com.example.fraccionamiento.Classes;

public class UserClass {
    private String name;
    private String email;
    private String urlImg;
    private String lastName;
    private String role;
    private String key;

    public UserClass(String name, String email, String lastName, String role, String urlImg) {
        this.name = name;
        this.email = email;
        this.lastName = lastName;
        this.role = role;
        this.urlImg = urlImg;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public UserClass() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}