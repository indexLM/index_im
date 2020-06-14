package xyz.indexlm.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Scanner;

/**
 * @Description
 * @Author LiuMing
 * @Date 2020/6/12
 */
@Component
public class LoginConsoleCommand implements Serializable,BaseCommand {
    private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(LoginConsoleCommand.class);

    public static final String KEY = BaseCommand.COMMAND_LOGIN;
    private String userName;
    private String password;
    @Override
    public void exec(Scanner scanner) {
        System.out.println("请输入用户信息(id:password)  ");
        String[] info = null;
        while (true) {
            String input = scanner.next();
            info = input.split(":");
            if (info.length != 2) {
                System.out.println("请按照格式输入(id:password):");
            } else {
                break;
            }
        }
        userName = info[0];
        password = info[1];
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "登陆";
    }

    public static String getKEY() {
        return KEY;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginConsoleCommand{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
