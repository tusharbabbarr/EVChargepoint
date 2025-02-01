package com.example.chargepointapplication.Models;


import java.io.Serializable;

//This will show all basic information about user.
public class ChargePoint implements Serializable {
    private String referenceID;
    private double latitude;
    private double longitude;
    private String town;
    private String county;
    private String postcode;
    private String chargeDeviceStatus;
    private String connectorID;
    private String connectorType;

    public ChargePoint(String referenceID, double latitude, double longitude, String town, String county, String postcode, String chargeDeviceStatus, String connectorID, String connectorType) {
        this.referenceID = referenceID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.town = town;
        this.county = county;
        this.postcode = postcode;
        this.chargeDeviceStatus = chargeDeviceStatus;
        this.connectorID = connectorID;
        this.connectorType = connectorType;
    }

    public String getReferenceID() {
        return referenceID;
    }

    public String getTown() {
        return town;
    }

    public String getCounty() {
        return county;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setReferenceID(String referenceID) {
        this.referenceID = referenceID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getChargeDeviceStatus() {
        return chargeDeviceStatus;
    }

    public void setChargeDeviceStatus(String chargeDeviceStatus) {
        this.chargeDeviceStatus = chargeDeviceStatus;
    }

    public String getConnectorID() {
        return connectorID;
    }

    public void setConnectorID(String connectorID) {
        this.connectorID = connectorID;
    }

    public String getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }
}

