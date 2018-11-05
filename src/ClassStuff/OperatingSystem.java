package ClassStuff;

/**
 * 该类表示操作系统这门学科，使用单例模式，不能生成对象，只能使用类体内的静态成员INSTANCE
 * 必修课 课程号：6.02
 *
 * @see ClassStuff.Subject
 */
public class OperatingSystem extends Subject {
    private static OperatingSystem INSTANCE = new OperatingSystem();

    private OperatingSystem() {
        super(false);
    }

    static OperatingSystem getInstance() {
        return INSTANCE;
    }

    @Override

    public String getSubjectName() {
        return "Operating System";
    }

    @Override
    public String getSubID() {
        return "6.02";
    }
}
