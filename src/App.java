import com.formdev.flatlaf.FlatLightLaf;
import view.LoginView;

public class App {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        new LoginView();
    }
}
