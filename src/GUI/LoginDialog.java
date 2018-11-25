package GUI;

import ClassStuff.Class;
import People.CollegeStudent;
import People.Teacher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class LoginDialog extends JDialog {
    private ArrayList<Class> classes;
    private ArrayList<CollegeStudent> students;
    private ArrayList<Teacher> teachers;
    private String id;
    private boolean teacher_flag = false;


    private JPanel contentPane;
    private JButton buttonLogin;
    private JButton buttonCancel;
    private JRadioButton studentRadioButton;
    private JRadioButton teacherRadioButton;
    private JButton buttonRigister;
    private JTextField accTextField;
    private JPasswordField passTextField;

    public LoginDialog(Frame owner) {
        super(owner);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonLogin);

        studentRadioButton.setSelected(true);

        buttonLogin.addActionListener(e -> onLogin());
        buttonCancel.addActionListener(e -> onCancel());
        buttonRigister.addActionListener(e -> onRigister());

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

    private void onRigister() {
        if (studentRadioButton.isSelected()) {
            StuRegisterDialog stuRegisterDialog = new StuRegisterDialog(classes, students);
            stuRegisterDialog.pack();
            stuRegisterDialog.setVisible(true);
        } else {
            TeacherRigisterDialog teacherRigisterDialog = new TeacherRigisterDialog(teachers);
            teacherRigisterDialog.pack();
            teacherRigisterDialog.setVisible(true);
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
                teacher_flag = true;
                showMessage("登陆成功！");
                setVisible(false);
            } else {
                showMessage("密码错误！");
            }

        } else {
            int idx = students.indexOf(new CollegeStudent(acc));
            if (idx == -1) {
                showMessage("该账号不存在");
                return;
            }
            CollegeStudent stu = students.get(idx);
            if (stu.getPassword().equals(pass)) {
                id = stu.getStudentID();
                teacher_flag = false;
                showMessage("登陆成功！");
                setVisible(false);
            } else {
                showMessage("密码错误！");
            }

        }
    }

    private void onCancel() {
        // add your code here if necessary
        System.exit(0);
    }

    public boolean isTeacher() {
        return teacher_flag;
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

    public void clear() {
        studentRadioButton.setSelected(true);
        teacher_flag=false;
        accTextField.setText("");
        passTextField.setText("");
        id="";
    }

}
