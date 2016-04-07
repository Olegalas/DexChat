package ua.dexchat.server.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by dexter on 04.04.16.
 */
@Aspect
@Component
public class PublicMethodsWrapper {

    private static Logger LOGGER = Logger.getLogger(PublicMethodsWrapper.class);

    @Before(value = "publicModelMethodsPointCut()")
    public void loggingPublicMethodsAdvice(){
        LOGGER.info("***public method of model was ended");
    }

    @Pointcut(value = "execution(public * ua.dexchat.server..*(..))")
    public void publicServerMethodsPointCut(){}

    @Pointcut(value = "execution(public * ua.dexchat.model..*(..))")
    public void publicModelMethodsPointCut(){}

    @Around(value = "publicServerMethodsPointCut()")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint){

        String methodName = proceedingJoinPoint.getSignature().getName();
        LOGGER.info("***" + methodName + " method was started");

        Object value = null;

        try {
            value =  proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            LOGGER.error("***" + methodName + " method threw exception");
            LOGGER.error(throwable.getClass().getName());
            LOGGER.error(throwable.getMessage());
        }

        LOGGER.info("***" + methodName + " method was ended");

        return value;
    }

}
