package ua.dexchat.server.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by dexter on 10.04.16.
 */
public class GetSpringContext {

    private static final ApplicationContext context =
            new ClassPathXmlApplicationContext("/spring-context.xml");

    public static ApplicationContext getContext(){
        return context;
    }
}
