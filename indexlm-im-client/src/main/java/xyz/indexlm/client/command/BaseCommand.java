package xyz.indexlm.client.command;

import java.util.Scanner;

public interface BaseCommand {
    String COMMAND_MENU="0";
    String COMMAND_LOGIN ="1";
    String COMMAND_CHAT ="2";
    String COMMAND_LOGOUT ="10";
    void exec(Scanner scanner);
    String getKey();
    String getTip();
}
