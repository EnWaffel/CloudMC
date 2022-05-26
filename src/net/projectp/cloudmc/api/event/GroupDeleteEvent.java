package net.projectp.cloudmc.api.event;

import net.projectp.cloudmc.group.Group;

public class GroupDeleteEvent extends Event {

    private final Group group;

    public GroupDeleteEvent(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

}