package de.enwaffel.cloudmc;

public abstract class B {

    protected final CloudSystem cloud;

    public B(CloudSystem cloud) {
        this.cloud = cloud;
    }

    public CloudSystem getCloud() {
        return cloud;
    }

}
