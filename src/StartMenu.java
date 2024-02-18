import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu extends JPanel {

    private static JRadioButton twoPlayers;
    private static JRadioButton threePlayers;
    private static JTextField moneyField;
    private static JTextField pointsField;
    public static boolean playerChoice; // 0 - two players, 1 - three players

    public StartMenu() {
        setLayout(new MigLayout("",
                "[grow]",
                "[grow]"));
        add(setupStartMenuPanel(), "center center");
    }

    public JPanel setupStartMenuPanel() {
        JPanel StartMenuPanel = new JPanel(new MigLayout("gapy 13",
                "[center][right][left][c]",
                "[top][center][b]"));

        // Player count selection

        JLabel playersLabel = GUIUtils.createTextFieldLabel("How many players?");
        twoPlayers = new JRadioButton("2 players");
        twoPlayers.setFont(new Font("Calibri", Font.PLAIN, 17));
        threePlayers = new JRadioButton("3 players");
        threePlayers.setFont(new Font("Calibri", Font.PLAIN, 17));

        // Adding Radio Buttons to a ButtonGroup, so you can only make one selection

        ButtonGroup RadioButtonsGroup = new ButtonGroup();
        RadioButtonsGroup.add(twoPlayers);
        RadioButtonsGroup.add(threePlayers);

        ///////////////////

        JLabel howMuchMoney = GUIUtils.createTextFieldLabel("How much money you all start with?");
        moneyField = GUIUtils.createTextField("Type in money...", new GUIUtils.NumericAndLengthFilter(0, false, true));

        JLabel howManyPoints = GUIUtils.createTextFieldLabel("How many points do you need to win?");
        pointsField = GUIUtils.createTextField("Type in points...", new GUIUtils.NumericAndLengthFilter(0, false, true));

        // Adding buttons

        JButton submitButton = GUIUtils.createButton("Start Game", new SubmitButtonActionListener());
        JButton backButton = GUIUtils.createButton("Back", new GUIUtils.BackButtonAction());

        // Adding everything to the panel

        StartMenuPanel.add(playersLabel, "wrap");
        StartMenuPanel.add(twoPlayers, "wrap");
        StartMenuPanel.add(threePlayers, "wrap");
        StartMenuPanel.add(howMuchMoney, "wrap");
        StartMenuPanel.add(moneyField, "wrap");
        StartMenuPanel.add(howManyPoints, "wrap");
        StartMenuPanel.add(pointsField, "wrap");
        StartMenuPanel.add(submitButton, "wrap");
        StartMenuPanel.add(backButton);

        return StartMenuPanel;
    }

    private static class SubmitButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if ((!twoPlayers.isSelected() && !threePlayers.isSelected()) || moneyField.getText().isEmpty() || pointsField.getText().isEmpty()) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Check again, something is missing", "WRONG!", JOptionPane.ERROR_MESSAGE);
            } else if (twoPlayers.isSelected()) {
                Toolkit.getDefaultToolkit().beep();
                int result = JOptionPane.showConfirmDialog(null, "Are you sure about these settings?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (result == 0) {
                    Integer money = Integer.parseInt(moneyField.getText());
                    Integer points = Integer.parseInt(pointsField.getText());
                    Player playerOne = new Player(money, points, 1);
                    Player playerTwo = new Player(money, points, 2);
                    Game.players.offerLast(playerOne);
                    Game.players.offerLast(playerTwo);
                    playerChoice = false;
                    Interface.rootPanel.add(new Game(), "Game");
                    ((CardLayout) Interface.rootPanel.getLayout()).show(Interface.rootPanel, "Game");
                }
            } else if (threePlayers.isSelected()) {
                Toolkit.getDefaultToolkit().beep();
                int result = JOptionPane.showConfirmDialog(null, "Are you sure about these settings?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (result == 0) {
                    Integer money = Integer.parseInt(moneyField.getText());
                    Integer points = Integer.parseInt(pointsField.getText());
                    Player playerOne = new Player(money, points, 1);
                    Player playerTwo = new Player(money, points, 2);
                    Player playerThree = new Player(money, points, 3);
                    Game.players.offerLast(playerOne);
                    Game.players.offerLast(playerTwo);
                    Game.players.offerLast(playerThree);
                    playerChoice = true;
                    Interface.rootPanel.add(new Game(), "Game");
                    ((CardLayout) Interface.rootPanel.getLayout()).show(Interface.rootPanel, "Game");
                }
            }
        }
    }
}
