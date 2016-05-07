package ua.dexchat.client.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by dexter on 30.04.16.
 */
@Controller
public class ClientController {

    private final static Logger LOGGER = Logger.getLogger(ClientController.class);
    private final static String COOKIE_NAME = "DexChat";
    private final static String COOKIE_VALUE = "Online";


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String firstPageGet(HttpServletResponse response, HttpServletRequest request) {

        LOGGER.debug("***Enter in firstPageGet method");

        if (checkCookies(request)) return "home";

        return "start";
    }


    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registrationPost(HttpServletResponse response, HttpServletRequest request) {

        LOGGER.debug("***Enter in registrationPost method");

        return "registration";
    }

    @RequestMapping(value = "/do_registration", method = RequestMethod.POST)
    public String doRegistrationPost(HttpServletResponse response, HttpServletRequest request) {

        LOGGER.debug("***Enter in doRegistrationPost method");

        String name = request.getParameter("name");
        String login = request.getParameter("login");
        String email = request.getParameter("email");
        String pass = request.getParameter("pass");

        LOGGER.info("***User was registered : ");
        LOGGER.info("***Login : " + login);
        LOGGER.info("***Name : " + name);
        LOGGER.info("***Email : " + email);
        LOGGER.info("***Pass : " + pass);

        response.setContentType("text/plain");

        try {
            PrintWriter out = response.getWriter();
            out.print("success");
            out.flush();
            out.close();
        } catch (IOException e) {
            LOGGER.error("***Something was wrong during registration", e);
        }

        return "registration";
    }

    @RequestMapping(value = "/home", method = RequestMethod.POST)
    public String homePost(HttpServletResponse response, HttpServletRequest request, Map<String, Object> model) {

        LOGGER.debug("***Enter in homePost method");

        String login = request.getParameter("login");
        String pass = request.getParameter("pass");
        LOGGER.info("***User entered : ");
        LOGGER.info("***Login : " + login);
        LOGGER.info("***Pass : " + pass);

        model.put("login", login);
        model.put("pass", pass);

        return "home";
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
