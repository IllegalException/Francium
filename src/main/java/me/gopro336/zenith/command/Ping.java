package me.gopro336.zenith.command;

import me.gopro336.zenith.Client;
import me.gopro336.zenith.util.LoggerUtil;

public class Ping
extends Command {
    public Ping(String name, String[] alias, String usage) {
        super(name, alias, usage);
    }

    public void OnTrigger(String arguments) {
        if (arguments.equals("")) {
            this.printUsage();
            return;
        }
        LoggerUtil.sendMessage("Pong!");
    }
}
