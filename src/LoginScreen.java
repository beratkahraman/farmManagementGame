import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen {
    private JFrame frame;

    public LoginScreen() {
        // Kullanıcı adı ve şifre
        final String predefinedUsername = "admin";
        final String predefinedPassword = "12345";

        // Giriş ekranı penceresi
        frame = new JFrame("Çiftlik Yönetimi - Giriş");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(null); // Özel yerleşim düzeni

        // Arka plan resmi
        JLabel background = new JLabel(new ImageIcon("resources/images/login_background.png"));
        background.setBounds(0, 0, 600, 400);
        frame.setContentPane(background);

        // Başlık
        JLabel title = new JLabel("Çiftlik Yönetimi Oyunu", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBounds(100, 20, 400, 50); // Konum ve boyut
        frame.add(title);

        // Kullanıcı adı etiketi ve alanı
        JLabel usernameLabel = new JLabel("Kullanıcı Adı:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(150, 100, 120, 30);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(280, 100, 170, 30);

        // Şifre etiketi ve alanı
        JLabel passwordLabel = new JLabel("Şifre:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(150, 150, 120, 30);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(280, 150, 170, 30);

        // Giriş butonu
        JButton loginButton = new JButton("Giriş Yap");
        loginButton.setBounds(230, 220, 150, 40);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(50, 205, 50)); // Yeşil buton
        loginButton.setForeground(Color.WHITE);

        // Giriş işlemi
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredUsername = usernameField.getText();
                String enteredPassword = new String(passwordField.getPassword());

                // Kullanıcı adı ve şifre kontrolü
                if (enteredUsername.equals(predefinedUsername) && enteredPassword.equals(predefinedPassword)) {
                    JOptionPane.showMessageDialog(frame, "Giriş Başarılı! Oyuna Hoş Geldiniz.");
                    frame.dispose(); // Giriş ekranını kapat
                    new Main().startGame(); // Oyunu başlat
                } else {
                    JOptionPane.showMessageDialog(frame, "Hatalı kullanıcı adı veya şifre!", "Giriş Hatası", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Bileşenleri pencereye ekle
        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(loginButton);

        // Pencereyi ortala ve göster
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
