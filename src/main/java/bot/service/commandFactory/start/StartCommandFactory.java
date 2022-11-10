package bot.service.commandFactory.start;

import bot.service.commandFactory.interfaces.Command;
import bot.service.commandFactory.interfaces.CommandFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class StartCommandFactory implements CommandFactory {
    @Override
    public Command setCommand() {
        return new StartCommand();
    }
}
