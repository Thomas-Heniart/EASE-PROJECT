package com.ExtensionManager.Exception;
/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

public class ExtensionMessageNotSendedException extends ExtensionException {
    public ExtensionMessageNotSendedException() {
        super();
    }

    public ExtensionMessageNotSendedException(String message) {
        super(message);
    }

    public ExtensionMessageNotSendedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtensionMessageNotSendedException(Throwable cause) {
        super(cause);
    }
}
