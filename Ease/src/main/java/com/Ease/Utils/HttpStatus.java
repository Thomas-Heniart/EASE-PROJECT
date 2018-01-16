package com.Ease.Utils;

/**
 * Created by thomas on 01/06/2017.
 */
public enum HttpStatus {
    Success(200),
    BadRequest(400),
    AccessDenied(401),
    PaymentRequired(402),
    Forbidden(403),
    InternError(500);

    private final int status;

    HttpStatus(int status) {
        this.status = status;
    }

    public int getValue() {
        return status;
    }
}
