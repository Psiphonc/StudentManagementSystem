package GUI;

import People.CollegeStudent;
import People.Teacher;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TeacherViewFrame extends JFrame {
    private Teacher teacher;

    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel sbjLabel;
    private JLabel cntLabel;
    private JTextField markField;
    private JButton giveMarkButton;
    private JTable contentTable;
    private JPanel framePnl;
    private JButton logoutButton;
    private JTextField stuIDField;
    private JButton searchButton;
    private JButton grantAsAdministratorButton;
    private JLabel adminLabel;
    private TeacherContentTableModel teacherContentTableModel;

    public TeacherViewFrame(Teacher teacher) {
        this.teacher = teacher;
        setTitle("Teacher Workstation");
        nameLabel.setText(teacher.getName());
        idLabel.setText(teacher.getId());
        sbjLabel.setText(teacher.getSubject().toString());
        cntLabel.setText(Integer.toString(teacher.getSubject().getStudentNum()));
        adminLabel.setText(teacher.getAdmin());
        teacherContentTableModel = new TeacherContentTableModel();
        contentTable.setModel(teacherContentTableModel);
        contentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row = contentTable.getSelectedRow();
                if (row < 0)
                    return;
                String mark = (String) teacherContentTableModel.getValueAt(row, 3);
                String id = (String) teacherContentTableModel.getValueAt(row, 0);
                stuIDField.setText(id);
                markField.setText(mark);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        giveMarkButton.addActionListener(new GiveMarkBtnAction());
        searchButton.addActionListener(new SearchBtnAction());
        grantAsAdministratorButton.addActionListener(new GrantAdminBtnAction());

        setContentPane(framePnl);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    class TeacherContentTableModel extends AbstractTableModel {
        ArrayList<CollegeStudent> students;

        TeacherContentTableModel() {
            students = teacher.getSubject().getStudents();
        }

        @Override
        public String getColumnName(int column) {
            String ret = "";
            switch (column) {
                case 0: {
                    ret = "Student ID";
                    break;
                }
                case 1: {
                    ret = "Class Number";
                    break;
                }
                case 2: {
                    ret = "Name";
                    break;
                }
                case 3: {
                    ret = "Grade";
                    break;
                }
            }
            return ret;
        }

        @Override
        public int getRowCount() {
            return teacher.getSubject().getStudentNum();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            String ret = "";
            switch (columnIndex) {
                case 0: {
                    ret = students.get(rowIndex).getStudentID();
                    break;
                }
                case 1: {
                    ret = students.get(rowIndex).getClassNum();
                    break;
                }
                case 2: {
                    ret = students.get(rowIndex).getName();
                    break;
                }
                case 3: {
                    ret = Double.toString(students.get(rowIndex).getSubjects().get(teacher.getSubject()));
                    break;
                }
            }
            return ret;
        }
    }

    class GiveMarkBtnAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String mark = markField.getText();
            if (mark.equals("")) {
                JOptionPane.showMessageDialog(framePnl, "分数不能为空！");
                return;
            } else {
                int row = contentTable.getSelectedRow();
                String id = (String) contentTable.getValueAt(row, 0);
                ArrayList<CollegeStudent> students = teacher.getSubject().getStudents();
                int idx = students.indexOf(new CollegeStudent(id));
                teacher.giveMark(students.get(idx), Double.parseDouble(mark));
                teacherContentTableModel.fireTableDataChanged();
            }

        }
    }

    class SearchBtnAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String id = stuIDField.getText();
            int idx = teacherContentTableModel.students.indexOf(new CollegeStudent(id));
            if (idx != -1)
                contentTable.setRowSelectionInterval(idx, idx);
        }
    }

    class GrantAdminBtnAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = contentTable.getSelectedRow();
            if (selectedRow == -1)
                return;
            CollegeStudent stu = teacherContentTableModel.students.get(selectedRow);
            teacher.grantAdmin(stu.getStudentID());
            adminLabel.setText(teacher.getAdmin());
        }
    }

}
