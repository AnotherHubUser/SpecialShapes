package view.Dialoge;

import controller.Controller;
import model.shapes.Shape;
import model.shapes.Parallelepiped;
import model.shapes.Tetrahedron;
import model.shapes.Globe;
import model.shapes.Cylinder;
import model.shapes.Cube;
import view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class AddDialoge  extends JDialog {
    private final Font DEFAULT_FONT = new Font(Font.DIALOG, Font.BOLD, 29);
    private double width_;
    private double height_;
    private Integer shapeID_ = 0;
    private JPanel mainPanel_;
    private String[] QUESTIONS = {"Side length = ?", "Radius = ?", "Radius = ?", "Side length = ?", "Edge length = ?"};
    private JLabel typeLabel_;
    private JComboBox<String> typeComboBox_;
    private JLabel sideLabel_;
    private JTextField sideField_;
    private JLabel lengthLabel_;
    private JTextField lengthField_;
    private JLabel highLabel_;
    private JTextField highField_;
    private JButton addButton_;
    private JButton cancelButton_;
    private Controller controller_;
    private Type type_;
    enum Type {
        CUBE,
        CYLINDER,
        GLOBE,
        PARALLELEPIPED,
        TETRAHEDRON
    }

    public AddDialoge(JFrame parent, int width, int height, boolean modal, Controller controller) {
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


        typeLabel_ = new JLabel("Shape:", SwingConstants.CENTER);

        typeComboBox_ = new JComboBox<>(new String[]{"Cube", "Cylinder", "Globe", "Parallelepiped", "Tetrahedron"});
        typeComboBox_.setFocusable(false);
        sideLabel_ = new JLabel("", SwingConstants.CENTER);
        sideLabel_.setText(QUESTIONS[shapeID_]);
        sideField_ = new JTextField("0");
        highLabel_ = new JLabel("", SwingConstants.CENTER);
        highLabel_.setText("high = ?");
        highField_ = new JTextField("0");
        highField_.setEnabled(false);
        lengthLabel_ = new JLabel("", SwingConstants.CENTER);
        lengthLabel_.setText("length = ?");
        lengthField_ = new JTextField("0");
        lengthField_.setEnabled(false);

        addButton_ = new JButton("Add 3D-Shape");
        cancelButton_ = new JButton("Cancel");

        mainPanel_.add(typeLabel_);
        mainPanel_.add(Box.createVerticalGlue());
        mainPanel_.add(typeComboBox_);
        mainPanel_.add(sideLabel_);
        mainPanel_.add(sideField_);
        mainPanel_.add(highLabel_);
        mainPanel_.add(highField_);
        mainPanel_.add(lengthLabel_);
        mainPanel_.add(lengthField_);
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


        addButton_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addShape();
                dispose();
            }
        });


        cancelButton_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        typeComboBox_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type_ = Type.valueOf(Objects.requireNonNull(
                        typeComboBox_.getSelectedItem()).toString().toUpperCase());
                switch (type_){
                    case CUBE:
                        shapeID_ = 0;
                        highField_.setEnabled(false);
                        highField_.setText("");
                        lengthField_.setEnabled(false);
                        lengthField_.setText("");
                        break;
                    case CYLINDER:
                        shapeID_ = 1;
                        highField_.setEnabled(true);
                        highField_.setText("0");
                        lengthField_.setEnabled(false);
                        lengthField_.setText("");
                        break;
                    case GLOBE:
                        shapeID_ = 2;
                        highField_.setEnabled(false);
                        highField_.setText("");
                        lengthField_.setEnabled(false);
                        lengthField_.setText("");
                        break;
                    case PARALLELEPIPED:
                        shapeID_ = 3;
                        highField_.setEnabled(true);
                        highField_.setText("0");
                        lengthField_.setEnabled(true);
                        highField_.setText("0");
                        break;
                    case TETRAHEDRON:
                        shapeID_ = 4;
                        highField_.setEnabled(false);
                        highField_.setText("");
                        lengthField_.setEnabled(false);
                        lengthField_.setText("");
                        break;
                }
                sideLabel_.setText(QUESTIONS[shapeID_]);
            }
        });

        sideField_.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent event) {
                char c = event.getKeyChar();
                if (!((c >= '0' && c <= '9' || c == '.') && sideField_.getText().length() < 7
                        || c == KeyEvent.VK_BACK_SPACE
                        || c == KeyEvent.VK_DELETE
                        || c == KeyEvent.VK_ENTER)) {
                    getToolkit().beep();
                    event.consume();
                }
            }
        });

        lengthField_.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent event) {
                char c = event.getKeyChar();
                if (!((c >= '0' && c <= '9' || c == '.') && lengthField_.getText().length() < 7
                        || c == KeyEvent.VK_BACK_SPACE
                        || c == KeyEvent.VK_DELETE
                        || c == KeyEvent.VK_ENTER)) {
                    getToolkit().beep();
                    event.consume();
                }
            }
        });

        highField_.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent event) {
                char c = event.getKeyChar();
                if (!((c >= '0' && c <= '9' || c == '.') && highField_.getText().length() < 7
                        || c == KeyEvent.VK_BACK_SPACE
                        || c == KeyEvent.VK_DELETE
                        || c == KeyEvent.VK_ENTER)) {
                    getToolkit().beep();
                    event.consume();
                }
            }
        });
    }


    void addShape() {
        Shape shape = null;
        Double value = 0.0;
        if(!sideField_.getText().isEmpty()){
            value = Double.parseDouble(sideField_.getText());
        }

        type_ = Type.valueOf(Objects.requireNonNull(
                typeComboBox_.getSelectedItem()).toString().toUpperCase());
        switch (type_){
            case CUBE:
                shape = new Cube(value);
                break;
            case CYLINDER:
                shape = new Cylinder(value, Double.parseDouble(highField_.getText()));
                break;
            case GLOBE:
                shape = new Globe(value);
                break;
            case PARALLELEPIPED:
                shape = new Parallelepiped(value,
                        Double.parseDouble(highField_.getText()),
                        Double.parseDouble(lengthField_.getText()));
                break;
            case TETRAHEDRON:
                shape = new Tetrahedron(value);
                break;
        }
//        switch (shapeID_) {
//            case 0:
//                shape = new Cube(value);
//                break;
//            case 1:
//                shape = new Globe(value);
//                break;
//            case 2:
//                shape = new Cylinder(value);
//                break;
//            case 3:
//                shape = new Tetrahedron(value);
//                break;
//        }
        controller_.addShape(shape);
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