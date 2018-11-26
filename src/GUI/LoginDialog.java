package GUI;

import ClassStuff.Class;
import ClassStuff.Subject;
import People.CollegeStudent;
import People.Teacher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class LoginDialog extends JDialog {
    private ArrayList<Class> classes;
    private ArrayList<CollegeStudent> students;
    private ArrayList<Teacher> teachers;
    private String id;
    private int login_type = 1;

    private JPanel contentPane;
    private JButton buttonLogin;
    private JButton buttonCancel;
    private JRadioButton studentRadioButton;
    private JRadioButton teacherRadioButton;
    private JButton buttonRegister;
    private JTextField accTextField;
    private JPasswordField passTextField;
    private JRadioButton administratorRadioButton;

    public LoginDialog(Frame owner) {
        super(owner);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonLogin);

        studentRadioButton.setSelected(true);

        buttonLogin.addActionListener(e -> onLogin());
        buttonCancel.addActionListener(e -> onCancel());
        buttonRegister.addActionListener(e -> onRegister());

        administratorRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (administratorRadioButton.isSelected()) {
                    buttonRegister.setEnabled(false);
                }
            }
        });
        studentRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (studentRadioButton.isSelected()) {
                    buttonRegister.setEnabled(true);
                }
            }
        });
        teacherRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (teacherRadioButton.isSelected()) {
                    buttonRegister.setEnabled(true);
                }
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
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public LoginDialog(Frame owner, ArrayList<Class> classes, ArrayList<CollegeStudent> students, ArrayList<Teacher> teachers) {
        this(owner);
        this.classes = classes;
        this.students = students;
        this.teachers = teachers;
    }

    private void onRegister() {
        if (studentRadioButton.isSelected()) {
            StuRegisterDialog stuRegisterDialog = new StuRegisterDialog(classes, students);
            stuRegisterDialog.pack();
            stuRegisterDialog.setVisible(true);
        } else {
            TeacherRegisterDialog teacherRegisterDialog = new TeacherRegisterDialog(teachers);
            teacherRegisterDialog.pack();
            teacherRegisterDialog.setVisible(true);
        }
    }

    private void onLogin() {
        String acc = accTextField.getText();
        String pass = new String(passTextField.getPassword());

        if (acc.equals("") || pass.equals("")) {
            showMessage("账号或密码不能为空");
            return;
        }
        if (teacherRadioButton.isSelected()) {
            int idx = teachers.indexOf(new Teacher(acc));
            if (idx == -1) {
                showMessage("该账号不存在");
                return;
            }
            Teacher tea = teachers.get(idx);
            if (tea.getPass().equals(pass)) {
                id = tea.getId();
                login_type = 0;
                showMessage("登陆成功！");
                dispose();
            } else {
                showMessage("密码错误！");
            }

        } else if (studentRadioButton.isSelected()) {
            int idx = students.indexOf(new CollegeStudent(acc));
            if (idx == -1) {
                showMessage("该账号不存在");
                return;
            }
            CollegeStudent stu = students.get(idx);
            if (stu.getPassword().equals(pass)) {
                id = stu.getStudentID();
                login_type = 1;
                showMessage("登陆成功！");
                dispose();
            } else {
                showMessage("密码错误！");
            }
        } else {
            Subject subject = Subject.getSubject(acc);
            if (subject != null && subject.getAdmin().getPassword().equals(pass)) {
                id = acc;
                login_type = 2;
                showMessage("登陆成功！");
                dispose();
            } else {
                showMessage("密码错误！");
            }
        }
    }

    private void onCancel() {
        // add your code here if necessary
        System.exit(0);
    }

    public int getLoginType() {
        return login_type;
    }

    public String getId() {
        return id;
    }

    private void showMessage(String m) {
        JOptionPane.showMessageDialog(contentPane, m);
    }

    public String getLoginInfo() {
        this.setLocationByPlatform(true);
        this.pack();
        this.setVisible(true);
        return "";
    }


}
