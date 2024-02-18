import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Objects;

public class AddNewCard extends JPanel {

    private JPanel selectionContainer;
    private JPanel colorTab;
    private JPanel cardInstance;
    private JPanel colorChoicePanel;
    private JLabel positionTextFieldLabel;
    private JTextField titleField;
    private JTextField priceField;
    private JTextField pointsField;
    private JTextField positionField;
    private JComboBox<String> typeSelection;
    private JComboBox<String> colorSelectionBox;
    private boolean propertyCardChosen;
    private boolean typeChosen;
    private boolean cardInstanceCreated;
    private final int borderThickness = 3;
    private final Font titleFont = new Font("Calibri", Font.BOLD, 30);
    private final Font priceFont = new Font("Calibri", Font.BOLD, 27);
    private final Font pointsFont = new Font("Calibri", Font.BOLD, 25);

    public AddNewCard() {
        setLayout(new MigLayout());
        add(createSelectionContainer(), "pos 350px 40px");
        add(createBottomPanel(), "pos 590px 700px");
    }

    private JPanel createCard() {
        cardInstanceCreated = true;

        cardInstance = new JPanel(new MigLayout("width 300px, height 420px, align center,",
                "[]",
                "[grow]"));
        cardInstance.setBackground(new Color(189, 234, 211));
        cardInstance.setBorder(BorderFactory.createLineBorder(Color.BLACK, borderThickness));

        if (propertyCardChosen) {
            colorTab = new JPanel();
            colorTab.setBackground(new Color(189, 234, 211));
            cardInstance.add(colorTab, "width 100px, height max(70px, 10%), north, wrap");
        }

        JLabel cardTitle = new JLabel();
        cardTitle.setFont(titleFont);
        cardTitle.setHorizontalAlignment(SwingConstants.CENTER);
        // Pridedam šioje funkcijoje dėl to, nes kitaip listeneris nežino ar tikrai cardTitle buvo sukurtas
        titleField.getDocument().addDocumentListener(new TextFieldListener(titleField, cardTitle, ""));

        JLabel priceLabel = new JLabel();
        priceLabel.setFont(priceFont);
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        priceField.getDocument().addDocumentListener(new TextFieldListener(priceField, priceLabel, "<font size='6'>PRICE $</font>"));

        JLabel pointsLabel = new JLabel();
        pointsLabel.setFont(pointsFont);
        pointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pointsField.getDocument().addDocumentListener(new TextFieldListener(pointsField, pointsLabel, "<font size='5'>POINTS </font>"));

        cardInstance.add(cardTitle, "north, gaptop 25");
        cardInstance.add(pointsLabel, "dock center");
        cardInstance.add(priceLabel, "south, gapbottom 15");

        return cardInstance;
    }

    private JPanel createSelectionContainer() {
        selectionContainer = new JPanel(new MigLayout("height 600px, gapy 8, aligny center"));

        selectionContainer.add(GUIUtils.createTextFieldLabel("Choose type"), "cell 0 1");
        String[] typeChoices = {"None", "Property", "Utility", "Tax"};
        typeSelection = GUIUtils.createASelectionBox(typeChoices);
        typeSelection.addItemListener(new TypeSelectionListener());
        selectionContainer.add(typeSelection, "cell 0 2");

        selectionContainer.add(GUIUtils.createTextFieldLabel("Write a title (Max: 25 characters)"), "cell 0 3");
        titleField = GUIUtils.createTextField("Type in a title...", new GUIUtils.LimitInputLength(25));
        selectionContainer.add(titleField, "cell 0 4");

        selectionContainer.add(GUIUtils.createTextFieldLabel("<html>How many points will it be worth? <br> (Only numbers allowed)</html>"), "cell 0 7");
        pointsField = GUIUtils.createTextField("Type in points...", new GUIUtils.NumericAndLengthFilter(0, false, true));
        selectionContainer.add(pointsField, "cell 0 8");

        selectionContainer.add(GUIUtils.createTextFieldLabel("<html>How much will it cost? <br> (Only numbers allowed)</html>"), "cell 0 9");
        priceField = GUIUtils.createTextField("Type in a price...", new GUIUtils.NumericAndLengthFilter(0, false, true));
        selectionContainer.add(priceField, "cell 0 10");

        positionTextFieldLabel = GUIUtils.createTextFieldLabel("<html>At what position you want to insert it?<br>(Only numbers allowed) <br> Current card count " + DataHandler.cardsCollection.size() + "</html>");
        selectionContainer.add(positionTextFieldLabel, "cell 0 11");
        positionField = GUIUtils.createTextField("Type in position...", new GUIUtils.NumericAndLengthFilter(0, true, false));
        selectionContainer.add(positionField, "cell 0 12");

        return selectionContainer;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton backButton = GUIUtils.createButton("Back", new GUIUtils.CardManagerBack());
        JButton submitButton = GUIUtils.createButton("Submit", new SubmitButtonListener());
        bottomPanel.add(backButton);
        bottomPanel.add(Box.createHorizontalStrut(30));
        bottomPanel.add(submitButton);
        return bottomPanel;
    }

