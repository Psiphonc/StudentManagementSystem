package ClassStuff;

/**
 * 该类表示Java程序设计这门学科，使用单例模式，不能生成对象，只能使用类体内的静态成员INSTANCE
 * 选修课 课程号：6.03
 *
 * @see ClassStuff.Subject
 */
public class JavaProgramming extends Subject {
    private static JavaProgramming INSTANCE = new JavaProgramming();

    private JavaProgramming() {
        super(true);
    }

    static JavaProgramming getInstance() {
        return INSTANCE;
    }

    @Override
    public String getSubjectName() {
        return "ClassStuff.JavaProgramming";
    }

    @Override
    public String getSubID() {
        return "6.03";
    }
}
