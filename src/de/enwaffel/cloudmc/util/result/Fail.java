package de.enwaffel.cloudmc.util.result;

public class Fail implements Result {

    private final String message;

    public Fail(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
