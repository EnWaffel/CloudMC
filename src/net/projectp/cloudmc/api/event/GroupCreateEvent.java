package net.projectp.cloudmc.api.event;

import net.projectp.cloudmc.group.Group;

public class GroupCreateEvent extends Event {

    private final Group group;

    public GroupCreateEvent(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

}