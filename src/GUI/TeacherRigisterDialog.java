package GUI;

import ClassStuff.Subject;
import People.Teacher;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TeacherRigisterDialog extends JDialog {
    ArrayList<Teacher> teachers;

    private JPanel contentPane;
    private JButton buttonRigister;
    private JButton buttonCancel;
    private JTextField accField;
    private JPasswordField passField;
    private JTextField nameField;
    private JComboBox<String> subComboBox;

    public TeacherRigisterDialog(ArrayList<Teacher> teachers) {
        this();
        this.teachers = teachers;
    }

    public TeacherRigisterDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonRigister);

        initSubList();

        buttonRigister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRigister();
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
    }

    private void onRigister() {
        // add your code here
        String teacherid = accField.getText();
        String pass = new String(passField.getPassword());
        String name = nameField.getText();
        if(teacherid.equals("")||pass.equals("")||name.equals("")){
            JOptionPane.showMessageDialog(contentPane, "用户名密码或者姓名不能为空！");
            dispose();
            return;
        }
        String[] sub = ((String) subComboBox.getSelectedItem()).split(" ");
        Subject selected_subject = Subject.getSubject(sub[0]);
        teachers.add(new Teacher(name, teacherid, selected_subject, pass, ""));
        JOptionPane.showMessageDialog(contentPane, "注册成功！");
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void initSubList() {
        for (int i = 1; i < Subject.subjects.length; ++i) {
            subComboBox.addItem(Subject.subjects[i].toString());
        }
    }

    public static void main(String[] args) {
        TeacherRigisterDialog dialog = new TeacherRigisterDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
