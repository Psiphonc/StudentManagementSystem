import ClassStuff.Class;
import ClassStuff.Subject;
import People.CollegeStudent;
import People.Teacher;

import java.io.*;
import java.util.Scanner;

public class LauncherWithIO extends Launcher {
    public static void main(String[] args) {
        init();//从文件中读取上次运行的数据
        Scanner in = new Scanner(System.in);
        while (true) {
            if (askStuOrTea()) {//询问用户以教师身份还是学生身份登陆系统
                //如果用户是教师
                do {
                    char optr = showTeacherList();//显示教师列表，并获取下一步操作
                    if (optr == '0')
                        teachers.add(createTeacher());//创建教师对象并加入列表
                    else if (optr == 'q') {
                        try {
                            updateInfo();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
            } else {
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
                            try {
                                updateInfo();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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
                                if (opt == 'q') {
                                    try {
                                        updateInfo();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;//返回上一层
                                } else {
                                    chooseSubject(stu);//学生选课
                                }
                            } while (true);
                            break;//返回上一层
                    }
                } while (true);

            }
        }
    }

    public static void init() {
        try {
            readClass();//读取班级
            readTeacherList();//读取教师列表
            for (Class cls : classes) {//为每个班级读取学生信息
                try {
                    cls.readStudent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//              cls.printStuInfoInClass();
            }
        } catch (IOException e) {
//          e.printStackTrace();
        }

    }


    /**
     * 通过给定班级列表文件新建班级并加入班级列表classes中
     *
     * @throws IOException
     */
    public static void readClass() throws IOException {
        FileInputStream class_list = new FileInputStream("./data/class.txt");
        InputStreamReader isr = new InputStreamReader(class_list);
        BufferedReader class_info = new BufferedReader(isr);
        String class_info_detail;
        while ((class_info_detail = class_info.readLine()) != null) {
            if (class_info_detail.startsWith("\uFEFF"))
                class_info_detail = class_info_detail.replace("\uFEFF", "");
            String[] class_stripped_info = class_info_detail.split(" ");
            Class temp_class = new Class(class_stripped_info[0], Integer.parseInt(class_stripped_info[1]));
            if (classes.indexOf(temp_class) == -1) {
                classes.add(temp_class);
                File dir = new File(temp_class.getClassPath());
                if (!dir.exists())
                    dir.mkdirs();//为新班级创建目录
            }
        }
        class_info.close();
        isr.close();
        class_list.close();
    }

    public static void updateClassInfo() throws IOException {
        File file = new File("./data/class.txt");
        if (file.exists())
            file.delete();
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file.getPath());
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter info_wtr = new BufferedWriter(osw);
        for (Class item : classes) {
            //item.writeStuInfoInClass();
            info_wtr.write(item.getInfo());
            item.writeStuInfoInClass();
        }
        info_wtr.close();
        osw.close();
        fos.close();
    }

    public static void updateTeacherInfo() throws IOException {
        File file = new File("./data/teacher.txt");
        if (file.exists())
            file.delete();
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file.getPath());
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter info_wtr = new BufferedWriter(osw);
        for (Teacher item : teachers) {
            info_wtr.write(item.getInfo());
        }
        info_wtr.close();
        osw.close();
        fos.close();
    }

    public static void updateInfo() throws IOException {
        updateClassInfo();
        updateTeacherInfo();

    }

    public static void readTeacherList() throws IOException {
        File file = new File("./data/teacher.txt");
        if (!file.exists()) {
            file.createNewFile();
            return;
        }
        FileInputStream fis = new FileInputStream(file.getPath());
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String tea_info;
        while ((tea_info = br.readLine()) != null) {
            if (tea_info.startsWith("\uFEFF")) {
                tea_info = tea_info.replace("\uFEFF", "");
            }
            String[] tea_info_detail = tea_info.split(" ");
            Teacher temp_tea = new Teacher(tea_info_detail[1], tea_info_detail[0], Subject.getSubject(tea_info_detail[2]));
            if (teachers.indexOf(temp_tea) == -1)
                teachers.add(temp_tea);
        }
    }

}
