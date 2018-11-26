import ClassStuff.Subject;
import GUI.LoginDialog;
import GUI.StudentViewFrame;
import GUI.SubjectAdminView;
import GUI.TeacherViewFrame;
import People.CollegeStudent;
import People.Teacher;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class LauncherWithGUI extends LauncherWithIO {
    static int login_type;
    static String id;

    public static void main(String[] args) {
        init();
        EventQueue.invokeLater(new setupLoginTread());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    updateInfo();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    static class setupLoginTread implements Runnable {
        LoginDialog login = new LoginDialog(null, classes, students, teachers);

        @Override
        public void run() {
            login.setLocationByPlatform(true);
            login.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    super.windowClosed(e);
                    getLoginInfo();
                    switch (login_type) {
                        case 0: {
                            int idx = teachers.indexOf(new Teacher(id));
                            Teacher tea = teachers.get(idx);
                            TeacherViewFrame teacherViewFrame = new TeacherViewFrame(tea);
                            teacherViewFrame.addWindowListener(new WhenWindowClose());
                            break;
                        }
                        case 1: {
                            int idx = students.indexOf(new CollegeStudent(id));
                            CollegeStudent stu = students.get(idx);
                            StudentViewFrame studentViewFrame = new StudentViewFrame(stu);
                            studentViewFrame.addWindowListener(new WhenWindowClose());
                            break;
                        }
                        case 2: {
                            SubjectAdminView subjectAdminView = new SubjectAdminView(Subject.getSubject(id), students);
                            subjectAdminView.addWindowFocusListener(new WhenWindowClose());
                            break;
                        }
                    }
                }
            });
            login.pack();

            login.setVisible(true);
        }

        public void getLoginInfo() {
            login_type = login.getLoginType();
            id = login.getId();
        }
    }

    static class WhenWindowClose extends WindowAdapter {
        @Override
        public void windowClosed(WindowEvent e) {
            EventQueue.invokeLater(new setupLoginTread());
        }
    }
}

