package com.controllers;

import com.entities.User;
import com.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by ericweidman on 2/3/17.
 */
@RestController
public class TextDetectiveController {


    @Autowired
    UserRepository users;

    @RequestMapping(path = "/create-user", method = RequestMethod.POST)
    public void newUser(@RequestBody User newUser, HttpSession session) throws Exception {

        User checkUser = users.findByUserName(newUser.getUserName().toLowerCase());
        User saveUser = new User(newUser.getUserName().toLowerCase(), newUser.getPin());
        int userCount = (int) users.count() + 1;

        if (newUser.getUserName() == null) {
            throw new Exception("No username entered");
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

    @RequestMapping(path = "/save-game", method = RequestMethod.POST)
    public void save(HttpSession session) {
        session.invalidate();

    }

    @RequestMapping(path = "/intro", method = RequestMethod.GET)
    public String intro(HttpSession session) {

        String brian = (String) session.getAttribute("username");
        String isBrian = brian.toLowerCase();

        String hiBrian = "Hi Brian! I suspect you might be one of the only people who plays this.</br>" +
                "I wrote this switch statement specifically just to say thanks.</br>" +
                "So, Thanks!</br>" +
                "---</br></br>";

        String intro = "Sara Berkeley finds herself standing in front of an unremarkable Oakland home.</br>" +
                "Today is a typical California spring day. The sun is shining, birds are chirping, the air the perfect kind of mild.</br>" +
                "She tends not to appreciate days like today anymore. Her world is constantly dampened by dark, ominous clouds.</br></br>" +

                "Sara suffers from major depression disorder. Her doctors diagnosed her recently, but she knew it the be the truth for the majority of her cognitive years.</br>" +
                "Oftentimes she thinks about how hard she fought it when she was younger.</br>" +
                "She is completely aware that she doesn't try to compete with it anymore. She is outmatched. Some days she almost embraces it.</br></br>"+

                "She is at this particular house because she has her job to do. The same job she's been doing for years. A job she has always excelled at.</br>" +
                "She is a detective for the Oakland City Police Department.</br></br>" +

                "Sara despises her work.</br></br>" +

                "She let out a long sigh. </br>" +
                "\"Well, let's get this over with.\" she muttered.</br></br>" +
                "---</br></br>" +

                "Enter \"commands\" for a list of actions to help get you started.</br></br>" +
                "---</br>";

        switch (isBrian) {
            case "brianweston":
            case "mrchozo":
            case "bweston":
            case "brianw":
                intro = hiBrian + intro;
                break;
        }
        return intro;
    }


    @RequestMapping(path = "/user-action", method = RequestMethod.POST)
    public String userAction(@RequestBody String userAction, HttpSession session) {

        String userName = (String) session.getAttribute("username");
        String action = userAction.toLowerCase();
        String response;
        boolean frontDoor = false;

        switch (action) {
            case "help":
            case "halp":
            case "wtf":
            case "commands":
                response =
                        "---<br>" +
                                "Useful commands include things like:</br></br>" +
                                "\"Look around.\"</br>" +
                                "\"Move to (location)\" i.e. \"front door.\"</br>" +
                                "\"Inspect (item)\" i.e. \"doorknob.\"</br>" +
                                "\"Inventory\"</br>" +
                                "\"Open (item)\" i.e. \"trashcan.\"</br>" +
                                "\"About\"</br></br>" +
                                "You are encouraged to experiment.</br></br>" +
                                "Enter \"save\" to save and quit.</br>" +
                                "Closing your browser before saving could cause you to lose your progress.</br></br>" +
                                "---</br>";
                break;

            case "about":
            case "detective sara":
            case "about detective sara":
                response = "You are currently logged in as " + userName + ".</br>" +
                        "---</br>" +
                        "Detective Sara was programmed and written by Eric Weidman.</br>" +
                        "If you're reading this, thank you for playing!</br>" +
                        "If you have any questions/comments/feedback/criticisms/devjobs,</br>" +
                        "I would love to hear from you! Shoot me an email - ericweidman@gmail.com.</br>" +
                        "All code for this game can be found at https://github.com/ericweidman/TextDetective</br>";

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

            case "item":
            case "inventory":
            case "items":
                response = "Sara has a keyring.";
                break;

            case "inspect keyring":
            case "inspect keys":
                response = "The keyring she is carrying used to belong to John Mercer. There are two keys attached.</br>" +
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
                        "It's the early afternoon on a Tuesday, most of the driveways are empty.</br> " +
                        "The normal folk who live here are at work.</br>" +
                        "All of the houses are rather plain looking, every nth house or so are seemingly identical.</br>"+
                        "The uninteresting house closest to her, she knows to be empty. Her cruiser is parked in the driveway.</br>" +
                        "What is left of her sense of duty beckons her to the front door.";
                break;

            case "inspect":
                response = "Sara wonders what kind of things she'll get to inspect today.";
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
                        "He did it shorty after a search warrant for his house had been issued.</br>" +
                        "The same house Sara was headed to conduct a search on.</br><br>" +

                        "She thought the suicide a bit odd, Johnathan had been in and out the county jail more times than she could remember.</br>" +
                        "His slimy lawyer normally didn't have any trouble getting him out.</br>" +
                        "Typically one of her idiot coworkers would forget how to book him properly, or forget to read him his rights,</br>" +
                        " or beat the ever loving shit out of him before bringing him in.</br></br>" +

                        "Her fellow officers didn't give the suicide a second thought, in fact they celebrated it.</br>" +
                        "While she didn't have any respect for Johnathan, she saw no reason to celebrate another addition to" +
                        " the long list of her coworkers fuck-ups.</br>" +
                        "She hates everyone in her precinct.</br></br>" +

                        "Her final thought before she reached the door was \"lucky bastard.\"</br>"+
                        "She chuckled to herself.";
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

            default:
                response = "Unrecognized command.";
                break;
        }
        return response;
    }
}
