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
            throw new Exception("Invalid PIN");
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
                "So I wrote this switch statement specifically just to say thanks.</br>" +
                "Thanks!</br></br>";

        String intro = "Sara Berkeley finds herself standing outside of a decidedly unremarkable Oakland home.</br>" +
                "It is a typical California spring day, warm and beautiful.</br>Although she tends not to notice such things anymore.</br>" +
                "She is here to do a job. A job she's done for years, and one she is rather good at.</br>" +
                "She is a detective for the Oakland Police Department. It is also a job she despises.</br>" +
                "She lets out a long sigh. </br>\"Well, let's get this over with\" she muttered.</br></br>" +
                "You may type 'help' at any time.";

        switch (isBrian) {
            case "brian":
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

        switch (action) {
            case "help":
            case "halp":
            case "wtf":
                response =
                        "---<br>" +
                                "Useful commands include things like:</br></br>" +
                                "\"Look around.\"</br>" +
                                "\"Move to (location)\" i.e. \"front door.\"</br>" +
                                "\"Inspect (item)\" i.e. \"doorknob.\"</br>" +
                                "\"Inventory\"</br>" +
                                "\"Open (item)\" i.e. \"trashcan.\"</br>" +
                                "\"About\"</br></br>" +
                                "You are encouraged to experiment.</br>" +
                                "---</br>";
                break;

            case "about":
            case "detective sara":
                response = "You are currently logged in as " + userName+ ".</br>" +
                        "This game was created by Eric Weidman.</br>" +
                        "If you're reading this, thank you so much for playing!</br>" +
                        "If you have any questions/comments/feedback/criticisms/devjobs,</br>" +
                        "I would love to hear from you! Shoot me an email ericweidman@gmail.com.</br>" +
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
            case "punch self":
                response = "Sara considers hurting herself. She thinks there will be plenty of time for self loating later.";
                break;

            case "item":
            case "inventory":
            case "items":
                response = "Sara has a keyring.";
                break;

            case "inspect keyring":
            case "inspect keys":
                response = "The keyring she is carrying used to belong to a criminal. There are two keys attached.</br>" +
                        "One of the keys looks like any standard key. It probably unlocks a door.</br>" +
                        "The other key has a large fob attached, displaying a FORD logo.</br>";
                break;

            case "open keys":
            case "open keyring":
                response = "Sara considers opening the key ring, but decides against it as there isn't much of a reason.";
                break;

            case "break keys":
            case "break keyring":
            case "smash keys":
            case "smash keyring":
                response = "Sara would like to break the keyring when she considers its previous owner but she decides she needs it.";
                break;

            case "throw keys":
            case "throw keyring":
                response = "Sara would like to throw the keyring when she considers its previous owner, but she decides she needs it.";
                break;

            case "look around":
                response = "Sara looks around. She is standing on the sidewalk in an average Oakland neighborhood.</br>" +
                        "Every nth house looks pretty much the same. Most of the driveways are empty. It is " +
                        "the early afternoon on a Tuesday, presumably everyone is still at work.</br>" +
                        "The house closest to her she knows to be empty, it also looks as much.";
                break;

            case "inspect":
                response = "Sara wonders what kind of things she'll get to inspect today.";
                break;

            case "open":
                response = "Sara decides she needs something to open, alas she has no ideas.";
                break;

            case "open trashcan":
                response = "There is not a trashcan nearby for Sara to open.";
                break;

            case "inspect trashcan":
                response = "There is no trashcan nearby for Sara to inspect.";
                break;

            case "inspect doorknob":
                response = "Sara can hardly see the doorknob from where she is standing.";
                break;

            case "open doorknob":
            case "open door":
            case "unlock door":
                response = "Sara cannot reach the door from where she is standing on the sidewalk. She is a bit ahead of herself.";
                break;

            case "go to the front door":
            case "move to the front door":
            case "walk to the front door":
            case "go to front door":
            case "move to front door":
            case "walk to front door":
                response = "MORE STORY STUFF HERE.";
                break;

            default:
                response = "Unrecognized command.";
                break;
        }
        return response;
    }
}
