package com.entities;
import javax.persistence.*;
/**
 * Created by ericweidman on 2/3/17.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    int id;

    @Column(nullable = false, unique = true)
    private
    String userName;

    @Column(nullable = false)
    private
    String pin;

    public User(int id, String userName, String pin) {
        this.id = id;
        this.userName = userName;
        this.pin = pin;
    }

    public User(String userName, String pin) {
        this.userName = userName;
        this.pin = pin;
    }

    public User(String userName) {
        this.userName = userName;
    }

    public User(int id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
