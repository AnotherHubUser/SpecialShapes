package view;

import controller.Controller;
import controller.XMLCommunicator;
import model.shapes.*;
import model.backpack.BackpackManager;
import model.backpack.Backpack;
import model.shapes.Shape;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class View extends JFrame {
    private static Integer number_ = 0;
    private final String[] tableCols_ = {"name", "type", "is shape pretty good?", "volume", "perimeter"};
    private final Font TABLE_FONT = new Font(Font.DIALOG, Font.PLAIN, 29);
    private final Font UI_FONT = new Font(Font.DIALOG, Font.ROMAN_BASELINE, 29);
    private double width_;
    private double height_;
    private Container container_;
    private JPanel toolPanel_;
    private JButton addButton_;
    private JButton eraseButton_;
    private JTable table_;
    private JScrollPane tablePanel_;
    private JProgressBar backpackBar_;
    private JLabel backpackLabel_;
    private Controller controller_;
    private JMenuBar menuBar_;
    private JMenu menu_;
    private JMenuItem menuSave_;
    private JMenuItem menuLoad_;
    private JComboBox<String> backpackComboBox_;
    private JButton backpackButton_;
    private Type type_;
    enum Type {
        CUBE,
        CYLINDER,
        GLOBE,
        PARALLELEPIPED,
        TETRAHEDRON
    }

    public View(int width, int height, String window_name, Controller controller) {
        super(window_name);
        this.setBounds(29, 29, width, height);
        this.width_ = width;
        this.height_ = height;
        this.setMinimumSize(new Dimension(width, height));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        controller_ = controller;

        container_ = this.getContentPane();
        container_.setLayout(new BoxLayout(container_, BoxLayout.Y_AXIS));

        container_.setBackground(Color.YELLOW);
        container_.setForeground(Color.BLUE);
        toolPanel_ = new JPanel();
        toolPanel_.setLayout(new BoxLayout(toolPanel_, BoxLayout.X_AXIS));
        toolPanel_.setBackground(Color.CYAN);
        toolPanel_.setForeground(Color.BLUE);

//      here i create combobox
        String[] items = {"Backpack 1"};
        backpackComboBox_ = new JComboBox<>(items);
        backpackComboBox_.setAlignmentX(Component.LEFT_ALIGNMENT);
        backpackComboBox_.setEditable(true);
        backpackComboBox_.setForeground(Color.BLUE);
        toolPanel_.add(backpackComboBox_);

        backpackLabel_ = new JLabel("Backpack filled:", JLabel.CENTER);
        toolPanel_.add(backpackLabel_);

        backpackBar_ = new JProgressBar(JProgressBar.CENTER);
        backpackBar_.setStringPainted(true);
        backpackBar_.setBackground(Color.yellow);
        backpackBar_.setForeground(Color.blue);

        toolPanel_.add(backpackBar_);
        toolPanel_.add(Box.createHorizontalGlue());

        addButton_ = new JButton("Add Figure");
        addButton_.setBackground(Color.YELLOW);
        addButton_.setForeground(Color.BLUE);
        toolPanel_.add(addButton_);

        eraseButton_ = new JButton("Erase Shape");
        eraseButton_.setBackground(Color.YELLOW);
        eraseButton_.setForeground(Color.BLUE);
        toolPanel_.add(eraseButton_);

        backpackButton_ = new JButton("Buy a new backpack");
        backpackButton_.setBackground(Color.YELLOW);
        backpackButton_.setForeground(Color.BLUE);
        toolPanel_.add(backpackButton_);


        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, tableCols_);
        table_ = new JTable(model);
        table_.setRowHeight(37);
        table_.setDefaultEditor(Object.class, null);
        table_.setFont(TABLE_FONT);
        tablePanel_ = new JScrollPane(table_);
        tablePanel_.setBackground(Color.YELLOW);
        tablePanel_.setForeground(Color.BLUE);

        container_.add(tablePanel_);
        container_.add(toolPanel_);

        menuBar_ = new JMenuBar();

        menu_ = new JMenu("Settings");
        menuSave_ = new JMenuItem("save");
        menuLoad_ = new JMenuItem("load");
        menu_.add(menuSave_);
        menu_.add(menuLoad_);
        menuBar_.add(menu_);

        this.setJMenuBar(menuBar_);



        for (Component component : toolPanel_.getComponents()) {
            ((JComponent) component).setAlignmentX(Component.CENTER_ALIGNMENT);
            ((JComponent) component).setAlignmentY(Component.CENTER_ALIGNMENT);
            component.setFont(UI_FONT);
        }

        makeItListened();
        stretchOut();


        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                stretchOut();
                super.componentResized(e);
            }
        });
    }

    public void Show() {
        setVisible(true);
    }


    private MouseAdapter menuMouseListener = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            JMenu item = (JMenu) e.getSource();
            item.setSelected(true);
            item.doClick();
        };

        @Override
        public void mouseExited(MouseEvent e) {
            JMenu item = (JMenu) e.getSource();
            item.setSelected(false);
        };
    };

    private void makeItListened() {
        addButton_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller_.openAddDialoge(controller_);
            }
        });
        eraseButton_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller_.removeShapes();
            }
        });
        backpackButton_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller_.addBackpack();
            }
        });

