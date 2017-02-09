package com.controllers;


import com.entities.Intro;
import com.entities.SaveData;
import com.entities.User;
import com.services.SaveDataRepository;
import com.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * Created by ericweidman on 2/3/17.
 */
@RestController
public class TextDetectiveController {


    @Autowired
    UserRepository users;

    @Autowired
    SaveDataRepository saveData;

    @RequestMapping(path = "/create-user", method = RequestMethod.POST)
    public void newUser(@RequestBody User newUser, HttpSession session) throws Exception {

        User checkUser = users.findByUserName(newUser.getUserName().toLowerCase());
        User saveUser = new User(newUser.getUserName().toLowerCase(), newUser.getPin());
        int userCount = (int) users.count() + 1;

        if (newUser.getUserName() == null) {
            throw new Exception("No username entered");
        } else if (checkUser == null && newUser.getUserName().equals("ericweidman")) {
            session.setAttribute("username", newUser.getUserName().toLowerCase());
            User admin = new User(newUser.getUserName().toLowerCase(), newUser.getPin(), true);
            users.save(admin);
            System.out.println("User " + newUser.getUserName().toLowerCase() + " created!");
            System.out.println("User count is now " + userCount + "!");
        } else if (checkUser == null) {
            session.setAttribute("username", newUser.getUserName().toLowerCase());
            users.save(saveUser);
            System.out.println("User " + newUser.getUserName().toLowerCase() + " created!");
            System.out.println("User count is now " + userCount + "!");
        } else if (newUser.getUserName().toLowerCase().equals(checkUser.getUserName()) && !newUser.getPin().equals(checkUser.getPin())) {
            throw new Exception("Username already in use");
        } else if (newUser.getUserName().toLowerCase().equals(checkUser.getUserName()) && newUser.getPin().equals(checkUser.getPin())) {
            session.setAttribute("username", checkUser.getUserName().toLowerCase());
        } else {
            session.setAttribute("username", newUser.getUserName().toLowerCase());
            users.save(saveUser);
        }
    }

    @RequestMapping(path = "/intro", method = RequestMethod.GET)
    public String userintro(HttpSession session) {

        Intro intro = new Intro();
        intro.intro(session);
        return intro.getReturnString();

    }

