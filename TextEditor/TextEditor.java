import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class TextEditor extends JFrame implements ActionListener {

    JTextArea textArea;
    JFileChooser fileChooser;

    TextEditor() {
        setTitle("My Text Editor");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Text Area
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);

        // File Chooser
        fileChooser = new JFileChooser();

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newFile = new JMenuItem("New");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");
        JMenuItem exit = new JMenuItem("Exit");

        newFile.addActionListener(this);
        openFile.addActionListener(this);
        saveFile.addActionListener(this);
        exit.addActionListener(this);

        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        // Edit Menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem cut = new JMenuItem("Cut");
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem paste = new JMenuItem("Paste");

        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);

        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);

        // Format Menu
        JMenu formatMenu = new JMenu("Format");
        JMenuItem font = new JMenuItem("Font");
        JMenuItem color = new JMenuItem("Font Color");


        font.addActionListener(this);
        color.addActionListener(this);
        formatMenu.add(font);
        formatMenu.add(color);

        // Add menus to bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);

        setJMenuBar(menuBar);

        // Keyboard Shortcuts
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));

        setVisible(true);
    }

private void showFontDialog() {
    JDialog dialog = new JDialog(this, "Font", true);
    dialog.setSize(350, 250);
    dialog.setLayout(new GridLayout(0, 2, 5, 5));

    // Components
    JComboBox<String> fontBox = new JComboBox<>(
        GraphicsEnvironment.getLocalGraphicsEnvironment()
        .getAvailableFontFamilyNames()
    );

    JComboBox<String> styleBox = new JComboBox<>(new String[]{"Plain", "Bold", "Italic"});
    JComboBox<Integer> sizeBox = new JComboBox<>(new Integer[]{12, 14, 16, 18, 20, 24});

    JLabel preview = new JLabel("Sample Text", JLabel.CENTER);

    JButton apply = new JButton("Apply");
    JButton cancel = new JButton("Cancel");

    // Common preview updater (single lambda)
    ActionListener update = e -> {
        preview.setFont(new Font(
            (String) fontBox.getSelectedItem(),
            styleBox.getSelectedIndex(),
            (Integer) sizeBox.getSelectedItem()
        ));
    };

    fontBox.addActionListener(update);
    styleBox.addActionListener(update);
    sizeBox.addActionListener(update);

    // Apply action
    apply.addActionListener(e -> {
        textArea.setFont(preview.getFont());
        dialog.dispose();
    });

    cancel.addActionListener(e -> dialog.dispose());

    // Add components (compact)
    dialog.add(new JLabel("Font")); dialog.add(fontBox);
    dialog.add(new JLabel("Style")); dialog.add(styleBox);
    dialog.add(new JLabel("Size")); dialog.add(sizeBox);
    dialog.add(preview);
    dialog.add(apply); dialog.add(cancel);

    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}

private void changeFontColor() {
    Color color = JColorChooser.showDialog(this, "Choose Font Color", Color.BLACK);

    if (color != null) {
        textArea.setForeground(color);
    }
}

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "New":
                textArea.setText("");
                break;

            case "Open":
                if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        textArea.read(br, null);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error opening file");
                    }
                }
                break;

            case "Save":
                if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                        textArea.write(bw);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error saving file");
                    }
                }
                break;

            case "Exit":
                System.exit(0);
                break;

            case "Cut":
                textArea.cut();
                break;

            case "Copy":
                textArea.copy();
                break;

            case "Paste":
                textArea.paste();
                break;

            case "Font":
                showFontDialog();
                break;
            
            case "Font Color":
                changeFontColor();
                break;
        }
    }

    public static void main(String[] args) {
        new TextEditor();
    }
}