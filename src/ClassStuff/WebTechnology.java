package ClassStuff;

/**
 * 该类表示操作系统这门学科，使用单例模式，不能生成对象，只能使用类体内的静态成员INSTANCE
 * 选修课 课程号：6.04
 *
 * @see ClassStuff.Subject
 */
public class WebTechnology extends Subject {
    private static WebTechnology INSTANCE = new WebTechnology();

    private WebTechnology() {
        super(true);
    }

    static WebTechnology getInstance() {
        return INSTANCE;
    }

    @Override
    public String getSubjectName() {
        return "WEB Technology";
    }

    @Override
    public String getSubID() {
        return "6.04";
    }
}
