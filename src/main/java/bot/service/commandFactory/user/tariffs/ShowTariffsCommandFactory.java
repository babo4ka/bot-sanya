package bot.service.commandFactory.user.tariffs;

import bot.service.commandFactory.interfaces.Command;
import bot.service.commandFactory.interfaces.CommandFactory;

public class ShowTariffsCommandFactory implements CommandFactory {
    @Override
    public Command setCommand() {
        return new ShowTariffsCommand();
    }
}
