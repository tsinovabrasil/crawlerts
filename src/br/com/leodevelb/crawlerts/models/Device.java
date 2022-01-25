package br.com.leodevelb.crawlerts.models;

import com.fasterxml.jackson.databind.JsonNode;

public class Device {
    
    private String host;
    private String username;
    private String password;
    private int interval;
    private JsonNode appendFields;
    private String manufacturer;
    private String category;

    public Device() {
    }        

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }   
    
    public JsonNode getAppendFields() {
        return appendFields;
    }

    public void setAppendFields(JsonNode appendFields) {
        this.appendFields = appendFields;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }    
    
    public boolean isWirelessAlvarion(){
        return manufacturer.equalsIgnoreCase("Alvarion") && category.equalsIgnoreCase("Wireless");
    }
    
}