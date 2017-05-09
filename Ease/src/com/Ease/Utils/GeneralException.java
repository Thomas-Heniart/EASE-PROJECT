package com.Ease.Utils;

public class GeneralException extends Exception {
    private static final long serialVersionUID = 1L;
    protected String msg;
    protected ServletManager.Code code;
    protected com.Ease.API.Utils.ServletManager.Code code1;

    public GeneralException(ServletManager.Code code, String msg) {
        this.msg = msg;
        this.code = code;
        this.code1 = null;
        System.out.println(msg);
    }

    public GeneralException(ServletManager.Code code, Exception e) {
        this.msg = e.toString() + ".\nStackTrace :";
        for (int i = 0; i < e.getStackTrace().length; i++) {
            this.msg += "\n" + e.getStackTrace()[i];
        }
        System.out.println(msg);
        this.code = code;
        this.code1 = null;
    }

    public GeneralException(com.Ease.API.Utils.ServletManager.Code code, String msg) {
        this.msg = msg;
        this.code1 = code;
        this.code = null;
        System.out.println(msg);
    }

    public GeneralException(com.Ease.API.Utils.ServletManager.Code code, Exception e) {
        this.msg = e.toString() + ".\nStackTrace :";
        for (int i = 0; i < e.getStackTrace().length; i++) {
            this.msg += "\n" + e.getStackTrace()[i];
        }
        System.out.println(msg);
        this.code1 = code;
        this.code = null;
    }

    public String getMsg() {
        return msg;
    }

    public ServletManager.Code getCode() {
        return code;
    }

    public com.Ease.API.Utils.ServletManager.Code getCode1() {
        return code1;
    }
}
