package GUI;

import ClassStuff.Subject;
import People.CollegeStudent;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;

public class StudentViewFrame extends JFrame {
    private CollegeStudent student;

    private JLabel nameLabel;
    private JLabel idLabel;
    private JLabel classLabel;
    private JButton logoutButton;
    private JButton addSubjectButton;
    private JTable contentTable;
    private JPanel framePnl;
    private ContentTableModel contentTableModel;

    public StudentViewFrame(CollegeStudent student) {
        this.student = student;
        setTitle("Student Information");
        nameLabel.setText(student.getName());
        idLabel.setText(student.getStudentID());
        classLabel.setText(student.getClassNum());
        contentTableModel = new ContentTableModel();
        contentTable.setModel(contentTableModel);

        AddSubjectAction addSubjectAction = new AddSubjectAction();
        addSubjectButton.addActionListener(addSubjectAction);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setContentPane(framePnl);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    class ContentTableModel extends AbstractTableModel {

        HashMap<Subject, Double> grade_list;
        Object[] subjects;

        ContentTableModel() {
            updateList();
        }

        @Override
        public int getRowCount() {
            return student.getSubjects().size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            String ret = "";
            if (columnIndex == 0) {
                ret = ((Subject) subjects[rowIndex]).getSubID();
            } else if (columnIndex == 1)
                ret = ((Subject) subjects[rowIndex]).getSubjectName();
            else if (columnIndex == 2)
                ret = grade_list.get(subjects[rowIndex]).toString();
            return ret;
        }

        @Override
        public String getColumnName(int column) {
            String[] header = {"Subject ID", "Subject", "Grade"};
            return header[column];
        }

        public void updateList() {
            grade_list = student.getSubjects();
            subjects = grade_list.keySet().toArray();
            Arrays.sort(subjects, (o1, o2) -> {
                Subject s1 = (Subject) o1;
                Subject s2 = (Subject) o2;
                return s1.compareTo(s2);
            });
        }
    }

    class AddSubjectAction extends AbstractAction {

        public AddSubjectAction() {
            putValue("name", "Add Subject");
            putValue("selected_sbj", null);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String[] availableSubject = Subject.getAvailableSubject(student);
            String msg = "These are the subjects available now:\n";
            String title = "Select Subject";

            String selected_sub = (String) JOptionPane.showInputDialog(framePnl, msg, title,
                    JOptionPane.PLAIN_MESSAGE, null,
                    availableSubject, availableSubject[0]);

            putValue("selected_sbj", selected_sub.split(" ")[0]);
            student.addSubject(Subject.getSubject((String) getValue("selected_sbj")));
            contentTableModel.updateList();
            contentTableModel.fireTableDataChanged();
            pack();
            if (availableSubject.length == 1)
                addSubjectButton.setEnabled(false);
        }
    }

}
