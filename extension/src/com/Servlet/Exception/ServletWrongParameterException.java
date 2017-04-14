package com.Servlet.Exception;
/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

import javax.servlet.ServletException;

public class ServletWrongParameterException extends ServletException {
    public ServletWrongParameterException() {
        super();
    }

    public ServletWrongParameterException(String message) {
        super(message);
    }

    public ServletWrongParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServletWrongParameterException(Throwable cause) {
        super(cause);
    }
}