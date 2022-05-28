package de.enwaffel.cloudmc.api.event;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private final List<EventListener> listeners = new ArrayList<>();
    private boolean isCancelled;
    public Event() {}
    public boolean isCancelled() { return isCancelled; }
    public void setCancelled(boolean cancelled) { isCancelled = cancelled; }
    public List<EventListener> getListeners() { return listeners; }
}