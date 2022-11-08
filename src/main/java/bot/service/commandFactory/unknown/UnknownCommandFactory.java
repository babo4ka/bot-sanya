package bot.service.commandFactory.unknown;

import bot.service.commandFactory.interfaces.Command;
import bot.service.commandFactory.interfaces.CommandFactory;

public class UnknownCommandFactory implements CommandFactory {
    @Override
    public Command setCommand() {
        return new UnknownCommand();
    }
}