    private void resetTextFields() {
        titleField.setText("");
        priceField.setText("");
        pointsField.setText("");
        positionField.setText("");
    }

    private void changeTabColor(JComponent jComponent, String colorChoice) {
        switch (colorChoice) {
            case "Legendary":
                jComponent.setBackground(new Color(0, 128, 255));
                jComponent.setBorder(BorderFactory.createMatteBorder(0, 0, borderThickness, 0, Color.black));
                break;
            case "Epic":
                jComponent.setBackground(new Color(231, 242, 78));
                jComponent.setBorder(BorderFactory.createMatteBorder(0, 0, borderThickness, 0, Color.black));
                break;
            case "Common":
                jComponent.setBackground(new Color(133, 79, 30));
                jComponent.setBorder(BorderFactory.createMatteBorder(0, 0, borderThickness, 0, Color.black));
                break;
            case "Uncommon":
                jComponent.setBackground(new Color(212, 35, 133));
                jComponent.setBorder(BorderFactory.createMatteBorder(0, 0, borderThickness, 0, Color.black));
                break;
            case "Rare":
                jComponent.setBackground(new Color(60, 38, 129));
                jComponent.setBorder(BorderFactory.createMatteBorder(0, 0, borderThickness, 0, Color.black));
                break;
            case "None":
                jComponent.setBackground(new Color(189, 234, 211));
                jComponent.setBorder(BorderFactory.createEmptyBorder());
                break;
            default:
                break;
        }
    }

    private JPanel createColorChoice() {
        colorChoicePanel = new JPanel(new MigLayout("insets 0"));
        colorChoicePanel.add(GUIUtils.createTextFieldLabel("How rare is it?"), "wrap");
        String[] colorChoices = {"None", "Common", "Uncommon", "Rare", "Epic", "Legendary"};
        colorSelectionBox = GUIUtils.createASelectionBox(colorChoices);
        colorSelectionBox.addActionListener(e -> changeTabColor(colorTab, (String) Objects.requireNonNull(colorSelectionBox.getSelectedItem())));
        colorChoicePanel.add(colorSelectionBox);
        return colorChoicePanel;
    }

