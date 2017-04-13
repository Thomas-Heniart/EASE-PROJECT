package com.Servlet.Exception;
/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

import javax.servlet.ServletException;

public class ServletNotConnectedException extends ServletException {
    public ServletNotConnectedException() {
        super();
    }

    public ServletNotConnectedException(String message) {
        super(message);
    }

    public ServletNotConnectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServletNotConnectedException(Throwable cause) {
        super(cause);
    }
}
