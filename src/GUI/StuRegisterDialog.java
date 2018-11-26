package GUI;

import ClassStuff.Class;
import People.CollegeStudent;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class StuRegisterDialog extends JDialog {
    ArrayList<Class> classes;
    ArrayList<CollegeStudent> students;


    private JPanel contentPane;
    private JButton buttonRegister;
    private JButton buttonCancel;
    private JPanel infoBoxPnl;
    private JPanel accPnl;
    private JTextField idTextField;
    private JPasswordField passwordField;
    private JComboBox<String> classlistComboBox;
    private JPanel passPnl;
    private JPanel clsPnl;
    private JPanel namePnl;
    private JTextField nameField;

    public StuRegisterDialog(ArrayList<Class> classes, ArrayList<CollegeStudent> students) {
        this.classes = classes;
        this.students = students;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonRegister);

        buttonRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRegister();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        initClassList();

        setLocationByPlatform(true);
    }

    private void onRegister() {
        String id = idTextField.getText();
        String name = nameField.getText();
        String pass = new String(passwordField.getPassword());
        if (id.equals("")||name.equals("")||pass.equals("")){
            JOptionPane.showMessageDialog(contentPane, "用户名密码或者姓名不能为空！");
            dispose();
            return;
        }
        String cls = (String) classlistComboBox.getSelectedItem();
        Class selected_class = classes.get(classes.indexOf(new Class(cls)));
        CollegeStudent stu = new CollegeStudent(id, name, selected_class.getClassNum(), pass);
        selected_class.addStudent(stu);
        students.add(stu);
        JOptionPane.showMessageDialog(contentPane, "注册成功！");
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
//        StuRegisterDialog dialog = new StuRegisterDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
    }

    private void initClassList() {
        for (Class c : classes) {
            classlistComboBox.addItem(c.getClassNum());
        }
    }
}
