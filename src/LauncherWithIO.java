import ClassStuff.Class;
import ClassStuff.Subject;
import People.Teacher;

import java.io.*;
import java.util.Scanner;

/**
 * 这是一个为管理系统实现状态保存和读取上次关闭时状态功能的类。
 * 继承自Launcher类，在launcher类已有功能的基础上，额外添加了I/O功能。
 * 具体实现方法是使用Stream类中的方法，
 * 在程序启动时从硬盘读取格式化输出的txt文件中的信息到内存；
 * 在程序退出时，对硬盘上已有状态文件统一删除，
 * 并重写当前内存中的信息到硬盘。
 *
 * @see Launcher
 */
public class LauncherWithIO extends Launcher {
    public static void main(String[] args) {
        init();//从文件中读取上次运行的数据
        Scanner in = new Scanner(System.in);
        Launcher.begin();
        try {
            updateInfo();//更新文件以便下次读取
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("Status saved successfully.");
    }


    /**
     * 从硬盘中读取上次程序运行所保存的数据（若没有会自动创建），
     * 初始化学生、教师列表以及选课信息。
     */
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
        while ((class_info_detail = class_info.readLine()) != null) {//读取文件信息到临时字符串
            if (class_info_detail.startsWith("\uFEFF"))//去除windows行标﻿﻿﻿\uFEFF
                class_info_detail = class_info_detail.replace("\uFEFF", "");
            String[] class_stripped_info = class_info_detail.split(" ");//以空格为标志切片字符串
            Class temp_class = new Class(class_stripped_info[0], Integer.parseInt(class_stripped_info[1]));
            if (classes.indexOf(temp_class) == -1) {//检查该学生是否已经存在在列表中
                classes.add(temp_class);
                File dir = new File(temp_class.getClassPath());
                if (!dir.exists())//为新加入的班级目录是否存在
                    dir.mkdirs();//为新班级创建目录
            }
        }
        class_info.close();
        isr.close();
        class_list.close();
    }

    /**
     * 更新班级列表和学生选课信息文件
     *
     * @throws IOException
     */
    public static void updateClassInfo() throws IOException {
        File file = new File("./data/class.txt");
        if (file.exists())//检查文件是否存在
            file.delete();//删除班级文件
        file.createNewFile();//新建班级文件
        FileOutputStream fos = new FileOutputStream(file.getPath());
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter info_wtr = new BufferedWriter(osw);
        for (Class item : classes) {//遍历班级列表
            //item.writeStuInfoInClass();
            info_wtr.write(item.getInfo());//写入班级信息
            item.writeStuInfoInClass();//更新学生选课信息
        }
        info_wtr.close();
        osw.close();
        fos.close();
    }


    /**
     * 更新内存中的教师列表信息到硬盘上的teacher.txt
     *
     * @throws IOException
     */
    public static void updateTeacherInfo() throws IOException {
        File file = new File("./data/teacher.txt");
        if (file.exists())//检查文件教师列表文件是否存在
            file.delete();//删除旧列表文件
        file.createNewFile();//创建新列表
        FileOutputStream fos = new FileOutputStream(file.getPath());
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter info_wtr = new BufferedWriter(osw);
        for (Teacher item : teachers) {
            info_wtr.write(item.getInfo());//写入教师信息到文件中
        }
        info_wtr.close();
        osw.close();
        fos.close();
    }

    /**
     * 同时更新教师、班级、学生选课信息
     *
     * @throws IOException
     */
    public static void updateInfo() throws IOException {
        updateClassInfo();
        updateTeacherInfo();
    }


    /**
     * 从硬盘中的教师列表文件读取教师信息到教师列表
     *
     * @throws IOException
     */
    public static void readTeacherList() throws IOException {
        File file = new File("./data/teacher.txt");
        if (!file.exists()) {//确认教师文件是否存在
            file.createNewFile();//创建教师文件
            return;
        }
        FileInputStream fis = new FileInputStream(file.getPath());
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String tea_info;
        while ((tea_info = br.readLine()) != null) {
            if (tea_info.startsWith("\uFEFF")) {
                tea_info = tea_info.replace("\uFEFF", "");//去除windows行标识符
            }
            String[] tea_info_detail = tea_info.split(" ");//以空格为标志切片字符串
            Teacher temp_tea = new Teacher(tea_info_detail[1], tea_info_detail[0], Subject.getSubject(tea_info_detail[2]));
            if (teachers.indexOf(temp_tea) == -1)//确认该教师原先不存在
                teachers.add(temp_tea);
        }
    }

}
