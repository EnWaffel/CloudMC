package net.projectp.cloudmc.command;

import net.projectp.cloudmc.util.result.Result;

public interface SequenceCallback {
    Result call(int i, String text);
}
