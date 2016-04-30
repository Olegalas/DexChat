package ua.dexchat.client.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by dexter on 30.04.16.
 */
@Controller
public class ClientController {

    private final static Logger LOGGER = Logger.getLogger(ClientController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String firstPage(Model model) {

        LOGGER.debug("***Enter in firstPage method");

        return "start";
    }

}
