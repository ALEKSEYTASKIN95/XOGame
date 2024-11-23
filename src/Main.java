import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        TicTacToe mainWindow = new TicTacToe("TicTacToe", "XO.png", 400,400);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setResizable(true);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);
    }
}
