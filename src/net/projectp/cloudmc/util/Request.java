package net.projectp.cloudmc.util;

public abstract class Request<C extends RequestCallback> {

    protected final C callback;

    public Request(C callback) {
        this.callback = callback;
    }

    public C getCallback() {
        return callback;
    }

}
