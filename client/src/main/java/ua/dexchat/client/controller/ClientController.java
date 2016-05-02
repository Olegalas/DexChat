package ua.dexchat.client.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dexter on 30.04.16.
 */
@Controller
public class ClientController {

    private final static Logger LOGGER = Logger.getLogger(ClientController.class);
    private final static String COOKIE_NAME = "DexChat";
    private final static String COOKIE_VALUE = "Online";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String firstPage(HttpServletResponse response, HttpServletRequest request) {

        LOGGER.debug("***Enter in firstPage method");

        if (checkCookies(request)) return "home";

        return "start";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(HttpServletResponse response, HttpServletRequest request) {

        LOGGER.debug("***Enter in registration method");


        return "registration";
    }

    private boolean checkCookies(HttpServletRequest request) {
        if(request != null){

            Cookie[] cookies =  request.getCookies();
            if(cookies != null){
                for(Cookie cookie : cookies){

                    if(COOKIE_NAME.equals(cookie.getName()) && COOKIE_VALUE.equals(cookie.getValue())){


                        return true;
                    }

                }
            }
        }
        return false;
    }

}