//      here i create listener to combobox
        backpackComboBox_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox)e.getSource();
                Integer index = (Integer)box.getSelectedIndex();
                controller_.setCurrentBackpack(index);
                controller_.update();
            }
        });


                menu_.addMouseListener(menuMouseListener);
        menuSave_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller_.save();
            }
        });
        menuLoad_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller_.load();
            }
        });
    }

    private void stretchOut() {
        width_ = this.getWidth();
        height_ = this.getHeight();

        tablePanel_.setPreferredSize(new Dimension((int) Math.round(width_), (int) Math.round(height_ * 3 / 4)));
        toolPanel_.setPreferredSize(new Dimension((int) Math.round(width_), (int) Math.round(height_ * 1 / 4)));

        for (Component component : toolPanel_.getComponents()) {
            component.setPreferredSize(new Dimension((int) Math.round(width_*0.15), (int) Math.round(height_ * 1 / 4)));
        }
    }


    public void addShape(Shape shape) {
        number_++;
        String name = number_.toString();
        String type = shape.type();
        String pretty = shape.isPretty() ? " Yes " : " No ";
        String volume = shape.volume().toString();

        type_ = Type.valueOf(type.toUpperCase());
        String perimeter = "";
        switch (type_){
            case CUBE:
                perimeter = ((Cube) shape).Perimeter().toString();
                break;
            case TETRAHEDRON:
                perimeter = ((Tetrahedron) shape).Perimeter().toString();
                break;
            case PARALLELEPIPED:
                perimeter = ((Parallelepiped) shape).Perimeter().toString();
            case GLOBE:
            case CYLINDER:
                break;
        }

        DefaultTableModel model = (DefaultTableModel) table_.getModel();
        model.addRow(new Object[]{name, type, pretty, volume, perimeter});
    }

    public void showException(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Exception? lol", JOptionPane.WARNING_MESSAGE);
    }


    public void updateShapes(ArrayList<Shape> shapes, double size, double capacity) {
        DefaultTableModel model = (DefaultTableModel) table_.getModel();
        model.setRowCount(0);
        number_ = 0;
//        if(shapes.size() == 0){
//            return;
//        }
        for (Shape shape : shapes) {
            addShape(shape);
        }
        int value = (int) Math.round(size / capacity * 100);
        backpackBar_.setValue(value);
        backpackBar_.setString((int) Math.round(size) + "/" + capacity);
        backpackBar_.setForeground(new Color(97, 81, 200));
    }

    public void addBackpack(String name) {
        backpackComboBox_.addItem(name);
        backpackComboBox_.setSelectedItem(name);
    }

    public String getSavePath() {
        FileDialog fileDialog = new FileDialog(this, "Choose file where to save", FileDialog.SAVE);
        fileDialog.setFile("*.xml");
        fileDialog.setMultipleMode(false);
        fileDialog.setModal(true);
        fileDialog.setVisible(true);
        return fileDialog.getFile();
    }

    public String getLoadPath() {
        FileDialog fileDialog = new FileDialog(this, "Choose file where to load from", FileDialog.LOAD);
        fileDialog.setFile("*.xml");
        fileDialog.setMultipleMode(false);
        fileDialog.setModal(true);
        fileDialog.setVisible(true);
        return fileDialog.getFile();
    }
}
