import ClassStuff.Class;
import ClassStuff.Subject;
import People.CollegeStudent;
import People.Teacher;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * 项目主类，实现了用户的交互，
 * 内部包含班级和老师数组字段，用于保存用户创建的班级和老师。
 *
 * @see Teacher
 * @see Class
 * @see ArrayList
 */
public class Launcher {
    /**
     * 存放所有教师的数组
     */
    static ArrayList<Teacher> teachers = new ArrayList<Teacher>();
    /**
     * 存放所有班级的数组
     */
    static ArrayList<Class> classes = new ArrayList<Class>();

    public static void main(String[] args) {
        begin();
    }

    public static void begin() {
        Scanner in = new Scanner(System.in);
        while (true) {
            char op = askStuOrTea();
            if (op == '1') {//询问用户以教师身份还是学生身份登陆系统
                //如果用户是教师
                do {
                    char optr = showTeacherList();//显示教师列表，并获取下一步操作
                    if (optr == '0')
                        teachers.add(createTeacher());//创建教师对象并加入列表
                    else if (optr == 'q') {
                        break;//返回上一步
                    } else {
                        Teacher tea;
                        try {
                            tea = teachers.get(optr - 1 - '0');//在教师列表中获取用户选择的教师
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("This teacher does not exist!");//用户选择的下标越界
                            continue;
                        }
                        tea.showTeacherInformation();//显示老师的详细信息
                        studentViewForTeacher(tea);//显示学生列表并询问下一步操作（添加学或打分）
                    }
                } while (true);
            } else if (op == '2') {
                //若用户是学生
                label:
                do {
                    showClasses();//显示班级列表
                    System.out.print("0 for creating a new class,'q' for quitting,class id for entering that class:");
                    String optr = in.nextLine();//询问用户操作
                    switch (optr) {
                        case "0":
                            createClass();//创建班级
                            break;
                        case "q":
                            break label;//返回上一层
                        default:
                            Class cls;
                            try {
                                cls = classes.get(classes.indexOf(new Class(optr)));//在班级列表查找用户输入的学生
                            } catch (IndexOutOfBoundsException e) {
                                cls = createClassWhenExceptionHappen(optr);//该班级号不存在，询问用户是否创建这个班级
                                if (cls == null)
                                    continue;//不创建
                            }
                            do {
                                CollegeStudent stu = studentViewForStudent(cls);//显示学生列表并询问进一步操作（创建或选择学生）
                                char opt;
                                System.out.print("'q' for quitting,anything else to choose subject:");
                                opt = in.nextLine().charAt(0);//询问用户操作
                                in.reset();
                                if (opt == 'q')
                                    break;//返回上一层
                                else
                                    chooseSubject(stu);//学生选课
                            } while (true);
                            break;//返回上一层
                    }
                } while (true);
            } else {
                break;
            }
        }
    }

    /**
     * 根据用户输入课程号为学生选课
     *
     * @param stu 选课的学生
     */
    public static void chooseSubject(CollegeStudent stu) {
        Scanner in = new Scanner(System.in);
        System.out.println("Here are all the subjects available to choose:");
        Subject.showElectiveSubList();//显示选修课列表
        System.out.print("subject id for choose this subject,0 for quit without doing anything:");
        do {
            String optr = in.nextLine();
            if (!optr.equals("0")) {
                Subject sbj = Subject.getSubject(optr);//查找课程
                if (sbj != null)
                    stu.addSubject(sbj);//向学生课程表中添加课程
                else {
                    System.out.println("This subject does not exist!");//输入有误，再次输入
                    continue;
                }
                break;
            } else
                break;
        } while (true);
    }

