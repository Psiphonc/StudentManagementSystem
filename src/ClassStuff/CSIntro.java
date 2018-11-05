package ClassStuff;

/**
 * 该类表示计算机科学导论这门学科，使用单例模式，不能生成对象，只能使用类体内的静态成员INSTANCE
 * 必修课 课程号：6.00
 *
 * @see ClassStuff.Subject
 */
public class
CSIntro extends Subject {
    private static CSIntro INSTANCE = new CSIntro();

    private CSIntro() {
        super(false);
    }

    static CSIntro getInstance() {
        return INSTANCE;
    }

    @Override
    public String getSubjectName() {
        return "Computer Science Introduction";
    }

    @Override
    public String getSubID() {
        return "6.00";
    }

}
