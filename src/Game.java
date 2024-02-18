import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayDeque;
import java.util.Deque;

public class Game extends JPanel {

    public static Deque<Player> players = new ArrayDeque<>();
    private int diceValue;

    public Game() {
        add(setupDice());
        add(setupPlayerInfoPanel());
    }

    private JLabel setupDice() {
        JLabel dice = new JLabel();
        dice.setIcon(new ImageIcon(new ImageIcon("data/dice_1.png").getImage().getScaledInstance(105, 100, Image.SCALE_DEFAULT)));
        dice.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                diceValue = (int)(Math.random() * 6) + 1;

                switch (diceValue) {
                    case 1:
                        dice.setIcon(new ImageIcon(new ImageIcon("data/dice_1.png").getImage().getScaledInstance(105, 100, Image.SCALE_DEFAULT)));
                        break;
                    case 2:
                        dice.setIcon(new ImageIcon(new ImageIcon("data/dice_2.png").getImage().getScaledInstance(105, 100, Image.SCALE_DEFAULT)));
                        break;
                    case 3:
                        dice.setIcon(new ImageIcon(new ImageIcon("data/dice_3.png").getImage().getScaledInstance(105, 100, Image.SCALE_DEFAULT)));
                        break;
                    case 4:
                        dice.setIcon(new ImageIcon(new ImageIcon("data/dice_4.png").getImage().getScaledInstance(105, 100, Image.SCALE_DEFAULT)));
                        break;
                    case 5:
                        dice.setIcon(new ImageIcon(new ImageIcon("data/dice_5.png").getImage().getScaledInstance(105, 100, Image.SCALE_DEFAULT)));
                        break;
                    case 6:
                        dice.setIcon(new ImageIcon(new ImageIcon("data/dice_6.png").getImage().getScaledInstance(105, 100, Image.SCALE_DEFAULT)));
                        break;
                }

            }
        });
        return dice;
    }

    private JPanel setupPlayerInfoPanel() {
        JPanel playerInfoPanel = new JPanel();
        if (!StartMenu.playerChoice) {
            // Sutvarkom pirmojo žaidėjo informaciją
            Player playerInfo = players.peekFirst();
            assert playerInfo != null;

            Integer playerOneMoneyValue = playerInfo.getMoney();

            JPanel playerOnePanel = new JPanel(new MigLayout("",
                    "[][grow, fill][]",
                    ""));
            JLabel playerOneIcon = new JLabel();
            playerOneIcon.setIcon(new ImageIcon(new ImageIcon("data/player.png").getImage().getScaledInstance(93, 93, Image.SCALE_DEFAULT)));
            JLabel playerOneTitle = GUIUtils.createTextFieldLabel("Player One");
            JLabel playerOneMoneyLabel = new JLabel("Money: ");
            playerOneMoneyLabel.setFont(new Font("Calibri", Font.BOLD, 16));
            JLabel playerOneMoneyValueLabel = new JLabel(String.valueOf(playerOneMoneyValue));
            playerOneMoneyValueLabel.setFont(new Font("Calibri", Font.PLAIN, 16));
            JLabel playerOneDollarSign = new JLabel (" $");
            playerOneDollarSign.setFont(new Font("Calibri", Font.BOLD, 16));
            playerOnePanel.add(playerOneTitle, "alignx 7px, cell 0 1");
            playerOnePanel.add(playerOneIcon, "cell 0 2");
            playerOnePanel.add(playerOneMoneyLabel, "gapy 5px, alignx 5px, cell 0 3");
            playerOnePanel.add(playerOneMoneyValueLabel, "cell 0 3");
            playerOnePanel.add(playerOneDollarSign, "cell 0 3");

            // Sutvarkom antrojo zaidejo informacija

            Integer playerTwoMoneyValue = playerInfo.getMoney();

            JPanel playerTwoPanel = new JPanel(new MigLayout("",
                    "[][grow, fill][]",
                    ""));
            JLabel playerTwoIcon = new JLabel();
            playerTwoIcon.setIcon(new ImageIcon(new ImageIcon("data/player.png").getImage().getScaledInstance(93, 93, Image.SCALE_DEFAULT)));
            JLabel playerTwoTitle = GUIUtils.createTextFieldLabel("Player Two");

            JLabel playerTwoMoneyLabel = new JLabel("Money: ");
            playerTwoMoneyLabel.setFont(new Font("Calibri", Font.BOLD, 16));
            JLabel playerTwoMoneyValueLabel = new JLabel(String.valueOf(playerTwoMoneyValue));
            playerTwoMoneyValueLabel.setFont(new Font("Calibri", Font.PLAIN, 16));
            JLabel playerTwoDollarSign = new JLabel (" $");
            playerTwoDollarSign.setFont(new Font("Calibri", Font.BOLD, 16));

            playerTwoPanel.add(playerTwoTitle, "alignx 7px, cell 0 1");
            playerTwoPanel.add(playerTwoIcon, "cell 0 2");
            playerTwoPanel.add(playerTwoMoneyLabel, "gapy 5px, alignx 5px, cell 0 3");
            playerTwoPanel.add(playerTwoMoneyValueLabel, "cell 0 3");
            playerTwoPanel.add(playerTwoDollarSign, "cell 0 3");

            playerInfoPanel.add(playerOnePanel);
            playerInfoPanel.add(playerTwoPanel);
        }
        return playerInfoPanel;
    }
}
