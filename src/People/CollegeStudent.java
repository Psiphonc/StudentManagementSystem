package People;

import ClassStuff.Subject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 这是一个由父接口Person继承而来的大学生类，每一个对象代表一个大学生。
 * 类中成员student_id,name,class_num分别代表学号，姓名和班级，均为字符串型并对外提供getter和setter。
 * 类中还包含一个HashMap成员subjects，键值对分别表示学科和成绩。
 * 哈希表subjects会在默认构造器中初始化成当前课程列表中所有必修科目，成绩为0分。
 * 可以通过类内给出的addSubject方法选课。
 * 允许只使用学号和三者均给出来构造本类成员，但只给出学号的构造方法应仅用于构造用于比较的临时对象。
 * 重写了equals方法，两个大学生之间判等仅根据学号，只要学号相同即使其余成员不同也会返回true。
 *
 * @see People.Person
 * @see ClassStuff.Subject
 */
public class CollegeStudent implements Person {
    /**
     * 课程表
     */
    HashMap<Subject, Double> subjects = new HashMap<Subject, Double>();
    private String student_id;
    private String name;
    private String class_num;

    /**
     * 初始化学生信息，
     * 在学生的课表中添加所有必修课 成绩初始化为0分。
     *
     * @param stu_id    学号
     * @param stu_name  姓名
     * @param class_num 班级
     */
    public CollegeStudent(String stu_id, String stu_name, String class_num) {
        setName(stu_name);
        setStudentID(stu_id);
        setClassNum(class_num);
        for (Subject i : Subject.subjects) {//初始化学生课程表
            if (!i.isElective()) {//检查是否必修
                subjects.put(i, 0.0);
                i.addStudent(this);//把该学生添加到学科的学生列表中
            }
        }
    }

    public CollegeStudent(String[] stu_info_array) {
        this(stu_info_array[0], stu_info_array[1], stu_info_array[2]);
    }

    /**
     * 仅用于构造用于查找的临时对象，只初始化学号变量。
     *
     * @param id 学号
     */
    public CollegeStudent(String id) {
        student_id = id;
    }

    public String getStudentID() {
        return student_id;
    }

    public String setStudentID(String stu_id) {
        return student_id = stu_id;
    }

    public String getName() {
        return name;
    }

    public String setName(String name) {
        return this.name = name;
    }

    public String getClassNum() {
        return this.class_num;
    }

    public void setClassNum(String class_num) {
        this.class_num = class_num;
    }

    /**
     * 该方法只是因为父接口中要求实现，实际并没有起到什么作用。
     */
    @Override
    public void work() {
        System.out.println("I am working hard.");

    }

    /**
     * 该方法用于格式化输出该生所有科目的成绩。
     * 输出格式为：课程号学科名    成绩
     *
     * @see Person#check()
     */
    @Override
    public void check() {
        for (Map.Entry<Subject, Double> entry : subjects.entrySet()) {
            System.out.println(
                    entry.getKey().getSubID() + entry.getKey().getSubjectName() + "    " + entry.getValue()
            );
        }
    }

    /**
     * 格式化输出该学生给定科目的成绩，
     * 输出格式为：课程号学科名    成绩。
     *
     * @param sbj 想要输出的学科句柄
     */
    public void check(Subject sbj) {
        if (subjects.containsKey(sbj)) {//检查给出科目是否在学生的课表中
            System.out.println(getStudentID() + ' ' + getName() + ' ' + getClassNum() + ' ' + subjects.get(sbj));
        }
    }

    /**
     * 选课，检查给出的参数学科是否可选和已经存在,之后将其添加至该生的课程表中。
     *
     * @param sbj 待选学科句柄
     */
    public void addSubject(Subject sbj) {
        if (sbj.isElective() && !subjects.containsKey(sbj)) {//检查给出的参数学科是否可选和已经存在
            subjects.put(sbj, 0.0);
            sbj.addStudent(this);
            //System.out.println(sbj.getSubID()+sbj.getSubjectName()+" added successfully.");
        }
    }

    /**
     * 根据学号判等，两个大学生之间判等仅根据学号，
     * 只要学号相同即使其余成员不同也会返回true。
     */
    @Override
    public boolean equals(Object stu) {
        if (this == stu)
            return true;
        if (stu == null)
            return false;
        if (this.getClass() != stu.getClass())
            return false;
        else {
            CollegeStudent stut = (CollegeStudent) stu;
            return stut.getStudentID().equals(this.getStudentID());
        }
    }

    /**
     * 格式化输出该学生的信息，
     * 输出格式：班级 学号 姓名。
     */
    public void printInfo() {
        System.out.println(this.getClassNum() + " " + this.getStudentID() + " " + this.getName());
    }

    public ArrayList<String> check2String() {
        ArrayList<String> ret = new ArrayList<>();
        for (Map.Entry<Subject, Double> entry : subjects.entrySet()) {
            ret.add(entry.getKey().getSubID() + " " + entry.getValue());
        }
        return ret;
    }

    public void writeStuInfo(FileOutputStream fos) throws IOException {
        ArrayList<String> stu_info = this.check2String();
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter stu_info_wtr = new BufferedWriter(osw);
        for (String str : stu_info) {
            stu_info_wtr.write(str + "\r\n");
        }
        stu_info_wtr.close();
        osw.close();
    }

    public void updateStuInfo() throws IOException {
        File file = new File(getGradeListPath());
        File dir=new File(file.getParent());
        if(!dir.exists())
            dir.mkdirs();
        if(file.exists())
            file.delete();
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(getGradeListPath());
        writeStuInfo(fos);
        fos.close();
    }

    public void createGradeList() throws IOException {
        File file = new File(getGradeListPath());
        if (!file.exists())
            file.createNewFile();
        else {
            FileInputStream fis_grade = new FileInputStream(file.getPath());
            readGradeFromList(fis_grade);
            fis_grade.close();
        }

    }

    public void readGradeFromList(FileInputStream fis_grade) throws IOException {
        InputStreamReader ir_grade = new InputStreamReader(fis_grade);
        BufferedReader br_grade_info = new BufferedReader(ir_grade);
        String grade_info;
        while ((grade_info = br_grade_info.readLine()) != null) {
            if (grade_info.startsWith("\uFEFF"))
                grade_info = grade_info.replace("\uFEFF", "");
            String[] kvps = grade_info.split(" ");
            subjects.put(Subject.getSubject(kvps[0]), Double.parseDouble(kvps[1]));
        }
        br_grade_info.close();
        ir_grade.close();
    }

    public String getGradeListPath() {
        return "./data/" + getClassNum() + '/' + getStudentID() + ".txt";
    }
    public String getInfo(){
        return student_id+' '+name+' '+getClassNum()+"\r\n";
    }
}