    private class TypeSelectionListener implements ItemListener {
        public void itemStateChanged(ItemEvent event) {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                Object source = event.getSource();
                if (source instanceof JComboBox) {
                    @SuppressWarnings("unchecked")
                    JComboBox<String> cb = (JComboBox<String>) source;
                    Object selectedItem = cb.getSelectedItem();
                    if ("None".equals(selectedItem)){
                        typeChosen = false;
                        if (cardInstanceCreated) {
                            remove(cardInstance);
                            cardInstanceCreated = false;
                        }
                        if (propertyCardChosen) {
                            selectionContainer.remove(colorChoicePanel);
                            propertyCardChosen = false;
                        }
                        resetTextFields();
                        revalidate();
                        repaint();
                    } else if ("Property".equals(selectedItem)) {
                        typeChosen = true;
                        propertyCardChosen = true;
                        if (cardInstanceCreated) {
                            remove(cardInstance);
                            cardInstanceCreated = false;
                        }
                        resetTextFields();
                        add(createCard(), "pos 950px 120px");
                        selectionContainer.add(createColorChoice(), "cell 0 6");
                        revalidate();
                        repaint();
                    } else if ("Utility".equals(selectedItem)){
                        typeChosen = true;
                        if (cardInstanceCreated) {
                            remove(cardInstance);
                            cardInstanceCreated = false;
                        }
                        if (propertyCardChosen) {
                            selectionContainer.remove(colorChoicePanel);
                            propertyCardChosen = false;
                        }
                        resetTextFields();
                        add(createCard(), "pos 950px 120px");
                        revalidate();
                        repaint();
                    } else if ("Tax".equals(selectedItem)) {
                        typeChosen = true;
                        if (cardInstanceCreated) {
                            remove(cardInstance);
                            cardInstanceCreated = false;
                        }
                        if (propertyCardChosen) {
                            selectionContainer.remove(colorChoicePanel);
                            propertyCardChosen = false;
                        }
                        resetTextFields();
                        add(createCard(), "pos 950px 120px");
                        revalidate();
                        repaint();
                    }
                }
            }
        }
    }

    private class TextFieldListener implements DocumentListener {

        private final JTextField textField;
        private final JLabel label;
        private final String additionalInformation;

        public TextFieldListener(JTextField textField, JLabel label, String additionalInformation) {
            this.textField = textField;
            this.label = label;
            this.additionalInformation = additionalInformation;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            if (typeChosen) {
                updateLabel();
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            if (typeChosen) {
                updateLabel();
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e){
        }

        private void updateLabel() {
            // Patikrinam ar įvestis nėra tuščia
            if (label == null) {
                System.out.println("Label is null!"); // Debug message
                return;
            }

            String textFieldRetrieval = textField.getText().toUpperCase();
            int maxCharacters = 16; // Koks ilgiausias pavadinimas gali būti

            StringBuilder formattedText = new StringBuilder("<html><center>" + additionalInformation); // Naudojam StringBuilder nes jis geresnis variantas String manipuliacijai negu tvarkyti Stringą tiesiogiai

            for (int i = 0; i < textFieldRetrieval.length(); i++) {
                formattedText.append(textFieldRetrieval.charAt(i)); // Kuriam naują label
                if ((i + 1) % maxCharacters == 0) {
                    formattedText.append("<br");
                }
            }
            formattedText.append("<center><html>");
            label.setText(formattedText.toString());
        }
    }

    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String type = String.valueOf(typeSelection.getSelectedItem()).toLowerCase();
            if (titleField.getText().isEmpty() || pointsField.getText().isEmpty()
                    || positionField.getText().isEmpty() || priceField.getText().isEmpty() || type.equals("none")) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Check again, something is missing", "WRONG!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String colorSelection = "None";
            if (propertyCardChosen) {
                if (colorSelectionBox.getSelectedItem() == "None") {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Check again, something is missing", "WRONG!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else {
                    colorSelection = String.valueOf(colorSelectionBox.getSelectedItem()).toLowerCase();
                }
            }

            String title = titleField.getText();
            int cost = Integer.parseInt(priceField.getText());
            int points = Integer.parseInt(pointsField.getText());
            int position = Integer.parseInt(positionField.getText()) - 1;
            Card cardInstance = new Card(title, cost, points, colorSelection, type);
            DataHandler.cardsCollection.add(position, cardInstance);
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Add operation was successful!", "Success!", JOptionPane.PLAIN_MESSAGE);
            positionTextFieldLabel.setText("<html>At what position you want to insert it?<br>(Only numbers allowed) <br> Current card count " + DataHandler.cardsCollection.size() + "</html>");
            typeSelection.setSelectedIndex(0);
        }
    }
}
