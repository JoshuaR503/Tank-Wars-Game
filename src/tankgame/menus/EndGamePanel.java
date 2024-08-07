package tankgame.menus;

import tankgame.Launcher;
import tankgame.game.SoundManager;

import javax.swing.*;
import java.awt.*;

public class EndGamePanel extends JPanel {

    private final Launcher lf;

    public EndGamePanel(Launcher lf, String winner, SoundManager soundManager) {

        System.out.println("Winner: " + winner);

        soundManager.playBackgroundMusic(winner.equals("1") ? "ladygaga" : "hotline");

        String message = "<html>RED TANK WINS. The Lore: from the gritty streets of Culiacán<br>Red tank emerged victorious.<br>" +
                "This win is for those who love corridos and have a bellicose spirit.</html>";


        String message1 = "<html>BLUE TANK WINS. Hailing from serene and peaceful lands," +
                "<br>this win is for those who love peace, avoid conflict," +
                "<br>those who view life through a lens of tranquility and harmony.</html>";

        this.lf = lf;

        this.setBackground(Color.BLACK);
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel winnerLabel = new JLabel(winner.equals("1") ? message : message1);
        winnerLabel.setFont(new Font("Serif", Font.ITALIC, 36));
        winnerLabel.setForeground(Color.WHITE);
        winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton start = new JButton("Restart Game");
        start.setFont(new Font("Courier New", Font.BOLD, 24));
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.addActionListener((actionEvent -> this.lf.setFrame("game")));

        JButton exit = new JButton("Exit");
        exit.setFont(new Font("Courier New", Font.BOLD, 24));
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.addActionListener((actionEvent -> this.lf.closeGame()));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(start);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(exit);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(winnerLabel, gbc);

        gbc.gridy = 1;
        this.add(buttonPanel, gbc);
    }
}
