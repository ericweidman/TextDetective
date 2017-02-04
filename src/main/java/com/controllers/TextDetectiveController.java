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

        User checkUser = users.findByUserName(newUser.getUserName());
        User saveUser = new User(newUser.getUserName(), newUser.getPin());
        int userCount = (int) users.count() + 1;

        if (newUser.getUserName() == null) {
            throw new Exception("No username entered");
        } else if (checkUser == null) {
            session.setAttribute("username", newUser.getUserName());
            users.save(saveUser);
            System.out.println("User " + newUser.getUserName() + " created!");
            System.out.println("User count is now " + userCount+"!");
        } else if (newUser.getUserName().equals(checkUser.getUserName()) && !newUser.getPin().equals(checkUser.getPin())) {
            throw new Exception("Invalid PIN");
        } else if (newUser.getUserName().equals(checkUser.getUserName()) && newUser.getPin().equals(checkUser.getPin())) {
            session.setAttribute("username", checkUser.getUserName());
        } else {
            session.setAttribute("username", newUser.getUserName());
            users.save(saveUser);
        }

    }

    @RequestMapping(path = "/save-game", method = RequestMethod.POST)
    public void save(HttpSession session) {
        session.invalidate();

    }

    @RequestMapping(path = "/intro", method = RequestMethod.GET)
    public String intro() {
        return "Sara Berkely found herself standing outside of a decidedly unremarkable Oakland home.</br>" +
                "It was a typical California spring day, warm and beautiful.</br>Although she tended not to notice such things anymore.</br>" +
                "She was here to do a job. A job she'd done for years, and one she was rather good at.</br>" +
                "She was a detective for the Oakland Police Department. It was also a job she despised.</br>" +
                "She let out a long sigh. </br>\"Well, let's get this over with\" she muttered.</br></br>" +
                "You may type 'help' at any time.";
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
                                "\"Open (item)\" i.e. \"trashcan.\"</br></br>" +
                                "You are encouraged to experiment.</br>" +
                                "---</br>";
                break;

            case "kill self":
            case "kill yourself":
                response = "Sara considered killing herself for brief a moment. She decided now was not the time.";
                break;

            case "look around":
                response = "Sara looked around. She was standing on the sidewalk in an average Oakland neighborhood.</br>" +
                        "Every nth house looked pretty much the same. Most of the driveways were empty. It was " +
                        "the early afternoon on a Tuesday, presumably everyone was still at work.</br>" +
                        "The house she was standing in front of she knew to be empty, it also looked as such.";
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
