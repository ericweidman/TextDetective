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
    String userName;

    @Column(nullable = false)
    int pin;
}
