package view;

import handler.LoginHandler;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class LoginView extends JFrame {
    URL icoUrl = LoginView.class.getClassLoader().getResource("whms-logo.png");
    ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(icoUrl));
    Image scaledImage = imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
    JLabel titleLabel = new JLabel("企业信息管理系统", new ImageIcon(scaledImage), JLabel.CENTER);

    SpringLayout springLayout = new SpringLayout();
    JPanel centerPanel = new JPanel(springLayout);
    JLabel userNameLabel = new JLabel("用户名：");
    JLabel PwdLabel = new JLabel("密码：");
    JTextField userNameTxt = new JTextField();
    JPasswordField pwdField = new JPasswordField();
    JButton loginBtn = new JButton("登录");
    JButton registerBtn = new JButton("注册");

    LoginHandler loginHandler;

    public JPasswordField getPwdField() {
        return pwdField;
    }


    public JTextField getUserNameTxt() {
        return userNameTxt;
    }


    public LoginView() {
        super("WHMS");
        loginHandler = new LoginHandler(this);
        Container contentPane = getContentPane();

        titleLabel.setFont(new Font(null, Font.PLAIN, 40));
        titleLabel.setPreferredSize(new Dimension(0, 80));

        Font centerFont = new Font("微软雅黑", Font.PLAIN, 20);
        Dimension btnDimension = new Dimension(200, 30);
        userNameLabel.setFont(centerFont);
        userNameTxt.setPreferredSize(btnDimension);  // 设置宽度和高度
        PwdLabel.setFont(centerFont);
        pwdField.setPreferredSize(btnDimension);
        loginBtn.setFont(centerFont);
        registerBtn.setFont(centerFont);

        // 把组件加入面板
        centerPanel.add(userNameLabel);
        centerPanel.add(userNameTxt);
        centerPanel.add(PwdLabel);
        centerPanel.add(pwdField);

        // 增加按键事件
        loginBtn.addActionListener(loginHandler);
        registerBtn.addActionListener(loginHandler);
        loginBtn.addKeyListener(loginHandler);
        registerBtn.addKeyListener(loginHandler);
        centerPanel.add(loginBtn);
        centerPanel.add(registerBtn);

        // 弹簧布局
        layoutCenter();

        // 加入面板
        contentPane.add(titleLabel, BorderLayout.NORTH);
        contentPane.add(centerPanel, BorderLayout.CENTER);

        // 设置loginBtn为默认按钮
        getRootPane().setDefaultButton(loginBtn);

        // 设置ICON
        setIconImage(imageIcon.getImage());
        setSize(600, 400);
        setLocationRelativeTo(null); // 窗口居中
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void layoutCenter() {
        // 弹簧布局
        // userNameLabel
        Spring childWidth = Spring.sum(Spring.sum(Spring.width(userNameLabel), Spring.width(userNameTxt)),
                Spring.constant(20));
        int offsetX = childWidth.getValue() / 2;
        springLayout.putConstraint(SpringLayout.WEST, userNameLabel, -offsetX,
                SpringLayout.HORIZONTAL_CENTER, centerPanel);
        springLayout.putConstraint(SpringLayout.NORTH, userNameLabel, 20,
                SpringLayout.NORTH, centerPanel);
        // userNameTxt
        springLayout.putConstraint(SpringLayout.WEST, userNameTxt, 20,
                SpringLayout.EAST, userNameLabel);
        springLayout.putConstraint(SpringLayout.NORTH, userNameTxt, 0,
                SpringLayout.NORTH, userNameLabel);
        // pwdLabel
        springLayout.putConstraint(SpringLayout.NORTH, PwdLabel, 20, SpringLayout.SOUTH, userNameLabel);
        springLayout.putConstraint(SpringLayout.EAST, PwdLabel, 0, SpringLayout.EAST, userNameLabel);
        // pwdField
        springLayout.putConstraint(SpringLayout.WEST, pwdField, 20,
                SpringLayout.EAST, PwdLabel);
        springLayout.putConstraint(SpringLayout.NORTH, pwdField, 0,
                SpringLayout.NORTH, PwdLabel);
        // loginBtn
        springLayout.putConstraint(SpringLayout.WEST, loginBtn, 50, SpringLayout.WEST, PwdLabel);
        springLayout.putConstraint(SpringLayout.NORTH, loginBtn, 20, SpringLayout.SOUTH, PwdLabel);
        // registerBtn
        springLayout.putConstraint(SpringLayout.WEST, registerBtn, 200, SpringLayout.WEST, PwdLabel);
        springLayout.putConstraint(SpringLayout.NORTH, registerBtn, 20, SpringLayout.SOUTH, PwdLabel);
    }

    public static void main(String[] args) {
        new LoginView();
    }
}
