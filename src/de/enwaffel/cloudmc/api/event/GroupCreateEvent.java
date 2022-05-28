package de.enwaffel.cloudmc.api.event;

import de.enwaffel.cloudmc.group.Group;

public class GroupCreateEvent extends Event {

    private final Group group;

    public GroupCreateEvent(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

}