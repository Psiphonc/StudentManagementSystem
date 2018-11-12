package ClassStuff;

/**
 * 该类表示学生的平均成绩，虽然继承自Subject，但它不能被教师选择（teacher句柄始终为null）。
 * 每个学生在初始化选课表时会自动添加本类对象到选课表。
 * 通过学生类中配套的updateAVG()方法来更新成绩。
 * 可以通过SubID ”AVG“来获取句柄。
 *
 * @see ClassStuff.Subject
 */
public class AVG extends Subject {
    private AVG() {
        super(false);
    }

    private static AVG INSTANCE = new AVG();

    static AVG getInstance() {
        return INSTANCE;
    }

    @Override
    public String getSubjectName() {
        return "Average";
    }

    @Override
    public String getSubID() {
        return "AVG";
    }
}
