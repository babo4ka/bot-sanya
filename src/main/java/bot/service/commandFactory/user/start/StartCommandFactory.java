package bot.service.commandFactory.user.start;

import bot.service.commandFactory.interfaces.Command;
import bot.service.commandFactory.interfaces.CommandFactory;

public class StartCommandFactory implements CommandFactory {
    @Override
    public Command setCommand() {
        return new StartCommand();
    }
}
