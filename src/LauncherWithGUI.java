import GUI.LoginDialog;
import GUI.StudentViewFrame;
import People.CollegeStudent;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LauncherWithGUI extends LauncherWithIO {
    static boolean is_tea;
    static String id;

    public static void main(String[] args) {
        init();
        EventQueue.invokeLater(new setupLoginTread());


    }

    static class setupLoginTread implements Runnable {
        LoginDialog login = new LoginDialog(null, classes, students, teachers);

        @Override
        public void run() {
            login.setLocationByPlatform(true);
            login.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    getLoginInfo();
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    super.windowClosed(e);
                    if (login.isTeacher()) {

                    } else {
                        int idx = students.indexOf(new CollegeStudent(login.getId()));
                        CollegeStudent stu = students.get(idx);
                        StudentViewFrame studentViewFrame = new StudentViewFrame(stu);
                        studentViewFrame.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                EventQueue.invokeLater(new setupLoginTread());
                            }
                        });
                    }
                }
            });
            login.pack();
            login.setVisible(true);
        }

        public void getLoginInfo() {
            is_tea = login.isTeacher();
            id = login.getId();
        }
    }

}
