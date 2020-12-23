package view.Dialoge;

import controller.Controller;
import model.shapes.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class AddBackpackDialoge extends JDialog {
    private final Font DEFAULT_FONT = new Font(Font.DIALOG, Font.BOLD, 29);
    private double width_;
    private double height_;
    private Integer shapeID_ = 0;
    private JPanel mainPanel_;
    private JLabel sizeLabel_;
    private JTextField sizeField_;
    private JLabel nameLabel_;
    private JTextField nameField_;
    private JButton addButton_;
    private JButton cancelButton_;
    private Controller controller_;

    public AddBackpackDialoge(JFrame parent, int width, int height, boolean modal, Controller controller) {
        super(parent, modal);
        controller_ = controller;
        this.width_ = width;
        this.height_ = height;
        this.setMinimumSize(new Dimension(width, height));
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        mainPanel_ = new JPanel();
        mainPanel_.setAlignmentY(Component.CENTER_ALIGNMENT);
        mainPanel_.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel_.setPreferredSize(new Dimension((int) Math.round(width_), (int) Math.round(height_)));

        nameLabel_ = new JLabel("", SwingConstants.CENTER);
        nameLabel_.setText("Name of backpack = ?");
        nameField_ = new JTextField("");
        nameField_.setEnabled(true);

        sizeLabel_ = new JLabel("", SwingConstants.CENTER);
        sizeLabel_.setText("Size = ?");
        sizeField_ = new JTextField("0");
        sizeField_.setEnabled(true);

        addButton_ = new JButton("Add Backpack");
        cancelButton_ = new JButton("Cancel");

        mainPanel_.add(nameLabel_);
        mainPanel_.add(Box.createVerticalGlue());
        mainPanel_.add(nameField_);
        mainPanel_.add(sizeLabel_);
        mainPanel_.add(sizeField_);
        mainPanel_.add(addButton_);
        mainPanel_.add(cancelButton_);

        for (Component item : mainPanel_.getComponents()) {
            item.setFont(DEFAULT_FONT);
        }

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                stretchOut();
                super.componentResized(e);
            }
        });


        this.getContentPane().add(mainPanel_);
        this.pack();

        makeItListened();
    }

    private void makeItListened() {
        addButton_.addActionListener(e -> {
            addBackpack();
            dispose();
        });


        cancelButton_.addActionListener(e -> dispose());


        sizeField_.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent event) {
                char c = event.getKeyChar();
                if (!((c >= '0' && c <= '9' || c == '.') && sizeField_.getText().length() < 7
                        || c == KeyEvent.VK_BACK_SPACE
                        || c == KeyEvent.VK_DELETE
                        || c == KeyEvent.VK_ENTER)) {
                    getToolkit().beep();
                    event.consume();
                }
            }
        });
    }


    void addBackpack() {
        if(!sizeField_.getText().isEmpty()){
            Double value = Double.parseDouble(sizeField_.getText());
            String name = nameField_.getText().toString();
            controller_.addBackpack(value, name);
        }
    }


    private void stretchOut() {
        mainPanel_.setSize(new Dimension(this.getWidth(), this.getHeight()));
        width_ = mainPanel_.getWidth() - 15;
        height_ = mainPanel_.getHeight() - 15;
        if (mainPanel_.getComponents().length == 0) {
            return;
        }

        int unit_height = (int) Math.floor(height_ /  (mainPanel_.getComponents().length + 1));
        int unit_width = (int) Math.floor(width_);
        for (Component item : mainPanel_.getComponents()) {
            Dimension item_size = new Dimension(unit_width, unit_height);
            item.setPreferredSize(item_size);
        }
    }
}