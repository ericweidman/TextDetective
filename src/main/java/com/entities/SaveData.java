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

    @Column(nullable = false)
    private
    Boolean hasFinished = false;

    @Column
    private
    Boolean hasSeenIntro = false;

    @Column
    private
    Boolean hasSeenFrontDoor = false;

    @Column
    private
    Boolean frontDoorUnlocked = false;

    @OneToOne
    private
    User user;

    public Boolean getFrontDoorUnlocked() {
        return frontDoorUnlocked;
    }

    public void setFrontDoorUnlocked(Boolean frontDoorUnlocked) {
        this.frontDoorUnlocked = frontDoorUnlocked;
    }

    public Boolean getHasSeenFrontDoor() {
        return hasSeenFrontDoor;
    }

    public void setHasSeenFrontDoor(Boolean hasSeenFrontDoor) {
        this.hasSeenFrontDoor = hasSeenFrontDoor;
    }

    public Boolean getHasSeenIntro() {
        return hasSeenIntro;
    }

    public void setHasSeenIntro(Boolean hasSeenIntro) {
        this.hasSeenIntro = hasSeenIntro;
    }

    public SaveData(int id, String location, String items, Boolean hasFinished, Boolean hasSeenIntro, Boolean hasSeenFrontDoor, Boolean frontDoorUnlocked, User user) {
        this.frontDoorUnlocked = frontDoorUnlocked;
        this.hasSeenIntro = hasSeenIntro;
        this.hasSeenFrontDoor = hasSeenFrontDoor;
        this.id = id;
        this.location = location;
        this.items = items;
        this.hasFinished = hasFinished;
        this.user = user;
    }

    public SaveData(int id, String location, String items, User user) {
        this.id = id;
        this.location = location;
        this.items = items;
        this.user = user;
    }

    public String getItems() {
        return items;
    }

    public SaveData(Boolean hasFinished) {
        this.hasFinished = hasFinished;
    }

    public Boolean getHasFinished() {
        return hasFinished;
    }

    public void setHasFinished(Boolean hasFinished) {
        this.hasFinished = hasFinished;
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
