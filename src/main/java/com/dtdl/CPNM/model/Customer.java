package com.dtdl.CPNM.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(value = "customer")
public class Customer {

    @Id
    private String id;

    private String dateTime;

    private List<String> mobileNumbers;

    public Customer() {
    }

    public Customer(String dateTime, List<String> mobileNumbers) {
        this.dateTime = dateTime;
        this.mobileNumbers = mobileNumbers;
    }

    public List<String> getMobileNumbers() {
        return mobileNumbers;
    }

    public void setMobileNumbers(List<String> mobileNumbers) {
        this.mobileNumbers = mobileNumbers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
