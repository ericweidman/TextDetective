package com.entities;

import com.services.SaveDataRepository;
import com.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by ericweidman on 2/9/17.
 */
@RestController
public class Intro {

    @Autowired
    UserRepository users;

    @Autowired
    SaveDataRepository saveData;

    public Intro() {}
    public String getReturnString() {
        return returnString;
    }
    private String returnString;


    public String intro(HttpSession session) {

        String sessionUser = (String) session.getAttribute("username");
        User checkIfNew = users.findByUserName(sessionUser);
        SaveData loadGame = saveData.findByUserId(checkIfNew.getId());
        Boolean isAdmin = checkIfNew.getAdmin();

        if (isAdmin) {
            loadGame = new SaveData(checkIfNew.getId(), "Admin lounge", "Admin Tools", true, checkIfNew);
            saveData.save(loadGame);
            returnString = "Welcome to the admin lounge!</br>" +
                    "Enter \"tools\" to see a list of unique Admin features.";
        } else {

            if (loadGame == null) {
                loadGame = new SaveData(checkIfNew.getId(), "sidewalk", "Sara has a keyring.</br>", false, checkIfNew);
                saveData.save(loadGame);
            }

            String brian = (String) session.getAttribute("username");
            String isBrian = brian.toLowerCase();
            String hiBrian = "Hi Brian! I suspect you might be one of the only people who plays this.</br>" +
                    "I wrote this switch statement specifically just to say thanks.</br>" +
                    "So, Thanks!</br>" +
                    "---</br></br>";

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

            switch (isBrian) {
                case "brianweston":
                case "mrchozo":
                case "bweston":
                case "brianw":
                case "brian":
                    returnString = hiBrian + returnString;
                    break;
            }
        }
        return returnString;
    }
}
