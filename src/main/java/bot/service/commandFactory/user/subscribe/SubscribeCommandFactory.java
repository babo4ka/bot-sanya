package bot.service.commandFactory.user.subscribe;

import bot.service.commandFactory.interfaces.Command;
import bot.service.commandFactory.interfaces.CommandFactory;

public class SubscribeCommandFactory implements CommandFactory {
    @Override
    public Command setCommand() {
        return new SubscribeCommand();
    }
}