    @RequestMapping(path = "/user-action", method = RequestMethod.POST)
    public String userAction(@RequestBody String userAction, HttpSession session) {
        String action = userAction.toLowerCase();
        String response;
        String adminResponse;
        assert session != null;
        String userSessionName = (String) session.getAttribute("username");
        User user = users.findByUserName(userSessionName);
        int userId = user.getId();
        SaveData userData = saveData.findByUserId(user.getId());
        String currentLocation = userData.getLocation();
        String currentItems = userData.getItems();
        Boolean isAdmin = user.getAdmin();
        int number = 0;

        if (isAdmin) {

            switch (action) {

                case "commands":
                case "tools":
                case "help":
                case "admin tools":
                case "admin":

                    adminResponse = "---<br></br>" +
                            "\"user count\" - Will display how many unique are in the database.</br>" +
                            "\"list users\" - Will display a list of all users.</br>" +
                            "\"list admins\" - Will display a list of admins.</br>" +
                            "\"completed\" - Will display the number of accounts have competed the game.</br>" +
                            "---</br></br>";
                    break;


                case "unique users":
                case "user count":
                case "count":
                    long countUsers = users.count();
                    String usersCount = Long.toString(countUsers);
                    adminResponse = "There are " + usersCount + " users currently in the database.";
                    break;

                case "user list":
                case "list users":
                case "users":
                    String another = "";
                    List<User> allUsers = (List<User>) users.findAll();
                    for (User eachUser : allUsers) {
                        another += "- " + eachUser.getUserName() + " ";
                    }
                    adminResponse = another;
                    break;

                case "admins":
                case "list admins":
                    String diff = "";
                    List<User> allAdmins = (List<User>) users.findAll();
                    for (User everyAdmin : allAdmins) {
                        if (everyAdmin.getAdmin()) {
                            diff += "- " + everyAdmin.getUserName() + " ";
                        }
                    }
                    adminResponse = diff;
                    break;

                case "complete":
                case "completed":
                case "times finished":
                case "finished":

                    List<SaveData> allSaveData = (List<SaveData>) saveData.findAll();

                    for (SaveData finished : allSaveData) {
                        if (finished.getHasFinished()) {
                            number++;
                        }
                    }
                    adminResponse = "Detective Sara has been completed by " + Integer.toString(number) + " users.";
                    break;

                default:
                    adminResponse = "Unrecognized command.";
                    break;
            }
            return adminResponse;
        } else {
            switch (action) {
                case "location":
                    response = "Sara is currently at the " + currentLocation + ".";
                    break;

                case "help":
                case "halp":
                case "wtf":
                case "commands":
                    response =
                            "---<br></br>" +
                                    "Useful commands include things like:</br></br>" +
                                    "\"Look around\"</br>" +
                                    "\"Move to (location)\" e.g. \"front door\"</br>" +
                                    "\"Inspect (item)\" e.g. \"doorknob\"</br>" +
                                    "\"Inventory\"</br>" +
                                    "\"Open (item)\" e.g. \"trashcan\"</br>" +
                                    "\"About\"</br></br>" +
                                    "You are encouraged to experiment.</br></br>" +
                                    "Enter \"quit\" to save and exit.</br></br>" +
                                    "---</br>";
                    break;

                case "save":
                case "exit":
                case "logout":
                case "savegame":
                case "save game":
                case "quit":

                    response = "Saving...";
                    break;

                case "about":
                case "detective sara":
                case "about detective sara":
                    response = "You are currently logged in as " + userSessionName + ".</br>" +
                            "---</br>" +
                            "'Detective Sara' was programmed and written by Eric Weidman.</br>" +
                            "If you have any questions/comments/feedback/criticisms/devjobs I would love to hear form you!</br>" +
                            "Feel free to shoot me an email - ericweidman@gmail.com</br>" +
                            "All code for this game can be found at https://github.com/ericweidman/TextDetective</br>" +
                            "If you're reading this, thank you for playing!</br>";

                    break;

                case "kill self":
                case "kill yourself":
                case "kill sara":
                case "die":
                    response = "Sara considers killing herself for brief a moment. She decides now is not the time.";
                    break;

                case "break arm":
                case "break leg":
                case "hurt self":
                case "cut arm":
                case "cut leg:":
                case "cut self":
                case "punch self":
                    response = "Sara considers hurting herself. She thinks there will be plenty of time for self loathing later.";
                    break;

                case "pickup rock":
                    currentItems = currentItems + "Sara has a small rock.</br>";
                    saveItems(userId, currentLocation, currentItems, user);
                    response = "Sara sees a small rock on the ground. She picks it up and pockets it.";
                    break;

                case "inspect inventory":
                case "open inventory":
                case "item":
                case "inventory":
                case "items":

                    saveItems(userId, currentLocation, currentItems, user);
                    response = currentItems;
                    break;

                case "inspect keyring":
                case "inspect keys":
                    response = "The keyring she is carrying used to belong to Johnathan Mercer. There are two keys attached.</br>" +
                            "One of the keys looks like any standard key. It probably unlocks a door.</br>" +
                            "The other key has a large fob attached, displaying a FORD logo.";
                    break;

                case "open keys":
                case "open keyring":
                    response = "Sara considers opening the key ring, but decides against it as she doesn't have much of a reason.";
                    break;

                case "break keys":
                case "break keyring":
                case "smash keys":
                case "smash keyring":
                    response = "Sara normally doesn't mind breaking things, but she decides she needs the keyring.";
                    break;

                case "throw keys":
                case "throw keyring":
                    response = "Sara had a sudden urge to throw the keyring, but it eventually passed.";
                    break;

                case "look around":
                    response = "Sara looks around. She is standing on the sidewalk in an average Oakland neighborhood.</br>" +
                            "It's the early afternoon on a Tuesday. Most of the driveways are empty.</br> " +
                            "The normal folk who live here are at work.</br>" +
                            "All of the houses are rather plain looking. Every nth house or so is seemingly identical.</br>" +
                            "The uninteresting house closest to her, she suspects to be empty. Her cruiser is parked in the driveway.</br>" +
                            "What's left of her sense of duty pushes her to the front door.";
                    break;

                case "inspect":
                    response = "Sara wonders what kind of of horrors she'll have to inspect today.";
                    break;

                case "open":
                    response = "Sara decides she needs something to open, unfortunately she has no ideas.";
                    break;

                case "open trashcan":
                    response = "There is not a trashcan nearby for Sara to open.";
                    break;

                case "inspect trashcan":
                    response = "There is no trashcan nearby for Sara to inspect.";
                    break;


                case "inspect door":
                case "inspect front door":
                    response = "Sara can see the door from here, but from this distance she cannot make out any distinguishing characteristics.";
                    break;

                case "inspect doorknob":
                    response = "Sara can hardly see the doorknob from where she is standing.";
                    break;

                case "open doorknob":
                case "open door":
                case "unlock door":
                    response = "Sara cannot reach the door from where she is standing on the sidewalk, let alone open it.";
                    break;

                case "jump":
                case "jump up and down":
                case "jump for joy":
                    response = "Sara cannot think of any reasons to jump for joy.";
                    break;

                case "walk to the door":
                case "go to the front door":
                case "move to the front door":
                case "walk to the front door":
                case "go to front door":
                case "move to front door":
                case "walk to front door":

                    response = "As Sara walks towards the door, she starts to think about Johnathan Mercer.</br>" +
                            "Johnathan suffocated himself earlier this morning during his latest stint in a city jail cell.</br>" +
                            "Apparently somebody had forgotten to remove his belt before locking him up for the night.</br>" +
                            "He did it shorty after a search warrant for his home had been issued.</br>" +
                            "The same home Sara was headed to.</br><br>" +

                            "She thought the suicide a bit odd, Johnathan had been in and out the county jail more times than she could remember.</br>" +
                            "His slimy lawyer normally didn't have any trouble getting him out.</br>" +
                            "Typically one of her idiot coworkers would forget how to book him properly, or forget to read him his rights,</br>" +
                            " or beat the ever loving shit out of him before bringing him in.</br></br>" +

                            "Her fellow officers didn't give the suicide a second thought, in fact many of them celebrated it.</br>" +
                            "While she didn't have any good things to say about Johnathan, she saw no reason to celebrate another addition to" +
                            " the long list of her coworkers fuck-ups.</br>" +
                            "She dislikes everyone at her precinct.</br></br>" +

                            "Her final thought concerning Johnathan before reaching the door was \"lucky bastard...\"</br>" +
                            "She chuckled to herself.";

                    currentLocation = "front door";
                    saveLocation(userId, currentLocation, currentItems, user);

                    break;

                case "overwatch":
                case "overwatch opinions":
                    response = "Oddly enough Sara starts thinking about some Overwatch truths.</br>" +
                            "\"Bastion sucks.\"</br>" +
                            "\"Never do you need a Hanzo AND a Widowmaker.\"</br>" +
                            "\"Tracer is the best character in the game.\"";
                    break;

                case "think":
                case "daydream":
                case "ponder":
                    response = "Sara doesn't want to think about things right now. She spends a lot of time avoiding her thoughts.";
                    break;

                case "this sucks":
                case "this game sucks":
                case "sara sucks":
                case "detective sara sucks":
                    response = "\"No, you suck.\" Sara suddenly quipped for some reason that even baffled her...";
                    break;

                case "what is my username":
                case "username":
                    response = "Your username is " + userSessionName + ".";
                    break;

                case "walk to sidewalk":
                case "move to sidewalk":
                case "go back to the sidewalk":
                case "sidewalk":
                case "walk to the sidewalk":
                case "move to the sidewalk":

                    currentLocation = "sidewalk";
                    saveLocation(userId, currentLocation, currentItems, user);

                    response = "Sara walks to the sidewalk.";
                    break;

                default:
                    response = "Unrecognized command.";
                    break;
            }

            return response;
        }
    }

    private void saveLocation(int userId, String location, String items, User user) {
        SaveData changeData = new SaveData();
        changeData.setId(userId);
        changeData.setLocation(location);
        changeData.setItems(items);
        changeData.setUser(user);
        saveData.save(changeData);
    }

    private void saveItems(int userId, String location, String items, User user) {
        SaveData changeData = new SaveData();
        changeData.setId(userId);
        changeData.setLocation(location);
        changeData.setItems(items);
        changeData.setUser(user);
        saveData.save(changeData);
    }
}
