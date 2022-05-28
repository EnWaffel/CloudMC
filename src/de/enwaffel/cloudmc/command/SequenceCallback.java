package de.enwaffel.cloudmc.command;

import de.enwaffel.cloudmc.util.result.Result;

public interface SequenceCallback {
    Result call(int i, String text);
}
