package com.controllers;

import com.entities.SaveData;
import com.entities.User;
import com.services.SaveDataRepository;
import com.services.UserRepository;
import com.utilities.SendMailTLS;
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

        String sessionUser = (String) session.getAttribute("username");
        User checkIfNew = users.findByUserName(sessionUser);
        SaveData loadGame = saveData.findByUserId(checkIfNew.getId());
        Boolean isAdmin = checkIfNew.getAdmin();
        String returnString;

        if (isAdmin) {
            loadGame = new SaveData(checkIfNew.getId(), "Admin lounge", "Admin Tools", true, true, true, true, checkIfNew);
            saveData.save(loadGame);
            returnString = "Welcome to the admin lounge!</br>" +
                    "Enter \"tools\" to see a list of unique Admin features.";
        } else if (loadGame == null) {
            loadGame = new SaveData(checkIfNew.getId(), "sidewalk", "Sara has a keyring.</br>", false, false, false, false, checkIfNew);
            saveData.save(loadGame);
            returnString = "Sara Berkeley finds herself standing in front of an unremarkable Oakland home.</br>" +
                    "Today is a typical California spring day. The sun is shining, birds are chirping, the air the perfect kind of mild.</br>" +
                    "She tends not to appreciate days like this one anymore. Her world is constantly dampened by dark, ominous clouds.</br></br>" +

                    "Sara suffers from major depressive disorder. Her doctor diagnosed her recently, but it has been her truth for the majority of her days.</br>" +
                    "Oftentimes she thinks about how hard she would fight it when she was younger. Sometimes she wonders what changed.</br>" +
                    "She is completely aware that she doesn't try to compete with it anymore. She's been outmatched for months.</br> " +
                    "Some days she refuses to grapple her depression, and instead embraces it.</br>" +
                    "Today is one of those days.</br></br>" +

                    "She is at this particular house because she has her job to do. The same job she's been doing for years.</br>" +
                    "She is a detective for the Oakland City Police Department.</br></br>" +

                    "Sara despises her work.</br></br>" +

                    "She let out a long sigh. </br>" +
                    "\"Well, let's get this over with.\" she muttered.</br></br>" +
                    "---</br></br>" +

                    "Enter \"commands\" at any time to see a list of useful actions.</br>" +
                    "Your game is saved automatically.</br></br>" +
                    "---</br>";
            loadGame.setHasSeenIntro(true);
            saveData.save(loadGame);

        } else {
            switch (loadGame.getLocation()) {
                case "sidewalk":
                    returnString = "Sara is standing on the the sidewalk.</br></br>" +
                            loadGame.getItems() + "</br>---</br></br>" +
                            "Enter \"commands\" to see a list of useful commands.";
                    break;
                case "front door":
                    returnString = "Sara is standing at the front door.</br></br>" +
                            loadGame.getItems() + "</br>---</br></br>" +
                            "Enter \"commands\" to see a list of useful commands.";
                    break;
                default:
                    returnString = "Sara is standing in the " + loadGame.getLocation() + ".</br></br>" +
                            loadGame.getItems() + "</br>---</br></br>" +
                            "Enter \"commands\" to see a list of useful actions.";
                    break;
            }
        }
        return returnString;
    }

    @RequestMapping(path = "/user-action", method = RequestMethod.POST)
    public String userAction(@RequestBody String userAction, HttpSession session) {
        String action = userAction.toLowerCase();
        String response = "";
        String adminResponse;
        assert session != null;
        String userSessionName = (String) session.getAttribute("username");
        User user = users.findByUserName(userSessionName);
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

                    adminResponse = "---</br></br>" +
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
                case "where am i?":
                case "where is sara?":
                case "where is sara":
                case "where am i":
                case "current location":

                    switch (currentLocation) {
                        case "sidewalk":
                            response = "Sara is standing on the the sidewalk.";
                            break;
                        case "front door":
                            response = "Sara is standing at the front door.";
                            break;
                        default:
                            response = "Sara is standing in the " + currentLocation + ".";
                            break;
                    }
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

                case "inspect keyring":
                case "inspect keys":
                    response = "The keyring she is carrying used to belong to Johnathan Mercer. There are two keys attached.</br>" +
                            "One of the keys looks like any standard key. It probably unlocks a door.</br>" +
                            "The other key has a large fob attached, displaying a FORD logo.";
                    break;

                case "look around":
                    switch (currentLocation) {
                        case "sidewalk":
                            response = "Sara looks around. She is standing on the sidewalk in an average Oakland neighborhood.</br>" +
                                    "It's the early afternoon on a Tuesday. Most of the driveways are empty.</br> " +
                                    "The normal folk who live here are at work.</br>" +
                                    "All of the houses are rather plain looking. Every nth house or so is seemingly identical.</br>" +
                                    "The uninteresting house closest to her, she suspects to be empty. Her cruiser is parked in the driveway.</br>" +
                                    "What's left of her sense of duty pushes her to the front door.";
                            break;
                        case "front door":
                            response = "Sara looks around. She is standing at the front door of the home of Johnathan Mercer.</br>";
                            break;
                        case "living room":
                            response =  "Sara looks around.</br>" +
                                        "She sees a neatly arranged living room with a single sofa pointed at a rather large wall mounted television.</br>" +
                                        "In front of the sofa is a smallish black wooden coffee table displaying nothing but a smooth black surface.</br>";
                            break;
                        case "bedroom":
                            response = "Sara looks around. She is standing in the bedroom.";
                            break;
                        case "kitchen":
                            response = "Sara looks around. She is standing in the kitchen.";
                            break;
                        case "garage":
                            response = "Sara looks around. She is standing in the garage.";
                            break;
                        case "bathroom":
                            response = "Sara looks around. She is standing in the bathroom.";
                            break;
                        case "office":
                            response = "Sara looks around. She is standing in the office.";
                            break;
                        case "bunker":
                            response = "Sara looks around. She is standing in a bunker.";
                            break;
                    }
                    break;

                case "turn on the television":
                case "turn on television":
                case "turn on the tv":
                case "turn on tv":
                case "put on the television":
                case "put on television":
                case "put on the tv":
                case "put on tv":
                    response = "Sara turns on the television. It's tuned to CNN.</br>" +
                                "\"... in response to the ongoing case regarding his travel ban The President commented..\"</br></br>" +
                                "\"You could be a lawyer, or you don't have to be a lawyer, If you were a good student in high school</br>" +
                                "or a bad student in high school, you can understand this.</br>" +
                                "And it's really incredible to me that we have a court case that's going on so long.\"</br></br>" +
                                "She felt her stomach drop, immediately she became irate.</br></br>" +
                                "She could feel anxiety creeping its way up her spine. She cut it off before it gripped her conscious thought.</br>" +
                                "\"Now is not the time.\" she thought out loud.</br>";
                    break;

                case "change channel":
                case "change station":
                case "change the channel":
                        response = "";
                        break;

                case "walk to the door":
                case "go to the front door":
                case "move to the front door":
                case "walk to the front door":
                case "go to front door":
                case "move to front door":
                case "walk to front door":

                    if (currentLocation.equals("front door")) {
                        response = "Sara is already at the front door.";
                    } else if (userData.getHasSeenFrontDoor()) {
                        response = "Sara walks back to the front door.";
                        userData.setLocation("front door");
                        saveData.save(userData);
                    } else {

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
                                "While she didn't have any good things to say about Johnathan,</br> she saw no reason to celebrate another addition to" +
                                " the long list of her coworkers fuck-ups.</br>" +
                                "She dislikes everyone at her precinct.</br></br>" +

                                "Her final thought concerning Johnathan before reaching the door was \"lucky bastard...\"</br>" +
                                "She chuckled to herself.";

                        userData.setLocation("front door");
                        userData.setHasSeenFrontDoor(true);
                        saveData.save(userData);
                    }
                    break;

                case "unlock":
                    response = "Sara wonders what she should unlock.";
                    break;


                case "walk to the office":
                case "go to the office":
                case "open office door":
                    if(userData.getFrontDoorUnlocked()){
                        response = "Sara walks into the office.";
                        userData.setLocation("office");
                        saveData.save(userData);
                    }else{
                        response = "Sara wonders if there is an office inside of the home.";
                    }
                    break;

                case "unlock the front door":
                case "unlock door":
                case "unlock front door":
                case "unlock the door":

                    if (currentLocation.equals("front door") && !userData.getFrontDoorUnlocked()) {
                        response = "Sara takes the keyring from her pocket, and tries one of the keys in lock. She successfully unlocks the front door.";
                        userData.setFrontDoorUnlocked(true);
                        saveData.save(userData);
                    } else if (!currentLocation.equals("front door") && userData.getFrontDoorUnlocked()) {
                        response = "Sara already unlocked the front door";
                    } else {
                        response = "Sara cannot open the front door from where she is standing.";
                    }
                    break;

                case "open the front door":
                case "open door":
                case "open front door":
                case "open the door":
                    if (userData.getFrontDoorUnlocked() && currentLocation.equals("front door")) {
                        response = "Sara opens the door and steps inside the house. She closes the front door behind herself.</br>" +
                                    "She sees a neatly arranged living room with a single sofa pointed at a rather large wall mounted television.</br>" +
                                    "In front of the sofa is a smallish black wooden coffee table displaying nothing but a smooth black surface.</br>" +
                                    "While the room was rather empty, she noticed that there was a certain elegance to it. It was clean, and made perfect use of its negative space.</br>" +
                                    "She immediately knew Johnathan had been a minimalist. She resented him for it.</br></br>" +
                                    "Sara always considered herself a minimalist,  and she hated that she shared anything with Johnathan.</br>" +
                                    "Even if it was just a preferred choice in aesthetics.</br>" +
                                    "She wonders if there is a connection between her lack of attachment to personal objects, and Johnathans'.</br>"+
                                    "She shakes off the thought.";
                        userData.setLocation("living room");
                        saveData.save(userData);
                    } else if (!userData.getFrontDoorUnlocked() && currentLocation.equals("front door")) {
                        response = "Sara tries to open the door. It's locked.";
                    } else {
                        response = "Sara isn't by the front door.";
                    }
                    break;

                case "walk to sidewalk":
                case "move to sidewalk":
                case "go back to the sidewalk":
                case "sidewalk":
                case "walk to the sidewalk":
                case "move to the sidewalk":

                    if (currentLocation.equals("sidewalk")) {
                        response = "Sara is already on the sidewalk.";
                    } else {
                        response = "Sara walks to the sidewalk.";
                        userData.setLocation("sidewalk");
                        saveData.save(userData);
                    }
                    break;

                case "send email":
                case "send e-mail":
                case "write e-mail":
                case "e-mail":
                case "email":
                    if(currentLocation.equals("office")){
                        response = "Who should Sara send an e-mail to?";
                    }else{
                        response = "Sara isn't near a device she could send an e-mail from.";
                    }

                    break;

                case "this sucks":
                case "this game sucks":
                case "sara sucks":
                case "detective sara sucks":
                    response = "\"No, you suck.\" Sara suddenly quipped for some strange reason. ";
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

                case "what is my username":
                case "username":
                    response = "Your username is " + userSessionName + ".";
                    break;

                case "jump":
                case "jump up and down":
                case "jump for joy":
                    response = "Sara cannot think of any reasons to jump for joy.";
                    break;

                case "inspect":
                    response = "Sara wonders what kind of horrors she'll have to inspect today.";
                    break;

                case "open":
                    response = "Sara decides she needs something to open, unfortunately she has no ideas.";
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

                case "save":
                case "exit":
                case "logout":
                case "savegame":
                case "save game":
                case "quit":

                    response = "Saving...";
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

                case "inspect inventory":
                case "open inventory":
                case "item":
                case "inventory":
                case "items":

                    response = currentItems;
                    break;


                default:
                    response = "Unrecognized command.";
                    break;
            }

            if (action.substring(action.length() - 4, action.length()).equals(".com") && userData.getLocation().equals("office")) {
                SendMailTLS.sendMail(action, user.getUserName());
                response = "Sara composes and sends an e-mail to a friend.";
            }

            return response;
        }
    }
}
