package ClassStuff;

import People.CollegeStudent;

import java.io.*;
import java.util.ArrayList;

/**
 * 班级类，类内字符串型成员class_num代表班级号，是班级在判等时的主键。
 * int型成员major和静态字符串数组字段major_type下标一一对应表示该班级专业。
 * 为了方便查找，类内students容器容纳了改班级的所有学生的句柄.
 *
 * @see CollegeStudent
 * @see ArrayList
 */
public class Class {
    /**
     * 所有专业
     */
    private final static String[] major_type = new String[]{"CS", "EE", "ENGLISH"};
    private String class_num;
    private int major;
    /**
     * 班级内所有学生
     */
    private ArrayList<CollegeStudent> students;

    /**
     * 通过班级号和专业编号构造班级
     *
     * @param class_num 班级号
     * @param major     专业代码
     */
    public Class(String class_num, int major) {
        this.major = major;
        this.class_num = class_num;
        students = new ArrayList<>();
    }

    /**
     * 向该班级学生列表中添加学生
     *
     * @throws IOException
     */
    public void readStudent() throws IOException {
        FileInputStream stu_list = new FileInputStream(getStudentListPath());
        InputStreamReader isr = new InputStreamReader(stu_list);
        BufferedReader stu_info = new BufferedReader(isr);
        String stu_info_detail;
        while ((stu_info_detail = stu_info.readLine()) != null) {
            if (stu_info_detail.startsWith("\uFEFF"))
                stu_info_detail = stu_info_detail.replace("\uFEFF", "");//删除windows行标支付
            String[] stu_stripped_info = stu_info_detail.split(" ");//以空格为标记切片字符串
            CollegeStudent temp_stu = new CollegeStudent(stu_stripped_info);
            if (students.indexOf(temp_stu) == -1) {//检查该学生是否已经存在
                addStudent(temp_stu);
            }
        }
        stu_info.close();
        isr.close();
        stu_list.close();
        initGradeListForWholeClass();
    }

    /**
     * 为班级内学生逐个创建选课信息（成绩单）；
     * 如果该学生选课列表已经存在，则直接读取选课信息。
     *
     * @throws IOException
     */
    public void initGradeListForWholeClass() throws IOException {
        for (CollegeStudent t_stu : students) {
            t_stu.createGradeList();
        }
    }

    public String getClassPath() {
        return "./data/" + getClassNum() + '/';
    }

    /**
     * 通过班级号构造临时班级，仅用于查找操作
     *
     * @param class_num 待查找班级号
     */
    public Class(String class_num) {
        this.class_num = class_num;
    }

    /**
     * 向班级中添加新学生
     *
     * @param stu 待添加学生
     * @return 刚刚加入的学生
     */
    public CollegeStudent addStudent(CollegeStudent stu) {
        students.add(stu);
        return students.get(students.size() - 1);
    }

    /**
     * 根据学生在数组中的下标获取学生
     *
     * @param idx 学生在内部数组中的下标
     * @return 找到的学生
     * @throws ArrayIndexOutOfBoundsException 给定下标越界
     */
    public CollegeStudent getStudent(int idx) throws ArrayIndexOutOfBoundsException {
        return students.get(idx);
    }

    /**
     * 根据学生学号在班级中查找学生
     *
     * @param id 待查找学生的学号
     * @return 找到的学生
     * @throws ArrayIndexOutOfBoundsException 给定学号的学生不在该班级中
     */
    public CollegeStudent getStudent(String id) throws ArrayIndexOutOfBoundsException {
        return getStudent(students.indexOf(new CollegeStudent(id)));
    }

    /**
     * 返回学生人数
     *
     * @return 学生的个数
     */
    public int getClaasSize() {
        return students.size();
    }


    /**
     * 返回该班级的班级号
     *
     * @return 班级号
     */
    public String getClassNum() {
        return class_num;
    }

    /**
     * 格式化输出班级信息以及班级中所有学生信息。
     * 输出格式：
     * [班级信息]学号 学生姓名……
     *
     * @see Class#printClassInfo()
     */
    public void printStuInfoInClass() {
        System.out.println("----------------------------------------");
        printClassInfo();
        if (getClaasSize() == 0) {
            System.out.println("ClassStuff.Class is empty.");
        } else {
            System.out.print('\n');
            for (int i = 0; i < getClaasSize(); ++i) {
                System.out.print(i + 1 + ".\t");
                System.out.println(students.get(i).getStudentID() + ' ' + students.get(i).getName() + '\t');
                //students.get(i).check();
            }
        }
    }

    /**
     * 格式化输出班级信息，
     * Class:班级号 Major:专业名称 Number of students:班级人数
     */
    public void printClassInfo() {
        System.out.println(
                "Class:" + class_num + " Major:" + major_type[major] + " Number of students:" + getClaasSize()
        );
    }


    /**
     * 以班级号为主键判等
     */
    @Override
    public boolean equals(Object cls) {
        if (this == cls)
            return true;
        if (cls == null)
            return false;
        if (this.getClass() != cls.getClass())
            return false;
        else {
            Class stut = (Class) cls;
            return stut.getClassNum().equals(this.getClassNum());
        }
    }


    /**
     * 更新班级学生列表文件和学生选课信息文件
     *
     * @throws IOException
     */
    public void updateClassList() throws IOException {
        for (CollegeStudent stu : students) {
            File file = new File(getStudentListPath());
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file.getPath());
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter info_wtr = new BufferedWriter(osw);
            try {
                stu.updateStuInfo();//逐个学生更新选课信息
                info_wtr.write(stu.getInfo());//向班级名单文件中写入该学生信息
            } catch (IOException e) {
                e.printStackTrace();
            }
            info_wtr.close();
            osw.close();
            fos.close();
        }
    }

    /**
     * 格式化给出包含该班级信息的字符串
     *
     * @return 班级号和专业
     */
    public String getInfo() {
        return class_num + ' ' + major + "\r\n";
    }


    /**
     * 返回该班级学生列表文件的路径字符串
     *
     * @return 学生列表文件的路径
     */
    public String getStudentListPath() {
        return "./data/" + getClassNum() + ".txt";
    }

}
