package com.Servlet.Exception;
/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/


import javax.servlet.ServletException;

public class ServletNoPermissionException extends ServletException{
    public ServletNoPermissionException() {
        super();
    }

    public ServletNoPermissionException(String message) {
        super(message);
    }

    public ServletNoPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServletNoPermissionException(Throwable cause) {
        super(cause);
    }
}
