import GameWindow.*;
import javax.swing.*;

//make UML Ctrl+Alt+Shift+U
public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Mesterul Grigore");

        Game Jocul = new Game();
        window.add(Jocul);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        Jocul.startGameThread();
    }


}