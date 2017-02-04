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
    public void newUser(@RequestBody User newUser, HttpSession session) {

        session.setAttribute("username", newUser.getUserName());
        User save = new User(newUser.getUserName(), newUser.getPin());
        users.save(save);
    }

    @RequestMapping(path = "/save-game", method = RequestMethod.POST)
    public void save(HttpSession session) {
        session.invalidate();

    }


    @RequestMapping(path = "userAction", method = RequestMethod.POST)
    public String userAction(@RequestBody String userAction) {

        String response = "Am I seeing this?";


        return response;
    }
}
