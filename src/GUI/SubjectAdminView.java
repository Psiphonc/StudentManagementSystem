package GUI;

import ClassStuff.Subject;
import People.CollegeStudent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class SubjectAdminView extends JFrame {
    Subject subject;
    ArrayList<CollegeStudent> students;
    NotSelectedStuTableModel notSelectedStuTableModel;
    SelectedStuTableModel selectedStuTableModel;
    private JTable notSelectedStudentTable;
    private JTable selectedStudentTable;
    private JTextField stuIDField;
    private JButton searchButton;
    private JButton addToSubjectButton;
    private JButton logoutButton;
    private JLabel sbjInofLabel;
    private JLabel stuCntLabel;
    private JLabel teaInfoLabel;
    private JPanel framePnl;

    public SubjectAdminView(Subject subject, ArrayList<CollegeStudent> students) {
        this.subject = subject;
        this.students = students;
        setTitle("Administrator Workstation");
        sbjInofLabel.setText(subject.toString());
        stuCntLabel.setText(Integer.toString(subject.getStudentNum()));
        teaInfoLabel.setText(subject.getTeacher().getId() + " " + subject.getTeacher().getName());
        notSelectedStuTableModel = new NotSelectedStuTableModel();
        notSelectedStudentTable.setModel(notSelectedStuTableModel);
        selectedStuTableModel = new SelectedStuTableModel();
        selectedStudentTable.setModel(selectedStuTableModel);

        for (MouseListener listener : selectedStudentTable.getTableHeader().getMouseListeners()) {
            selectedStudentTable.getTableHeader().removeMouseListener(listener);
        }
        for (MouseListener listener : notSelectedStudentTable.getTableHeader().getMouseListeners()) {
            notSelectedStudentTable.getTableHeader().removeMouseListener(listener);
        }
        logoutButton.addActionListener(e -> dispose());

        notSelectedStudentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row = notSelectedStudentTable.getSelectedRow();
                if (row < 0)
                    return;
                String id = (String) notSelectedStuTableModel.getValueAt(row, 0);
                stuIDField.setText(id);
            }
        });

        searchButton.addActionListener(new SearchBtnModel());
        addToSubjectButton.addActionListener(new Add2SbjBtnModel());


        setContentPane(framePnl);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);


    }

    class SelectedStuTableModel extends AbstractTableModel {
        ArrayList<CollegeStudent> stus;

        SelectedStuTableModel() {
            updateStudents();
        }

        @Override
        public int getRowCount() {
            return stus.size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            String ret = null;
            CollegeStudent selectedStu = stus.get(rowIndex);
            switch (columnIndex) {
                case 0: {
                    ret = selectedStu.getStudentID();
                    break;
                }
                case 1: {
                    ret = selectedStu.getClassNum();
                    break;
                }
                case 2: {
                    ret = selectedStu.getName();
                    break;
                }
                case 3: {
                    ret = selectedStu.getSubjects().get(subject).toString();
                    break;
                }
            }
            return ret;
        }

        @Override
        public String getColumnName(int column) {
            String ret = null;
            switch (column) {
                case 0:
                case 1:
                case 2: {
                    ret = notSelectedStuTableModel.getColumnName(column);
                    break;
                }
                case 3: {
                    ret = "Grade";
                }

            }
            return ret;
        }

        public void updateStudents() {
            stus = subject.getStudents();
        }
    }

    class NotSelectedStuTableModel extends AbstractTableModel {
        ArrayList<CollegeStudent> stus;

        NotSelectedStuTableModel() {
            updateStudents();
        }

        @Override
        public int getRowCount() {
            return stus.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            String ret = null;
            CollegeStudent selectedStu = stus.get(rowIndex);
            switch (columnIndex) {
                case 0: {
                    ret = selectedStu.getStudentID();
                    break;
                }
                case 1: {
                    ret = selectedStu.getClassNum();
                    break;
                }
                case 2: {
                    ret = selectedStu.getName();
                    break;
                }
            }
            return ret;
        }

        public void updateStudents() {
            ArrayList<CollegeStudent> stuInSbj = subject.getStudents();
            ArrayList<CollegeStudent> allStu = (ArrayList<CollegeStudent>) students.clone();
            allStu.removeAll(stuInSbj);
            stus = allStu;
        }

        @Override
        public String getColumnName(int column) {
            String ret = null;
            switch (column) {
                case 0: {
                    ret = "Student ID";
                    break;
                }
                case 1: {
                    ret = "Class";
                    break;
                }
                case 2: {
                    ret = "Name";
                    break;
                }
            }
            return ret;
        }
    }

    class SearchBtnModel implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String id = stuIDField.getText();
            int idx = notSelectedStuTableModel.stus.indexOf(new CollegeStudent(id));
            if (idx != -1)
                notSelectedStudentTable.setRowSelectionInterval(idx, idx);
        }
    }

    class Add2SbjBtnModel implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = notSelectedStudentTable.getSelectedRow();
            if (row < 0)
                return;
            CollegeStudent stu = notSelectedStuTableModel.stus.get(row);
            stu.addSubject(subject);
            selectedStuTableModel.updateStudents();
            notSelectedStuTableModel.updateStudents();
            selectedStuTableModel.fireTableDataChanged();
            notSelectedStuTableModel.fireTableDataChanged();
        }
    }


}
