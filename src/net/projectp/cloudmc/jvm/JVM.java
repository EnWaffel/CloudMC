package net.projectp.cloudmc.jvm;

import de.enwaffel.randomutils.buff.InByteBuffer;
import de.enwaffel.randomutils.file.FileOrPath;
import net.projectp.cloudmc.B;
import net.projectp.cloudmc.CloudSystem;
import net.projectp.cloudmc.util.result.Fail;
import net.projectp.cloudmc.util.result.Result;
import net.projectp.cloudmc.util.result.Success;

public class JVM extends B {

    private Process jvm;
    private Thread thread;

    public JVM(CloudSystem cloud) {
        super(cloud);
    }


    public JVM start(String args, FileOrPath fileOrPath) {
        try {
            ProcessBuilder builder = new ProcessBuilder("java " + args + " -jar " + fileOrPath.getFile().getPath());
            jvm = builder.start();
            thread = new Thread(this::runThread);
        } catch (Exception e) {
            cloud.getLogger().e("Failed to start jvm.");
            cloud.getLogger().ex(Thread.currentThread(), e);
        }
        return this;
    }

    public Result stop(boolean force) {
        try {
            if (force)
                jvm.destroyForcibly();
            else
                jvm.destroy();
            return new Success();
        } catch (Exception e) {
            return new Fail("Failed to stop jvm.");
        }
    }

    private void runThread() {
        try {
            while (jvm.isAlive()) {
                InByteBuffer buff = new InByteBuffer(jvm.getInputStream());
                buff.readFully();
                System.out.println(new String(buff.getBuffer()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