    /**
     * 向用户打印该班级的学生列表，
     * 并让用户创建或选择学生
     * 打印该生成绩
     *
     * @param cls 操作班级
     * @return 新创建或选择的学生
     */
    public static CollegeStudent studentViewForStudent(Class cls) {
        cls.showSortedList();//打印该班级信息和其中所有学生
        System.out.println("0 for add new student,student id for further manipulation:");
        Scanner in = new Scanner(System.in);
        CollegeStudent stu;
        do {
            String optr = in.nextLine();
            if (optr.equals("0")) {
                stu = createStudent(cls);//创建新学生
                break;
            } else {
                try {
                    stu = cls.getStudent(optr);//查找这个学号的学生
                } catch (Exception e) {
                    System.out.println("This student id does not exist in this class.");//学号输入有误，重新输入
                    continue;
                }
                break;
            }
        } while (true);
        stu.check();//打印选择学生的各科分数
        return stu;
    }

    /**
     * 打印该老师任教学科下所有学生的成绩，
     * 创建新学生或给已存在学生打分。
     *
     * @param tea 操作老师
     */
    public static void studentViewForTeacher(Teacher tea) {
        Scanner in = new Scanner(System.in);
        do {
            System.out.println("-------------------------------");
            tea.check();//打印该教师所有学生的成绩
            System.out.print("0 for add new student,student id to give student mark,q for back to teacher list:");
            String optr = in.nextLine();
            switch (optr) {
                case "0":
                    createStudentByTeacher(tea.getSubject());//创建新学生
                    break;
                case "q":
                    return;//返回上层
                default:
                    int idx = tea.getSubject().students.indexOf(new CollegeStudent(optr));//获得该学生在学生列表中的下标
                    if (idx == -1)//学生不存在
                        System.out.println("student:" + optr + " does not exists in your list!");
                    else {
                        CollegeStudent stu = tea.getSubject().students.get(idx);
                        stu.printInfo();//打印学生信息
                        while (true) {
                            System.out.print("give a mark:");
                            double mark = in.nextDouble();
                            in.nextLine();
                            try {
                                tea.giveMark(stu, mark);//打分
                            } catch (NumberFormatException e) {
                                System.out.println("Error:Marks must remain between 0-100!Please try it again.");
                                continue;
                            }
                            break;
                        }
                        stu.check(tea.getSubject());//打印分数
                    }
                    break;
            }
        } while (true);
    }

    /**
     * 格式化输出班级列表中所有班级信息
     * 输出格式：idx.[班级信息]
     *
     * @see Class#printClassInfo()
     */
    public static void showClasses() {
        if (classes.size() == 0) {
            System.out.println("There is no class in our list,plz create 1 class at least!");
            return;
        }
        for (int i = 0; i < classes.size(); ++i) {
            System.out.print(i + 1 + ".\t");//add index before class information
            classes.get(i).printClassInfo();
        }
    }

    /**
     * 由用户输入班级号和专业创建新班级
     *
     * @return 返回刚创建的班级
     * @see Launcher#createClass(String, int)
     */
    public static Class createClass() {
        Scanner in = new Scanner(System.in);
        System.out.print("Class ID:");
        String class_id = in.nextLine();
        return createClass(class_id);

    }

    /**
     * 由用户输入专业和传入参数班级号创建新班级
     *
     * @param class_id 班级号
     * @return 返回刚创建的班级
     * @see Launcher#createClass(String, int)
     */
    public static Class createClass(String class_id) {
        Scanner in = new Scanner(System.in);
        System.out.print("(0forCS,1forEE,2forEnglish)Major:");
        int major = in.nextInt();
        in.reset();
        return createClass(class_id, major);
    }

    /**
     * 由传入参数班级号和专业创建新班级
     *
     * @param major    专业代号
     * @param class_id 班级号
     * @return 返回刚创建的班级
     * @see Launcher#createClass(String, int)
     */
    public static Class createClass(String class_id, int major) {
        classes.add(new Class(class_id, major));
        classes.get(classes.size() - 1).printClassInfo();
        System.out.println("Class created successfully");
        return classes.get(classes.size() - 1);
    }

