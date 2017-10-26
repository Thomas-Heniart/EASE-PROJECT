package com.Ease.Utils;

import org.json.simple.JSONObject;

public class HttpServletException extends Exception {
    private static final long serialVersionUID = 1L;
    protected String msg;
    protected JSONObject jsonObject;
    private HttpStatus httpStatus;

    public HttpServletException(HttpStatus httpStatus, String msg) {
        this.msg = msg;
        this.httpStatus = httpStatus;
        System.out.println(msg);
    }

    public HttpServletException(HttpStatus httpStatus, JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        this.httpStatus = httpStatus;
    }

    public HttpServletException(HttpStatus httpStatus, Exception e) {
        this.msg = e.toString() + ".\nStackTrace :";
        for (int i = 0; i < e.getStackTrace().length; i++) {
            this.msg += "\n" + e.getStackTrace()[i];
        }
        System.out.println(msg);
        this.httpStatus = httpStatus;
    }

    public HttpServletException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        if (this.httpStatus == HttpStatus.InternError)
            this.msg = "Darn - that didn’t work. Feel free to send Thomas at thomas@ease.space";
        else
            this.msg = null;
    }

    public String getMsg() {
        return msg;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
