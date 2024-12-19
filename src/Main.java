import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginScreen()); // Giriş ekranını başlat
    }

    public void startGame() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Çiftlik Yönetimi Oyunu");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            FarmGrid farmGrid = new FarmGrid();
            frame.add(farmGrid);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            farmGrid.requestFocusInWindow();
        });
    }
}