    /**
     * 由用户输入的学号姓名来创建新学生并加入参数班级
     *
     * @param cls 把学生创建在班级cls中
     * @return 刚创建好的学生
     */
    public static CollegeStudent createStudent(Class cls) {
        Scanner in = new Scanner(System.in);
        System.out.print("Student ID:");
        String stu_id = in.nextLine();
        System.out.print("Name:");
        String name = in.nextLine();
        return cls.addStudent(new CollegeStudent(stu_id, name, cls.getClassNum()));
    }

    /**
     * 由老师创建新学生，
     * 若班级不存在亦可直接创建班级。
     * 并为新创建的学生选上该任课老师的课程
     *
     * @param sbj 该老师所任课程
     */
    public static void createStudentByTeacher(Subject sbj) {
        showClasses();//显示班级列表
        System.out.print("Which class would you like to join?(0 for create a new one)");
        Scanner in = new Scanner(System.in);
        String class_id = in.nextLine();
        Class temp;
        if (class_id.equals("0"))//创建班级
            temp = createClass();
        else {
            try {
                temp = classes.get(classes.indexOf(new Class(class_id)));//根据用户输入查找班级
            } catch (IndexOutOfBoundsException e) {
                temp = createClassWhenExceptionHappen(class_id);//用户输入的班级不存在 询问是否创建
                if (temp == null)
                    return;//不创建
            }
        }
        CollegeStudent stu = createStudent(temp);//在上面得到的班级中创建学生
        stu.addSubject(sbj);//为上面创建的学生选这位老师所任的课程
        System.out.println("Student added successfully.");
    }

    /**
     * 格式化输出教师列表，并询问操作
     * 输出格式：Here is the teacher list:idx.[教师信息]……
     *
     * @return 用户输入的操作数
     * @see Teacher#showTeacherInformation()
     */
    public static char showTeacherList() {
        char ret;
        if (teachers.size() == 0) {
            System.out.println("There is no teacher in our list,plz create one.");
            return '0';
        }
        System.out.println("Here is the teacher list:");
        for (int i = 0; i < teachers.size(); ++i) {
            Teacher tea = teachers.get(i);
            System.out.print((i + 1) + ". ");
            tea.showTeacherInformation();
        }
        System.out.println("0 for add you in our list,'q' for quit list,other number to choose you for further manipulation.");
        Scanner in = new Scanner(System.in);
        ret = in.nextLine().charAt(0);
        in.reset();
        return ret;
    }

    /**
     * 显示欢迎信息并询问用户以老师或学生身份登陆
     *
     * @return 0-退出 1-老师 2-学生
     */
    public static char askStuOrTea() {
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to student manage system!");
        System.out.println("0 for save current statue and quit the system;");
        System.out.println("1 stands for logging as a teacher;");
        System.out.println("2 stands for logging as a student;");
        do {
            char c = in.nextLine().charAt(0);
            in.reset();
            if (c == '0' || c == '1' || c == '2')
                return c;
            else
                System.out.println("Only answer y/n!");
        } while (true);
    }

    /**
     * 根据用户输入的信息创建老师并加入教师列表
     *
     * @return 刚刚创建的老师
     */
    public static Teacher createTeacher() {
        Scanner in = new Scanner(System.in);
        System.out.print("name:");
        String name = in.nextLine();
        System.out.print("id:");
        String id = in.nextLine();
        Subject.showSubList();
        System.out.print("Subject:");
        String subid = in.nextLine();
        Subject sbj = Subject.getSubject(subid);
        sbj.setTeacher(new Teacher(name, id, sbj));
        return sbj.getTeacher();
    }

    /**
     * 当用户输入的班级号不在班级列表中时，
     * 询问用户是否以该班级号新建班级
     *
     * @param class_id 不存在的班级号
     * @return 用户若同意创建返回创建好的班级，若不同意则返回空句柄。
     */
    public static Class createClassWhenExceptionHappen(String class_id) {
        Scanner in = new Scanner(System.in);
        System.out.println("This class does not exists,Would you like to create?'y' to create.");
        char op = in.nextLine().charAt(0);
        in.reset();
        if (op == 'y')
            return createClass(class_id);
        else
            return null;
    }


}
