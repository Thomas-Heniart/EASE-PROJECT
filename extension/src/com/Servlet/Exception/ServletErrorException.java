package com.Servlet.Exception;
/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

import javax.servlet.ServletException;

public class ServletErrorException extends ServletException {
    public ServletErrorException() {
        super();
    }

    public ServletErrorException(String message) {
        super(message);
    }

    public ServletErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServletErrorException(Throwable cause) {
        super(cause);
    }
}
