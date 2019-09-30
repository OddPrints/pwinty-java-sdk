package uk.co.mattburns.pwinty.v2_6;

import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class PwintyError extends RuntimeException {
    @Expose private String errorMessage;
    @Expose private int code;

    public PwintyError() {}

    public PwintyError(String errorMessage, int code) {
        this.errorMessage = errorMessage;
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return code + (errorMessage == null ? "" : ": " + errorMessage);
    }
}
