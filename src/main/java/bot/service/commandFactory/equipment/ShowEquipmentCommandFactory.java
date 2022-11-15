package bot.service.commandFactory.equipment;

import bot.service.commandFactory.interfaces.Command;
import bot.service.commandFactory.interfaces.CommandFactory;

public class ShowEquipmentCommandFactory implements CommandFactory {
    @Override
    public Command setCommand() {
        return new ShowEquipmentCommand();
    }
}
