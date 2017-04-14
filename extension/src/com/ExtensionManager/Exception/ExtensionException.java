package com.ExtensionManager.Exception;
/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

public class ExtensionException extends Exception {
    public ExtensionException() {
        super();
    }

    public ExtensionException(String message) {
        super(message);
    }

    public ExtensionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtensionException(Throwable cause) {
        super(cause);
    }
}