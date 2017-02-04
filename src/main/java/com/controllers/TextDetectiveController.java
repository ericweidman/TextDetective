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

        if (newUser.getUserName() == null) {
            throw new Exception("No username entered");
        } else if (checkUser == null) {
            session.setAttribute("username", newUser.getUserName());
            users.save(saveUser);
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
        public String intro(){
            return "Did this get appended properly?";
        }


    @RequestMapping(path = "/user-action", method = RequestMethod.POST)
    public String userAction(@RequestBody String userAction) {

        String response;
        System.out.println(userAction);

        switch (userAction) {
            case "1":
                response = "Option 1";
                break;
            case "2":
                response = "Option 2";
                break;
            default:
                response = "Option 3";
                break;
        }
        System.out.println(response);
        return response;
    }
}
