package com.entities;

import javax.persistence.*;

/**
 * Created by ericweidman on 2/6/17.
 */

@Entity
@Table(name = "savedata")
public class SaveData {

    @Id
    private
    int id = 1;

    @Column(nullable = false)
    private
    String location;

    @Column(nullable = false, length = 1000)
    private
    String items;

    @OneToOne
    private
    User user;


    public SaveData(int id, String location, String items, User user) {
        this.id = id;
        this.location = location;
        this.items = items;
        this.user = user;
    }

    public String getItems() {
        return items;
    }


    public SaveData(String location, String items) {
        this.location = location;
        this.items = items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SaveData(int id, String location, User user) {
        this.id = id;
        this.location = location;
        this.user = user;
    }

    public SaveData() {
    }

    public SaveData(String location) {
        this.location = location;
    }

    public SaveData(User user) {
        this.user = user;
    }

    public SaveData(String location, User user) {
        this.location = location;
        this.user = user;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
