package bot.service.commandFactory.user.SendPostCommand;

import bot.service.commandFactory.interfaces.Command;
import bot.service.commandFactory.interfaces.CommandFactory;

public class SendPostCommandFactory implements CommandFactory {
    @Override
    public Command setCommand() {
        return new SendPostCommand();
    }
}
