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
public class ClientCommandMenu implements Serializable,BaseCommand {
    private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(ClientCommandMenu.class);

    public static final String KEY = BaseCommand.COMMAND_MENU;
    private String allCommandsShow;
    private String commandInput;

    @Override
    public void exec(Scanner scanner) {
        System.err.println("请输入某个操作指令：");
        System.err.println(allCommandsShow);
        //  获取第一个指令
        commandInput = scanner.next();
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "展示所有命令";
    }

    public static String getKEY() {
        return KEY;
    }

    public String getAllCommandsShow() {
        return allCommandsShow;
    }

    public void setAllCommandsShow(String allCommandsShow) {
        this.allCommandsShow = allCommandsShow;
    }

    public String getCommandInput() {
        return commandInput;
    }

    public void setCommandInput(String commandInput) {
        this.commandInput = commandInput;
    }

    @Override
    public String toString() {
        return "ClientCommandMenu{" +
                "allCommandsShow='" + allCommandsShow + '\'' +
                ", commandInput='" + commandInput + '\'' +
                '}';
    }
}
