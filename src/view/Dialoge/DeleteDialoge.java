package view.Dialoge;

import controller.Controller;
import model.backpack.Backpack;
import model.shapes.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DeleteDialoge extends JDialog {
    private final Font DEFAULT_FONT = new Font(Font.DIALOG, Font.BOLD, 29);
    private double width_;
    private double height_;
    private Integer shapeID_ = 0;
    private JPanel mainPanel_;
    private JLabel nameLabel_;
    private JTextField nameField_;
    private JButton deleteButton_;
    private JButton cancelButton_;
    private Controller controller_;

    public DeleteDialoge(JFrame parent, int width, int height, boolean modal, Controller controller) {
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
        nameLabel_.setText("Name = ?");
        nameField_ = new JTextField("0");
        nameField_.setEnabled(true);

        deleteButton_ = new JButton("Delete Shape");
        cancelButton_ = new JButton("Cancel");

        mainPanel_.add(nameLabel_);;
        mainPanel_.add(Box.createVerticalGlue());
        mainPanel_.add(nameField_);
        mainPanel_.add(deleteButton_);
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


    public void showException(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Exception? lol", JOptionPane.WARNING_MESSAGE);
    }

    private void makeItListened() {
        deleteButton_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteShape();
                } catch (Exception exception) {
                    showException(exception);
                }
                dispose();
            }
        });


        cancelButton_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        nameField_.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent event) {
                char c = event.getKeyChar();
                if (!((c >= '0' && c <= '9' || c == '.') && nameField_.getText().length() < 7
                        || c == KeyEvent.VK_BACK_SPACE
                        || c == KeyEvent.VK_DELETE
                        || c == KeyEvent.VK_ENTER)) {
                    getToolkit().beep();
                    event.consume();
                }
            }
        });
    }

    public static class IndexException extends RuntimeException {
        public IndexException(String message) {
            super(message);
        }
    }

    void deleteShape() throws IndexException {
        if (nameField_.getText().isEmpty() ||
                Integer.parseInt(nameField_.getText()) >= controller_.getNumberOfBackpacks()) {

            throw new IndexException("Can not find such shape");
        }
        String name = nameField_.getText().toString();
        controller_.removeShape(name);
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