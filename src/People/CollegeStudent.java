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
public class CollegeStudent implements Person,Comparable {
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
                addSubject(i);
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
            if(entry.getKey()!=Subject.getSubject("AVG"))
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
        if (!subjects.containsKey(sbj)) {//检查给出的参数学科是否可选和已经存在
            subjects.put(sbj, 0.0);
            sbj.addStudent(this);
            updateAVG();
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


    /**
     * 格式化返回包含学生选课表和成绩信息的列表
     *
     * @return 学生选课表
     */
    public ArrayList<String> check2String() {
        ArrayList<String> ret = new ArrayList<>();
        for (Map.Entry<Subject, Double> entry : subjects.entrySet()) {
            ret.add(entry.getKey().getSubID() + " " + entry.getValue());
        }
        return ret;
    }

    /**
     * 更新该学生选课表文件
     *
     * @throws IOException
     */
    public void updateStuInfo() throws IOException {
        File file = new File(getGradeListPath());
        File dir = new File(file.getParent());
        if (!dir.exists())
            dir.mkdirs();
        if (file.exists())
            file.delete();
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(getGradeListPath());
        ArrayList<String> stu_info = this.check2String();//获得学生选课表和成绩
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter stu_info_wtr = new BufferedWriter(osw);
        for (String str : stu_info) {
            stu_info_wtr.write(str + "\r\n");//逐行写入
        }
        stu_info_wtr.close();
        osw.close();
        fos.close();
    }

    /**
     * 为该学生创建选课（成绩）列表文件；
     * 若文件已经存在，直接读取选课列表信息。
     *
     * @throws IOException
     */
    public void createGradeList() throws IOException {
        File file = new File(getGradeListPath());
        if (!file.exists())//检查该学生选课列表是否存在
            file.createNewFile();//创建选课列表
        else {//若列表已经存在
            FileInputStream fis_grade = new FileInputStream(file.getPath());
            readGradeFromList(fis_grade);//读取该学生选课列表
            fis_grade.close();
        }
    }

    /**
     * 从选课表文件中读取该学生的选课信息
     *
     * @param fis_grade 包含该学生选课表信息的文件输入流
     * @throws IOException
     */
    public void readGradeFromList(FileInputStream fis_grade) throws IOException {
        InputStreamReader ir_grade = new InputStreamReader(fis_grade);
        BufferedReader br_grade_info = new BufferedReader(ir_grade);
        String grade_info;
        while ((grade_info = br_grade_info.readLine()) != null) {
            if (grade_info.startsWith("\uFEFF"))
                grade_info = grade_info.replace("\uFEFF", "");//去除行标
            String[] kvps = grade_info.split(" ");//以空格为标志切片
            subjects.put(Subject.getSubject(kvps[0]), Double.parseDouble(kvps[1]));
        }
        br_grade_info.close();
        ir_grade.close();
        updateAVG();
    }


    /**
     * 以字符串形式返回学生选课表路径
     *
     * @return 学生选课表路径
     */
    public String getGradeListPath() {
        return "./data/" + getClassNum() + '/' + getStudentID() + ".txt";
    }

    /**
     * 以字符串形式返回学生信息。
     * 学号 姓名 班级
     *
     * @return 该学生信息
     */
    public String getInfo() {
        return student_id + ' ' + name + ' ' + getClassNum() + "\r\n";
    }

    /**
     * 更新该学生当时的平均成绩（忽略分数为0的学科）
     *
     * @return 平均成绩
     */
    public double updateAVG() {
        Subject sub_avg=Subject.getSubject("AVG");
        if(!subjects.containsKey(sub_avg))
            return 0.0;
        subjects.put(sub_avg, 0.0);
        Double avg = 0.0;
        int sbj_cnt=0;
        for (Double n : subjects.values()) {
            if(n!=0.0)
                sbj_cnt++;
            avg += n;
        }
        avg /= sbj_cnt;
        subjects.put(sub_avg,avg);
        return avg;
    }

    /**
     * 比较两个学生对象的平均成绩
     *
     * @param o 待比较对象
     * @return 负整数-小于 0-等于 正整数-大于
     */
    @Override
    public int compareTo(Object o) {
        CollegeStudent stu = (CollegeStudent) o;
        return  this.subjects.get(Subject.getSubject("AVG")).intValue()
                -stu.subjects.get(Subject.getSubject("AVG")).intValue();
    }

    /**
     * 返回平均成绩
     *
     * @return 平均成绩
     */
    public double getAVG(){
        return subjects.get(Subject.getSubject("AVG"));
    }

    @Override
    public int hashCode() {
        return getStudentID().hashCode();
    }
}
