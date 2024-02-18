import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interface extends JFrame {
    private final CardManager cardManagerReference;
    private final AddNewCard addNewCardReference;
    private final StartMenu StartMenuReference;
    public static JPanel rootPanel;

    public Interface() {
        cardManagerReference = new CardManager();
        addNewCardReference = new AddNewCard();
        StartMenuReference = new StartMenu();
        add(createLogo(), BorderLayout.PAGE_START);
        add(createRootPanel(), BorderLayout.CENTER);
        setSize(1600, 1000);
        setTitle("Monopoly: Student Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private JPanel createLogo() {
        JPanel logoPanel = new JPanel();
        JLabel logoLabel = new JLabel();
        ImageIcon logoImage = new ImageIcon("data/logo.png");
        logoLabel.setIcon(logoImage);
        logoPanel.add(logoLabel);
        return logoPanel;
    }

    private JPanel createRootPanel() {
        rootPanel = new JPanel(new CardLayout());
        rootPanel.add(createMenuCard());
        rootPanel.add(cardManagerReference, "CardManagerCard");
        rootPanel.add(addNewCardReference, "AddNewCardCard");
        rootPanel.add(StartMenuReference, "StartMenu");
        return rootPanel;
    }

    private JPanel createMenuCard() {
        JPanel menuCard = new JPanel();
        menuCard.setLayout(new BoxLayout(menuCard, BoxLayout.Y_AXIS));

        JButton startButton = GUIUtils.createButton("Start", new startButtonAction());
        JButton cardManagerButton = GUIUtils.createButton("Card Manager", new cardManagerButtonAction());
        JButton exitButton = GUIUtils.createButton("Exit", new exitButtonAction());

        menuCard.add(Box.createVerticalGlue());
        menuCard.add(startButton);
        menuCard.add(Box.createRigidArea(new Dimension(0, 10)));
        menuCard.add(cardManagerButton);
        menuCard.add(Box.createRigidArea(new Dimension(0, 10)));
        menuCard.add(exitButton);
        menuCard.add(Box.createVerticalGlue());
        return menuCard;
    }

    private static class startButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((CardLayout) rootPanel.getLayout()).show(rootPanel, "StartMenu");
        }
    }

    private static class cardManagerButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            ((CardLayout) rootPanel.getLayout()).show(rootPanel, "CardManagerCard");
        }
    }

    private static class exitButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            System.exit(0);
        }
    }
}
