import GUI.LoginDialog;
import GUI.StudentViewFrame;
import People.CollegeStudent;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LauncherWithGUI extends LauncherWithIO {

    public static void main(String[] args) {
        init();
        JFrame fmain = new JFrame();
        LoginDialog login = new LoginDialog(fmain, classes, students, teachers);
        login.setLocationByPlatform(true);
        login.pack();
        login.setVisible(true);
        while (login.getId().equals("")) ;
        if (login.isTeacher()) {

        } else {
            int idx = students.indexOf(new CollegeStudent(login.getId()));
            CollegeStudent stu = students.get(idx);
            StudentViewFrame studentViewFrame = new StudentViewFrame(stu);
        }
    }
}
