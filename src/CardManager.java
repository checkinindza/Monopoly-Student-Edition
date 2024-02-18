import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Objects;
import java.util.Stack;

public class CardManager extends JPanel {

    private JPanel controlPanel;
    private JPanel rootCardDeletionPanel;
    private JComboBox<String> deleteHowSelection;
    private JLabel confirmationMessage;
    private boolean cardDeletionRootCreated = false;

    public CardManager() {
        setLayout(new MigLayout("",
                "[grow]",
                "[grow]"));
        add(setupControlPanel(), "east, gapright 20px, gapleft 20px");
        add(createCardTable(), "dock center");
    }

    private JPanel setupControlPanel() {
        controlPanel = new JPanel(new MigLayout("align 50% 50%, insets 0"));
        JButton AddNewCardButton = GUIUtils.createButton("Add New Card", new GUIUtils.AddButtonAction());
        JButton backButton = GUIUtils.createButton("Back", new GUIUtils.BackButtonAction());
        JButton loadButton = GUIUtils.createButton("Load all", e -> {
            if (DataHandler.dataLoaded) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "List is already loaded!", "Already done", JOptionPane.ERROR_MESSAGE);
            } else {
                DataHandler.readDataFromJSON();
                revalidate();
                repaint();
            }
        });
        controlPanel.add(AddNewCardButton, "cell 0 1");
        controlPanel.add(cardDeletionChoices(), "cell 0 2");
        controlPanel.add(loadButton, "cell 0 5");
        controlPanel.add(backButton, "cell 0 6");
        return controlPanel;
    }

    private JLabel getConfirmationMessage() {
        confirmationMessage = GUIUtils.createTextFieldLabel("Deletion successful!");
        confirmationMessage.setForeground(new Color(46, 204, 113));
        return confirmationMessage;
    }

    private JPanel cardDeletionPanelAfterChoice() {
        rootCardDeletionPanel = new JPanel(new CardLayout());
        JPanel deleteByPos = cardDeletionBySetting("Type in position...");
        JPanel deleteByName = cardDeletionBySetting("Type in name...");
        rootCardDeletionPanel.add(deleteByPos, "DeleteByPos");
        rootCardDeletionPanel.add(deleteByName, "DeleteByName");
        return rootCardDeletionPanel;
    }

    private JPanel cardDeletionChoices() {
        JPanel cardDeletion = new JPanel(new MigLayout("insets 0"));
        JLabel explanation = GUIUtils.createTextFieldLabel("Deletion options: ");
        String[] choices = {"None", "By Position", "By Name", "All"};
        deleteHowSelection = new JComboBox<>(choices);
        deleteHowSelection.setPreferredSize(new Dimension(210, 40));
        deleteHowSelection.setRenderer(new GUIUtils.BorderListCellRenderer(10, 3));
        deleteHowSelection.setFont(new Font("Calibri", Font.BOLD, 17));
        deleteHowSelection.addItemListener(new DeletionChoice());
        cardDeletion.add(explanation, "wrap");
        cardDeletion.add(deleteHowSelection);
        revalidate();
        repaint();
        return cardDeletion;
    }

    private JPanel cardDeletionBySetting(String placeHolder) {
        JPanel cardDeletionBySetting = new JPanel(new MigLayout("insets 0"));

        JTextField cardDeletionField = new JTextField();
        AbstractDocument doc = (AbstractDocument) cardDeletionField.getDocument();
        if (placeHolder.equals("Type in position...")) {
            doc.setDocumentFilter(new GUIUtils.NumericAndLengthFilter(0, true, true));
        } else if (placeHolder.equals("Type in name...")) {
            doc.setDocumentFilter(new GUIUtils.LimitInputLength(25));
        }
        TextPrompt tp = new TextPrompt(placeHolder, cardDeletionField, TextPrompt.Show.FOCUS_LOST);
        tp.setFont(new Font("Calibri", Font.PLAIN, 16));
        cardDeletionField.setFont(new Font("Calibri", Font.PLAIN, 16));
        cardDeletionField.setPreferredSize(new Dimension(168, 40));

        // Kadangi mes norime pasalinti borderi aplink musu ivedimo laukeli
        // Mums reikia sukurti compoundborder, nes setMargin, funkcija,
        // kurios pagalba nustatome, kur bus musu placeholder tekstas,
        // veikia naudojant atstuma nuo laukelio borderio
        // tai sukuriam dviguba borderi, vidini ir isorini
        // ir pagal vidini stumdysim teksta

        Border emptyOutside = BorderFactory.createEmptyBorder();
        Border emptyInside = new EmptyBorder(4, 8, 0, 0);
        CompoundBorder textFieldborder = new CompoundBorder(emptyOutside, emptyInside);
        cardDeletionField.setBorder(textFieldborder);

        ////////////////////////////////////////////////////////////////////

        JButton OKButton = new JButton("OK");
        OKButton.addActionListener(e -> {
            if (cardDeletionField.getText().isEmpty()) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Did you type something?", "Empty field", JOptionPane.ERROR_MESSAGE);
            }
            else {
                String selection = Objects.requireNonNull(deleteHowSelection.getSelectedItem()).toString();
                try {
                    if (selection.equals("By Position")) {
                        DataHandler.cardsCollection.remove((Integer.parseInt(cardDeletionField.getText()) - 1));
                        cardDeletionField.setText("");
                        revalidate();
                        repaint();
                        controlPanel.add(getConfirmationMessage(), "cell 0 4");
                        Timer timer = new Timer(3500, e1 -> {
                            controlPanel.remove(confirmationMessage);
                            revalidate();
                            repaint();
                        });
                        timer.start();
                    }
                    if (selection.equals("By Name")) {
                        String name = cardDeletionField.getText().toLowerCase();
                        int repetitions = 0;
                        Stack<Integer> indexes = new Stack<>();
                        for (int i = 0; i < DataHandler.cardsCollection.size(); i++) {
                            Card card = DataHandler.cardsCollection.get(i);
                            if (card.getName().toLowerCase().equals(name)) {
                                repetitions++;
                                indexes.push(i + 1);
                            }
                        }
                        if (repetitions == 0) {
                            Toolkit.getDefaultToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Are you sure you typed the right name?", "Wrong name", JOptionPane.ERROR_MESSAGE);
                        } else if (repetitions == 1) {
                            int index = (indexes.peek() - 1);
                            DataHandler.cardsCollection.remove(index);
                            cardDeletionField.setText("");
                            revalidate();
                            repaint();
                            controlPanel.add(getConfirmationMessage(), "cell 0 4");
                            Timer timer = new Timer(3500, e12 -> {
                                controlPanel.remove(confirmationMessage);
                                revalidate();
                                repaint();
                            });
                            timer.start();
                        } else if (repetitions >= 2) {
                            ClarificationWindow(indexes);
                            cardDeletionField.setText("");
                            revalidate();
                            repaint();
                        }
                    }
                } catch (IndexOutOfBoundsException error) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "The list is either empty or index is out of bounds", "Something's wrong", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        OKButton.setPreferredSize(new Dimension(40, 40));
        OKButton.setMargin(new Insets(0, 0, 0, 0));

        JPanel TextFieldWithButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
        TextFieldWithButton.setBorder(border);
        TextFieldWithButton.add(cardDeletionField);
        TextFieldWithButton.add(OKButton);

        cardDeletionBySetting.add(TextFieldWithButton);
        return cardDeletionBySetting;
    }

    private void ClarificationWindow (Stack<Integer> Index) {
        // Setting up new frame

        JFrame ClarificationWindowFrame = new JFrame("Clarification");
        ClarificationWindowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ClarificationWindowFrame.setResizable(false);
        ClarificationWindowFrame.setVisible(true);
        ClarificationWindowFrame.setSize(350, 180);

        // Setting up our panel

        JPanel ClarificationPanel = new JPanel(new MigLayout("align 50% 50%"));

        // Setting up data

        Object[] optionsObject = Index.toArray();
        String[] optionsString = Arrays.stream(optionsObject).map(Object::toString).toArray(String[]::new);

        // Setting up our components
        JLabel Text = GUIUtils.createTextFieldLabel("<html>One than more instance found,<br>choose which index you want to delete:</html>");
        JComboBox<String> choices = GUIUtils.createASelectionBox(optionsString);
        JButton ConfirmButton = new JButton("Confirm");
        ConfirmButton.addActionListener(e -> {
            DataHandler.cardsCollection.remove((Integer.parseInt((String) Objects.requireNonNull(choices.getSelectedItem())) - 1));
            ClarificationWindowFrame.dispose();
            revalidate();
            repaint();
        });
        ClarificationPanel.add(Text, "wrap");
        ClarificationPanel.add(choices, "align center, wrap");
        ClarificationPanel.add(ConfirmButton, "align center");
        ClarificationWindowFrame.add(ClarificationPanel);
    }

    private class DeletionChoice implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                Object source = e.getSource();
                if (source instanceof JComboBox<?>) {
                    @SuppressWarnings("unchecked")
                    JComboBox<String> cb = (JComboBox<String>) source;
                    Object selectedItem = cb.getSelectedItem();
                    if ("None".equals(selectedItem)) {
                        if (cardDeletionRootCreated) {
                            controlPanel.remove(rootCardDeletionPanel);
                            cardDeletionRootCreated = false;
                            revalidate();
                            repaint();
                        }
                    } else if ("By Position".equals(selectedItem)) {
                        if (!cardDeletionRootCreated) {
                            controlPanel.add(cardDeletionPanelAfterChoice(), "cell 0 3");
                            cardDeletionRootCreated = true;
                            revalidate();
                            repaint();
                        }
                        CardLayout c1 = (CardLayout)(rootCardDeletionPanel.getLayout());
                        c1.first(rootCardDeletionPanel);
                    } else if ("By Name".equals(selectedItem)) {
                        if (!cardDeletionRootCreated) {
                            controlPanel.add(cardDeletionPanelAfterChoice(), "cell 0 3");
                            cardDeletionRootCreated = true;
                            revalidate();
                            repaint();
                        }
                        ((CardLayout) rootCardDeletionPanel.getLayout()).show(rootCardDeletionPanel, "DeleteByName");
                    } else if ("All".equals(selectedItem)) {
                        if (cardDeletionRootCreated) {
                            controlPanel.remove(rootCardDeletionPanel);
                            cardDeletionRootCreated = false;
                            revalidate();
                            repaint();
                        }
                        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete all the cards?", "Confirmation", JOptionPane.YES_NO_OPTION);
                        if (result == 0) {
                            DataHandler.cardsCollection.clear();
                            revalidate();
                            repaint();
                            DataHandler.dataLoaded = false;
                        }
                    }
                }
            }
        }
    }

    private JPanel createCardTable() {
        JPanel cardTablePanel = new JPanel(new GridLayout(1, 0));
        JTable cardTable = new JTable(new CardTableModel());
        cardTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        cardTable.setFillsViewportHeight(true);
        cardTable.setFocusable(false);
        cardTable.setRowSelectionAllowed(false);
        cardTable.getTableHeader().setResizingAllowed(false);
        cardTable.getColumnModel().getColumn(0).setMaxWidth(50);
        JScrollPane tablePanelPane = new JScrollPane(cardTable);
        cardTablePanel.add(tablePanelPane);
        return cardTablePanel;
    }

    static class CardTableModel extends AbstractTableModel {
        String[] columnNames = { "Index", "Name", "Price", "Points", "Rarity", "Type" };

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return DataHandler.cardsCollection.size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            Card card = DataHandler.cardsCollection.get(row);
            if (card == null) {
                return null;
            }

            return switch (col) {
                case 0 -> row + 1;
                case 1 -> card.getName();
                case 2 -> card.getPrice();
                case 3 -> card.getPoints();
                case 4 -> {
                    if (card.getColor().equals("none")) {
                        yield null;
                    }
                    yield card.getColor();
                }
                case 5 -> card.getType();
                default -> null;
            };
        }
    }
}
