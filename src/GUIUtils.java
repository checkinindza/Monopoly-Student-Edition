import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class GUIUtils {
    private static final Font labelFont = new Font("Calibri", Font.BOLD, 18);
    private static final Font textFieldFont = new Font("Calibri", Font.PLAIN, 16);
    private static final Font comboBoxFont = new Font("Calibri", Font.BOLD, 17);
    private static final Dimension componentSize = new Dimension(260, 40);

    static JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        /* https://stackoverflow.com/questions/7229226/should-i-avoid-the-use-of-setpreferredmaximumminimum-size-methods-in-java-sw/7229519#7229519
            Trumpai tariant, setXXXSize naudoti nereikėtų, tačiau taupant laiką, kad nežaisti su Layout Manager'ių ribojimais, panaudosime.
         */
        button.setPreferredSize(new Dimension(210, 40));
        button.setMaximumSize(new Dimension(210, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(actionListener);
        button.setFont(comboBoxFont);
        button.setMargin(new Insets(5, 0, 0, 0));
        return button;
    }

    public static JLabel createTextFieldLabel(String text) {
        JLabel textFieldLabel = new JLabel(text);
        textFieldLabel.setFont(labelFont);
        return textFieldLabel;
    }

    public static JTextField createTextField(String text, DocumentFilter Filter) {
        JTextField textField = new JTextField();
        AbstractDocument doc = (AbstractDocument) textField.getDocument();
        doc.setDocumentFilter(Filter);
        TextPrompt tp = new TextPrompt(text, textField, TextPrompt.Show.FOCUS_LOST);
        tp.setFont(textFieldFont);
        textField.setFont(textFieldFont);
        textField.setPreferredSize(componentSize);
        textField.setMaximumSize(componentSize);
        textField.setMargin(new Insets(4, 10, 0, 0));
        return textField;
    }

    public static JComboBox<String> createASelectionBox(String[] choices) {
        JComboBox<String> selectionBox = new JComboBox<>(choices);
        selectionBox.setPreferredSize(componentSize);
        selectionBox.setRenderer(new BorderListCellRenderer(10, 3));
        selectionBox.setFont(comboBoxFont);
        return selectionBox;
    }

    // JComboxBox margin reguliavimui

    public static class BorderListCellRenderer implements ListCellRenderer<Object> {

        private final Border insetBorder;

        private final DefaultListCellRenderer defaultRenderer;

        public BorderListCellRenderer(int leftMargin, int topMargin) {
            this.insetBorder = new EmptyBorder(topMargin, leftMargin, 0, 0);
            this.defaultRenderer = new DefaultListCellRenderer();
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            JLabel renderer = (JLabel) defaultRenderer
                    .getListCellRendererComponent(list, value, index, isSelected,
                            cellHasFocus);
            renderer.setBorder(insetBorder);
            return renderer;
        }
    }

    public static class BackButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            CardLayout c1 = (CardLayout)(Interface.rootPanel.getLayout());
            c1.first(Interface.rootPanel);
        }
    }

    public static class AddButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((CardLayout) Interface.rootPanel.getLayout()).show(Interface.rootPanel, "AddNewCardCard");
        }
    }

    public static class CardManagerBack implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            ((CardLayout) Interface.rootPanel.getLayout()).show(Interface.rootPanel, "CardManagerCard");
        }
    }

    public static class LimitInputLength extends DocumentFilter {

        private final int maxCharacters;

        public LimitInputLength(int maxCharacters) {
            this.maxCharacters = maxCharacters;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String text, AttributeSet attributes) throws BadLocationException {
            replace(fb, offset, 0, text, attributes);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            // In case someone tries to clear the Document by using setText(null)

            if (text == null) {
                text = "";
            }

            Document doc = fb.getDocument();
            // If you want to add decimal points, otherwise not really needed
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.replace(offset, offset + length, text);
            if (sb.length() <= maxCharacters) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    public static class NumericAndLengthFilter extends DocumentFilter {
        private final int maxCharacters;
        private final boolean rangeLimit;
        private final boolean onlyNumericFilter;

        public NumericAndLengthFilter(int maxCharacters, boolean rangeLimit, boolean onlyNumericFilter) {
            this.maxCharacters = maxCharacters;
            this.rangeLimit = rangeLimit;
            this.onlyNumericFilter = onlyNumericFilter;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String text, AttributeSet attributes) throws BadLocationException {
            replace(fb, offset, 0, text, attributes);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attributes) throws BadLocationException {
            // In case someone tries to clear the Document by using setText(null)

            if (text == null) {
                text = "";
            }

            // Build the text string assuming the replace of the text is successful

            Document doc = fb.getDocument();
            // If you want to add decimal points, otherwise not really needed
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.replace(offset, offset + length, text);

            if (validReplace(sb.toString())) {
                if (rangeLimit) {
                    super.replace(fb, offset, length, text, attributes);
                }
                else if (onlyNumericFilter) {
                    super.replace(fb, offset, length, text, attributes);
                }
                else if (sb.length() <= maxCharacters) {
                    super.replace(fb, offset, length, text, attributes);
                }
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
        private boolean validReplace(String text) {
            // In case setText("") is used to clear the Document
            if (text.isEmpty()) {
                return true;
            }

            // Verify input is an Integer
            try {
                if (rangeLimit) {
                    int value = Integer.parseInt( text );
                    return value >= 1 && value <= DataHandler.cardsCollection.size();
                } else {
                    Integer.parseInt( text );
                    return true;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }
}
