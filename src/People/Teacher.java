package People;

import ClassStuff.Subject;

/**
 * 该类表示老师，每个老师都任教一个学科，所以类内部有成员subjcet。
 * 字符串型成员name和id分别表示姓名和教师号。
 * 老师可以使用givemark和work方法给其任教学科下的学生打分。
 *
 * @see Subject
 * @see CollegeStudent
 * @see People.Person
 */
public class Teacher implements Person {

    private String name;
    private String id;
    private Subject subject;
    private String password;

    /**
     * 根据给出的姓名、教师编号和任教学科构造教师对象，
     * 同时把相应学科的任教老师设置成自己。
     *
     * @param id   教师编号
     * @param name 姓名
     * @param sbj  学科
     * @see Subject#setTeacher(Teacher)
     */
    public Teacher(String name, String id, Subject sbj) {
        this.name = name;
        this.id = id;
        subject = sbj;
        sbj.setTeacher(this);
    }

    public Teacher(String name, String id, Subject subject, String password, String admin) {
        this(name, id, subject);
        this.password = password;
        grantAdmin(admin);
    }


    public Teacher(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Subject getSubject() {
        return subject;
    }

    /**
     * 本方法给该教师任教的学科下的所有学生随机打分，
     * 只为了实现题目的要求，并没有实际作用。
     */
    @Override
    public void work() {
        for (CollegeStudent s : this.subject.students) {
            giveMark(s, Math.random() * 100);//随机数
        }
    }

    /**
     * 给任教学科下的一个学生打分
     *
     * @param stu  待打分学生
     * @param mark 分数
     */
    public void giveMark(CollegeStudent stu, Double mark) throws NumberFormatException {
        if (!(mark >= 0 && mark <= 100))
            throw new NumberFormatException();
        if (stu.subjects.containsKey(this.subject)) {//检查这个学生有没有选自己任教的课
            stu.subjects.put(this.subject, mark);
            stu.updateAVG();
        } else {
            System.out.println("The student did not choose this course ");
        }

    }

    /**
     * 格式化输出任教学科下所有学生的成绩
     *
     * @see CollegeStudent#check(Subject)
     */
    @Override
    public void check() {
        if (this.subject.students.size() == 0) {
            System.out.println("There is no students in your subject.");
            return;
        }
        for (CollegeStudent s : this.subject.students) {
            //System.out.print(s.getStudentID()+' '+s.getName()+'\t');
            s.check(getSubject());
        }
    }

    /**
     * 格式化输出教师信息，输出格式：教师ID 教师姓名 任教学科名
     */
    public void showTeacherInformation() {
        System.out.println(getId() + ' ' + getName() + ' ' + getSubject().getSubjectName());
    }


    /**
     * 以教师编号为主键的判等操作
     *
     * @param obj 比较对象
     * @return true-相等 false-不相等
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        else {
            Teacher tea = (Teacher) obj;
            return tea.getId().equals(this.getId());
        }
    }


    /**
     * 以字符串形式返回教师信息
     * 教师编号 姓名 任教科目编号
     *
     * @return 教师信息
     */
    public String getInfo() {
        return getId() + ' ' + getName() + ' ' +
                getSubject().getSubID() + ' ' + password +
                ' ' + getAdmin() + "\r\n";
    }

    public String getPass() {
        return password;
    }

    public void grantAdmin(String stu_id) {
        if (stu_id.equals(""))
            return;
        int idx = subject.getStudents().indexOf(new CollegeStudent(stu_id));
        subject.setAdmin(subject.getStudents().get(idx));
    }

    public String getAdmin() {
        CollegeStudent t_stu = subject.getAdmin();
        String ret = "";
        if (t_stu != null)
            ret = t_stu.getStudentID();
        return ret;
    }
}

