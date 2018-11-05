package ClassStuff;

/**
 * 该类表示数据结构这门学科，使用单例模式，不能生成对象，只能使用类体内的静态成员INSTANCE
 * 必修课 课程号：6.01
 *
 * @see ClassStuff.Subject
 */
public class DSA extends Subject {
    private static DSA INSTANCE = new DSA();

    private DSA() {
        super(false);
    }

    static DSA getInstance() {
        return INSTANCE;
    }

    @Override
    public String getSubjectName() {
        return "Data Structure and Algorithm";
    }

    @Override
    public String getSubID() {
        return "6.01";
    }


}
