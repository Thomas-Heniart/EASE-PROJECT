package com.ExtensionManager.Exception;
/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

public class ExtensionClosedSocketException extends ExtensionException {
    public ExtensionClosedSocketException() {
        super();
    }

    public ExtensionClosedSocketException(String message) {
        super(message);
    }

    public ExtensionClosedSocketException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtensionClosedSocketException(Throwable cause) {
        super(cause);
    }
}