import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Çiftlik Yönetimi Oyunu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FarmGrid farmGrid = new FarmGrid();
        JScrollPane scrollPane = new JScrollPane(farmGrid);
        scrollPane.setPreferredSize(new java.awt.Dimension(800, 600));

        frame.add(scrollPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        farmGrid.requestFocusInWindow();
    }
}
