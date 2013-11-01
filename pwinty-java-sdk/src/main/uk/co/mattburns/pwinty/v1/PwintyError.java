package uk.co.mattburns.pwinty.v1;

@SuppressWarnings("serial")
public class PwintyError extends RuntimeException {
    private String error;
    private int code;

    public PwintyError() {
    }

    public PwintyError(String error, int code) {
        this.error = error;
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return code + (error == null ? "" : ": " + error);
    }
}
