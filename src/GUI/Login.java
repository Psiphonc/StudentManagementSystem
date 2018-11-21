package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JPanel framePnl;
    private JPanel optionBoxPnl;
    private JPanel roleBtnPnl;
    private JPanel loginBtnPnl;
    private JRadioButton studentRadioButton;
    private JRadioButton teacherRadioButton;
    private JButton loginButton;
    private JButton cancelButton;
    private JPanel infoBoxPnl;
    private JPanel loginPnl;
    private JTextField accTextField;
    private JPasswordField passTextField;
    private JButton registerButton;

    private void initFrame(){
        this.setContentPane(this.framePnl);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        studentRadioButton.setSelected(true);
        cancelButton.addActionListener(e -> System.exit(0));
        loginButton.addActionListener(new LoginActionListener());
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isTeacher()) {
                    StuRegisterDialog stuRegisterDialog = new StuRegisterDialog();
                    stuRegisterDialog.pack();
                    stuRegisterDialog.setVisible(true);
                }else{

                }
            }
        });
        this.pack();
    }

    public Login() throws HeadlessException {
        initFrame();

    }

    private void showMessage(String m){
        new JOptionPane().showMessageDialog(framePnl,m);
    }

    private boolean isTeacher(){
        return teacherRadioButton.isSelected();
    }

    public static void main(String[] args) {
        Login login = new Login();
        login.setVisible(true);
    }

    private class LoginActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String acc=accTextField.getText();
            String pass=new String(passTextField.getPassword());

            if(acc.equals("")||pass.equals("")){
                showMessage("账号或密码不能为空");
                return;
            }

            if(isTeacher()){

            }
        }

    }


}
