package ClassStuff;

import People.CollegeStudent;
import People.Teacher;

import java.util.ArrayList;

/**
 * 这个抽象类代表学科，内部的一个静态数组subjects包含所有子类的唯一对象。
 * subjects中每个对象代表一个具体学科。
 * 成员变量elective，teacher分别代表该课程是否可选和其任课老师的句柄。
 * 类内部的Arraylist容纳了所有选择该科学生的句柄。
 * 重写equals方法 主键为课程号sub_id。
 *
 * @see ArrayList
 */
public abstract class Subject {

    /**
     * 所有具体学科的数组
     */
    public final static Subject[] subjects = new Subject[5];//学科数组

    static {
        subjects[0] = CSIntro.getInstance();
        subjects[1] = DSA.getInstance();
        subjects[2] = OperatingSystem.getInstance();
        subjects[3] = JavaProgramming.getInstance();
        subjects[4] = WebTechnology.getInstance();
    }

    private final boolean elective;
    /**
     * 所有选择该科的学生数组
     */
    public ArrayList<CollegeStudent> students = new ArrayList<>();
    private Teacher teacher;

    /**
     * 子类继承时必须给出课程是否为选修课
     */
    Subject(boolean elective) {
        this.elective = elective;
    }

    /**
     * 格式化输出所有课程列表，
     * 输出格式：课程号 课程名 选择该科的学生数
     */
    public static void showSubList() {
        System.out.println("Here are all of subjects:");
        for (Subject s : Subject.subjects) {
            System.out.println(s.getSubID() + " " + s.getSubjectName() + ' ' + s.students.size());
        }
    }

    /**
     * 格式化输出选修课课程列表，
     * 输出格式：课程号 课程名 选择该科的学生数
     */
    public static void showElectiveSubList() {
        //System.out.println("Here are all of elective subjects:");
        for (Subject s : Subject.subjects) {
            if (s.isElective())
                System.out.println(s.getSubID() + " " + s.getSubjectName() + ' ' + s.students.size());
        }
    }

    /**
     * 根据课程号给出具体课程对象
     *
     * @param subid 课程号
     * @return 如果所给课程号存在，返回该具体课程对象；若不存在则返回空句柄。
     */
    public static Subject getSubject(String subid) {
        Subject ret = null;
        for (Subject s : subjects) {
            if (s.getSubID().equals(subid)) {
                ret = s;
                break;
            }
        }
        return ret;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Teacher setTeacher(Teacher teacher) {
        return this.teacher = teacher;
    }

    public boolean isElective() {
        return elective;
    }

    /**
     * 学生选课后必须向学科中的学生数组添加自身以便查询
     *
     * @param stu 待添加的学生
     */
    public void addStudent(CollegeStudent stu) {
        students.add(stu);
    }

    public abstract String getSubjectName();

    public abstract String getSubID();

    /**
     * 根据课程号判等，以课程号为主键
     */
    @Override
    public boolean equals(Object sbj) {
        if (this == sbj)
            return true;
        if (sbj == null)
            return false;
        if (this.getClass() != sbj.getClass())
            return false;
        else {
            Subject sbjt = (Subject) sbj;
            return sbjt.getSubID().equals(this.getSubID());
        }
    }

    /**
     * 为了在学生的选课表中可以正常使用HashMap容器，
     * 对父类Object的hashCode函数进行重写
     *
     * @see String#hashCode()
     */
    @Override
    public int hashCode() {
        return getSubID().hashCode();
    }

}
