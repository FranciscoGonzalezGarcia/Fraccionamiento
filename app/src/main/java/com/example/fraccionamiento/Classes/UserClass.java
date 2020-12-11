package com.example.fraccionamiento.Classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


// Clase para obtener los datos del usuario

public class UserClass {
    private String name;
    private String email;
    private String urlImg;
    private String lastName;
    private String role;
    private String key;
    private Boolean debt;
    private int deptNum;
    private String build;
    private int payDay;
    private int rent;
    private int maintenance;
    private String pass;


    public UserClass() {
    }


    public UserClass(String name, String email, String urlImg, String lastName, String role, String key, Boolean debt, int deptNum, String build, int payDay, int rent, int maintenance) {
        this.name = name;
        this.email = email;
        this.urlImg = urlImg;
        this.lastName = lastName;
        this.role = role;
        this.key = key;
        this.debt = debt;
        this.deptNum = deptNum;
        this.build = build;
        this.payDay = payDay;
        this.rent = rent;
        this.maintenance = maintenance;
    }

    public UserClass(String name, String email, String lastName, String role, String urlImg) {
        this.name = name;
        this.email = email;
        this.lastName = lastName;
        this.role = role;
        this.urlImg = urlImg;
    }

    public UserClass(String name, String email, String lastName, String role, String urlImg, Boolean dept) {
        this.name = name;
        this.email = email;
        this.lastName = lastName;
        this.role = role;
        this.urlImg = urlImg;
        this.debt = dept;
    }

    public UserClass(HashMap<String, String> map) {
        this.name = map.get("name");
        this.email = map.get("email");
        this.lastName = map.get("lastName");
        this.role = map.get("role");
        this.urlImg = map.get("urlImg");
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public Boolean getDebt() {
        return debt;
    }

    public void setDebt(Boolean debt) {
        this.debt = debt;
    }

    public int getDeptNum() {
        return deptNum;
    }

    public void setDeptNum(int deptNum) {
        this.deptNum = deptNum;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public int getPayDay() {
        return payDay;
    }

    public void setPayDay(int payDay) {
        this.payDay = payDay;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public int getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(int maintenance) {
        this.maintenance = maintenance;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("urlImg", urlImg);
        result.put("name", name);
        result.put("lastName", lastName);
        result.put("email", email);
        result.put("role", role);
        result.put("debt", debt);
        result.put("build", build);
        result.put("payDay", payDay);
        result.put("rent", rent);
        result.put("maintenance", maintenance);
        return result;
    }
}