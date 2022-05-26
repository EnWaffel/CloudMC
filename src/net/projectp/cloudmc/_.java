package net.projectp.cloudmc;

import net.projectp.cloudmc.cloud.CloudSystem;

public abstract class _ {

    protected final CloudSystem cloud;

    public _(CloudSystem cloud) {
        this.cloud = cloud;
    }

    public CloudSystem getCloud() {
        return cloud;
    }

}
