package de.enwaffel.cloudmc.task;

import de.enwaffel.cloudmc.B;
import de.enwaffel.cloudmc.CloudSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler extends B {

    private final List<Task> queue = new ArrayList<>();
    private final List<Task> activeTasks = new ArrayList<>();

    public Scheduler(CloudSystem cloud) {
        super(cloud);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (queue.size() > 0) {
                    runTask(queue.get(0));
                }
            }
        },0 ,1);
    }

    public void schedule(Task task) {
        queue.add(task);
    }

    private void runTask(Task task) {
        queue.remove(task);
        task.run();
        activeTasks.add(task);
    }

}
