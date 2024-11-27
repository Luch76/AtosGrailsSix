package com.luch

import com.luch.exception.MyException
import com.luch.spring.security.SpringUserService

abstract class BaseController {

    SpringUserService springUserService;

    def handleException(final Exception exception) {
        String exceptionString = "Exception: ${exception.toString()}. ";
        StackTraceElement[] elements;
        boolean isInstanceOfMyException;

        isInstanceOfMyException = exception instanceof MyException;
        if (isInstanceOfMyException) {
            // No additional text since Exception is known
        } else {
            elements = exception.getStackTrace();
            for (Integer indexE = 0; indexE < elements?.size() && indexE < 10; indexE++) {
                exceptionString = exceptionString + ", " + elements[indexE];
            }
            exceptionString = exceptionString?.length() > 1000 ? exceptionString?.substring(0, 1000) : exceptionString;
            exception.printStackTrace();
        }

        try {
            flash.message = exceptionString;
            render(status: 422, text: exceptionString);
            //response.sendError(499, exceptionString);
        } catch (Exception e) {
            throw exception;
        }
        return;
    }

}
