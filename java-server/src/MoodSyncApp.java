import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.io.File;

public class MoodSyncApp extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private String currentUser = "";
    private String currentMood = "";
    private String languageFilter = "All";
    
    // Colors from index.css
    private final Color BG_DARK = new Color(11, 11, 11);
    private final Color PRIMARY = new Color(139, 0, 0);
    private final Color TEXT_BEIGE = new Color(245, 245, 220);
    private final Color CARD_BG = new Color(26, 26, 26);

    public MoodSyncApp() {
        setTitle("MoodSync - Harmony in every beat");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BG_DARK);

        cardPanel.add(createLoginView(), "login");
        cardPanel.add(new JPanel(), "quiz"); // Placeholder
        cardPanel.add(new JPanel(), "dashboard"); // Placeholder

        add(cardPanel);
        cardLayout.show(cardPanel, "login");
    }

    private JPanel createLoginView() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_DARK);
        
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255,255,255,20), 1),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));

        JLabel title = new JLabel("MoodSync");
        title.setFont(new Font("Arial", Font.BOLD, 42));
        title.setForeground(PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Harmony in every beat.");
        subtitle.setForeground(new Color(245, 245, 220, 150));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField emailField = createStyledTextField("you@example.com");
        JPasswordField passField = createStyledPasswordField();

        JButton loginBtn = createStyledButton("Login", true);
        loginBtn.addActionListener(e -> {
            String email = emailField.getText();
            currentUser = email.contains("@") ? email.split("@")[0] : email;
            showQuizView();
        });

        card.add(title);
        card.add(Box.createVerticalStrut(10));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(40));
        card.add(emailField);
        card.add(Box.createVerticalStrut(15));
        card.add(passField);
        card.add(Box.createVerticalStrut(30));
        card.add(loginBtn);

        panel.add(card);
        return panel;
    }

    private void showQuizView() {
        JPanel quizPanel = new JPanel(new GridBagLayout());
        quizPanel.setBackground(BG_DARK);
        
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setPreferredSize(new Dimension(600, 400));
        card.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel qLabel = new JLabel("What lighting matches your current energy?");
        qLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        qLabel.setForeground(TEXT_BEIGE);
        qLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] options = {"Bright and Sunlight", "Soft and Cozy", "Dim and Moody"};
        String[] moods = {"Happy", "Calm", "Sad"};

        card.add(qLabel);
        card.add(Box.createVerticalStrut(40));

        for (int i = 0; i < options.length; i++) {
            final String m = moods[i];
            JButton btn = createStyledButton(options[i], false);
            btn.addActionListener(e -> {
                currentMood = m;
                showDashboardView();
            });
            card.add(btn);
            card.add(Box.createVerticalStrut(15));
        }

        quizPanel.add(card);
        cardPanel.add(quizPanel, "quiz");
        cardLayout.show(cardPanel, "quiz");
    }

    private void showDashboardView() {
        JPanel dashPanel = new JPanel(new BorderLayout());
        dashPanel.setBackground(BG_DARK);
        dashPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_DARK);
        JLabel logo = new JLabel("MOODSYNC");
        logo.setForeground(TEXT_BEIGE);
        logo.setFont(new Font("Arial", Font.BOLD, 18));
        
        JLabel userLbl = new JLabel(currentUser.toUpperCase() + " | MOOD: " + currentMood.toUpperCase());
        userLbl.setForeground(PRIMARY);
        userLbl.setFont(new Font("Arial", Font.BOLD, 12));
        
        header.add(logo, BorderLayout.WEST);
        header.add(userLbl, BorderLayout.EAST);

        // Center - Player
        JPanel playerCard = new JPanel();
        playerCard.setLayout(new BoxLayout(playerCard, BoxLayout.Y_AXIS));
        playerCard.setBackground(CARD_BG);
        playerCard.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, PRIMARY));
        playerCard.setPreferredSize(new Dimension(0, 250));

        JLabel nowPlaying = new JLabel("NOW PLAYING");
        nowPlaying.setForeground(new Color(245, 245, 220, 100));
        nowPlaying.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel songTitle = new JLabel("Select a Song");
        songTitle.setFont(new Font("Arial", Font.BOLD, 32));
        songTitle.setForeground(TEXT_BEIGE);
        songTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        playerCard.add(Box.createVerticalStrut(40));
        playerCard.add(nowPlaying);
        playerCard.add(Box.createVerticalStrut(10));
        playerCard.add(songTitle);

        // Playlist
        JPanel playlistPanel = new JPanel(new BorderLayout());
        playlistPanel.setBackground(BG_DARK);
        JLabel pTitle = new JLabel("Your " + currentMood + " Playlist");
        pTitle.setForeground(TEXT_BEIGE);
        pTitle.setFont(new Font("Arial", Font.BOLD, 20));
        playlistPanel.add(pTitle, BorderLayout.NORTH);

        JPanel songGrid = new JPanel(new GridLayout(0, 3, 20, 20));
        songGrid.setBackground(BG_DARK);
        
        List<Song> songs = DataService.getSongsByMood(currentMood, languageFilter);
        for (Song s : songs) {
            JButton sBtn = new JButton("<html><div style='text-align:left; padding:10px;'>" + s.getTitle() + "<br/><span style='color:gray;'>" + s.getArtist() + "</span></div></html>");
            sBtn.setBackground(new Color(255, 255, 255, 5));
            sBtn.setForeground(TEXT_BEIGE);
            sBtn.setBorder(BorderFactory.createLineBorder(new Color(255,255,255,10)));
            sBtn.setFocusPainted(false);
            sBtn.addActionListener(e -> songTitle.setText(s.getTitle()));
            songGrid.add(sBtn);
        }
        
        JScrollPane scroll = new JScrollPane(songGrid);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_DARK);
        playlistPanel.add(scroll, BorderLayout.CENTER);

        dashPanel.add(header, BorderLayout.NORTH);
        dashPanel.add(playerCard, BorderLayout.CENTER);
        dashPanel.add(playlistPanel, BorderLayout.SOUTH);

        cardPanel.add(dashPanel, "dashboard");
        cardLayout.show(cardPanel, "dashboard");
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField f = new JTextField(placeholder);
        f.setBackground(new Color(255, 255, 255, 10));
        f.setForeground(TEXT_BEIGE);
        f.setCaretColor(TEXT_BEIGE);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255,255,255,20)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        return f;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField f = new JPasswordField();
        f.setBackground(new Color(255, 255, 255, 10));
        f.setForeground(TEXT_BEIGE);
        f.setCaretColor(TEXT_BEIGE);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255,255,255,20)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        return f;
    }

    private JButton createStyledButton(String text, boolean isPrimary) {
        JButton b = new JButton(text);
        b.setBackground(isPrimary ? PRIMARY : new Color(255, 255, 255, 5));
        b.setForeground(TEXT_BEIGE);
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new MoodSyncApp().setVisible(true));
    }
}
