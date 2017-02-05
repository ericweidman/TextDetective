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

        String intro = "Sara Berkeley found herself standing outside of a decidedly unremarkable Oakland home.</br>" +
                "It was a typical California spring day, warm and beautiful.</br>Although she tended not to notice such things anymore.</br>" +
                "She is here to do a job. A job she'd done for years, and one she is rather good at.</br>" +
                "She is a detective for the Oakland Police Department. It is also a job she despises.</br>" +
                "She let out a long sigh. </br>\"Well, let's get this over with\" she muttered.</br></br>" +
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
    public String userAction(@RequestBody String userAction) {

        String action = userAction.toLowerCase();
        String response;

        switch (action) {
            case "help":
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
                response = "This game was created by Eric Weidman.</br>" +
                        "If you're reading this, thank you so much for playing!</br>" +
                        "If you have any questions/comments/feedback/criticisms/devjobs,</br>" +
                        "I would love to hear from you! Shoot me an email ericweidman@gmail.com.</br>" +
                        "All code for this game can be found at https://github.com/ericweidman/TextDetective</br>";
                break;

            case "kill self":
            case "kill yourself":
                response = "Sara considers killing herself for brief a moment. She decided now is not the time.";
                break;

            case "inventory":
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

            case "look around":
                response = "Sara looks around. She is standing on the sidewalk in an average Oakland neighborhood.</br>" +
                        "Every nth house looked pretty much the same. Most of the driveways were empty. It is " +
                        "the early afternoon on a Tuesday, presumably everyone is still at work.</br>" +
                        "The house closest to her she knows to be empty, it also looks as much.";
                break;

            case "open trashcan":
                response = "There is no trashcan nearby for Sara to open.";
                break;

            case "inspect trashcan":
                response = "There is no trashcan nearby for Sara to inspect.";
                break;

            case "inspect doorknob":
                response = "Sara can hardly see the doorknob from where she is standing.</br>";
                break;

            case "open doorknob":
                response ="Sara cannot reach the doorknob. She is getting a bit ahead of herself.";
                break;

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
