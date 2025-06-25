package handler;

import entity.EmployeeDO;
import service.EmployeeService;
import service.impl.EmployeeServiceImpl;
import view.LoginView;
import view.MainView;

import javax.swing.*;
import java.awt.event.*;

public class LoginHandler extends KeyAdapter implements ActionListener {
    private final LoginView loginView;

    public LoginHandler(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jButton = (JButton) e.getSource();
        String text = jButton.getText();

        if ("登录".equals(text)) {
            // 从数据库中拉取用户名密码进行校验
            System.out.println("Login");
            login();
        } else if ("注册".equals(text)) {
            // 将用户名密码写入数据库，用户名唯一
            System.out.println("Register");
        }
    }

    private void login() {
        // 拉取用户输入的用户名密码
        String user = loginView.getUserNameTxt().getText();
        char[] chars = loginView.getPwdField().getPassword();
        if (user == null || user.trim().isEmpty() || chars == null || chars.length == 0) {
            JOptionPane.showMessageDialog(loginView, "用户名或密码不能为空");
            return;
        }
        String pwd = new String(chars);

        // 查询DB
        EmployeeService employeeService = new EmployeeServiceImpl();
        EmployeeDO employeeDO = new EmployeeDO();
        employeeDO.setUsername(user);
        employeeDO.setPassword(pwd);

        boolean isSuccess = employeeService.validateEmployee(employeeDO);  // 是否查询成功
        if (isSuccess) {
            employeeDO = employeeService.getEmployeeInfo(user);
            // 进入主界面
            new MainView(employeeDO);
            loginView.dispose();  // 销毁登录page
        } else {
            JOptionPane.showMessageDialog(loginView, "用户名或密码错误");
        }

        System.out.println(user + ":" + pwd);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            login();
        }
    }
}
