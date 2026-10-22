import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class UserQueryCSVDialog {

    private static SimpleMapBST<StringInt, String> ourMap;
    private static String keyTitle = "Key";
    private static String valueTitle = "Value";

    public UserQueryCSVDialog() {
    }

    public UserQueryCSVDialog(File file) {
        if (file != null && file.exists()) {
            ourMap = new SimpleMapBST<>();
            SimpleMapBSTBuilder.readGenericMapFromCSV(ourMap, file.getAbsolutePath());
            keyTitle = SimpleMapBSTBuilder.getKeyTitle();
            valueTitle = SimpleMapBSTBuilder.getValueTitle();
            if (keyTitle == null || keyTitle.isEmpty()) {
                keyTitle = "Key";
            }
            if (valueTitle == null || valueTitle.isEmpty()) {
                valueTitle = "Value";
            }
            SwingBinaryTreeDisplay.showInWindow(ourMap.getTree());
            showQueryDialog(file.getName());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            setGlobalFont(new Font("SansSerif", Font.PLAIN, 16));
            startFileDialog();
        });
    }

    private static void setGlobalFont(Font font) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }

    private static void startFileDialog() {
        File defaultDir = new File("datafiles");
        if (!defaultDir.exists()) {
            defaultDir = new File("DataFiles");
        }

        JFileChooser fileChooser = new JFileChooser(defaultDir.getAbsoluteFile());
        fileChooser.setDialogTitle("Select CSV Input File");

        File defaultFile = new File(defaultDir.getAbsoluteFile(), "Top10SongsEdited.csv");
        if (!defaultFile.exists()) {
            defaultFile = new File(defaultDir.getAbsoluteFile(), "CityAndPopulation.csv");
        }
        if (defaultFile.exists()) {
            fileChooser.setSelectedFile(defaultFile);
        }

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            ourMap = new SimpleMapBST<>();
            SimpleMapBSTBuilder.readGenericMapFromCSV(ourMap, selectedFile.getAbsolutePath());

            keyTitle = SimpleMapBSTBuilder.getKeyTitle();
            valueTitle = SimpleMapBSTBuilder.getValueTitle();
            if (keyTitle == null || keyTitle.isEmpty()) {
                keyTitle = "Key";
            }
            if (valueTitle == null || valueTitle.isEmpty()) {
                valueTitle = "Value";
            }

            SwingBinaryTreeDisplay.showInWindow(ourMap.getTree());
            showQueryDialog(selectedFile.getName());
        }
    }

    private static void showQueryDialog(String fileName) {
        JDialog queryDialog = new JDialog((Frame) null, "CSV Query Dialog - " + fileName, false);
        queryDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        queryDialog.setLayout(new BorderLayout(10, 10));

        Font font16 = new Font("SansSerif", Font.PLAIN, 16);

        // Top Panel: Label, Input Text Field, Submit Button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel label = new JLabel(keyTitle + ": " + "<Enter key>");
        label.setFont(font16);

        JTextField inputField = new JTextField(20);
        inputField.setFont(font16);

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(font16);

        topPanel.add(label);
        topPanel.add(inputField);
        topPanel.add(submitButton);

        // Center Panel: Output JTextArea (wrapping text for display) inside JScrollPane
        JTextArea outputArea = new JTextArea(12, 40);
        outputArea.setFont(font16);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Bottom Panel: Clear Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton clearButton = new JButton("Clear");
        clearButton.setFont(font16);
        bottomPanel.add(clearButton);

        // Action Listener for Submit
        ActionListener submitAction = e -> {
            String userInput = inputField.getText().trim();
            if (userInput.isEmpty()) {
                return;
            }

            String value = (ourMap != null) ? ourMap.get(new StringInt(userInput)) : null;
            String searchOutput;
            if (value != null) {
                searchOutput = "Key: " + userInput + "\nValue: " + value;
            } else {
                searchOutput = "Key not found.";
            }

            if (outputArea.getText().isEmpty()) {
                outputArea.setText("Query: " + userInput + "\n" + searchOutput);
            } else {
                outputArea.append("\n\nQuery: " + userInput + "\n" + searchOutput);
            }
            inputField.selectAll();
        };

        submitButton.addActionListener(submitAction);
        inputField.addActionListener(submitAction);

        // Action Listener for Clear
        clearButton.addActionListener(e -> {
            inputField.setText("");
            outputArea.setText("");
        });

        queryDialog.add(topPanel, BorderLayout.NORTH);
        queryDialog.add(scrollPane, BorderLayout.CENTER);
        queryDialog.add(bottomPanel, BorderLayout.SOUTH);

        queryDialog.pack();
        queryDialog.setLocationRelativeTo(null);
        queryDialog.setVisible(true);
    }
}
