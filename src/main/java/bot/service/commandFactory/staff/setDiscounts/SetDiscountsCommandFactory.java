package bot.service.commandFactory.staff.setDiscounts;

import bot.service.commandFactory.interfaces.Command;
import bot.service.commandFactory.interfaces.CommandFactory;

public class SetDiscountsCommandFactory implements CommandFactory {
    @Override
    public Command setCommand() {
        return new SetDiscountsCommand();
    }
}
