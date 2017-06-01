package com.Ease.Utils;

public class HttpServletException extends Exception {
    private static final long serialVersionUID = 1L;
    protected String msg;
    protected ServletManager2.HttpStatus httpStatus;

    public HttpServletException(ServletManager2.HttpStatus httpStatus, String msg) {
        this.msg = msg;
        this.httpStatus = httpStatus;
        System.out.println(msg);
    }

    public HttpServletException(ServletManager2.HttpStatus httpStatus, Exception e) {
        this.msg = e.toString() + ".\nStackTrace :";
        for (int i = 0; i < e.getStackTrace().length; i++) {
            this.msg += "\n" + e.getStackTrace()[i];
        }
        System.out.println(msg);
        this.httpStatus = httpStatus;
    }

    public HttpServletException(ServletManager2.HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.msg = null;
    }

    public String getMsg() {
        return msg;
    }

    public ServletManager2.HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
