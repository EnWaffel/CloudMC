package de.enwaffel.cloudmc.jvm;

import de.enwaffel.cloudmc.B;
import de.enwaffel.cloudmc.CloudSystem;
import de.enwaffel.cloudmc.util.result.Fail;
import de.enwaffel.cloudmc.util.result.Result;
import de.enwaffel.cloudmc.util.result.Success;
import de.enwaffel.randomutils.io.InByteBuffer;
import de.enwaffel.randomutils.file.FileOrPath;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class JVM extends B {

    private Process jvmProcess;
    public static int counter = 0;
    //private Thread thread;

    public JVM(CloudSystem cloud) {
        super(cloud);
    }


    public JVM start(FileOrPath fileOrPath, String[] jvmArgs, String... args) {
        try {
            String[] commands = {"java", "-Xmx500M", "-jar", fileOrPath.getFile().getAbsolutePath(), "nogui"};
            ProcessBuilder builder = new ProcessBuilder(commands);
            builder.redirectOutput(new File("" + counter + ".txt"));
            builder.directory(fileOrPath.getFile().getParentFile());
            jvmProcess = builder.start();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        jvmProcess.waitFor();
                        jvmProcess.destroy();
                        if (cloud.isShuttingDown()) {
                            cloud.setServicesToWaitFor(cloud.getServicesToWaitFor()-1);
                        }
                        cancel();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            },0, 1);
            //thread = new Thread(this::runThread);
            //thread.start();
            counter++;
        } catch (Exception e) {
            cloud.getLogger().e("Failed to start jvm.");
            cloud.getLogger().ex(Thread.currentThread(), e);
        }
        return this;
    }

    public Result stop(boolean force) {
        try {
            if (force)
                jvmProcess.destroyForcibly();
            else
                jvmProcess.destroy();
            return new Success();
        } catch (Exception e) {
            return new Fail("Failed to stop jvm.");
        }
    }

    private void runThread() {
        try {
            while (jvmProcess.isAlive()) {
                InByteBuffer buff = new InByteBuffer(jvmProcess.getInputStream());
                System.out.println(new String(buff.getBuffer()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Process getJVM() {
        return jvmProcess;
    }

}
