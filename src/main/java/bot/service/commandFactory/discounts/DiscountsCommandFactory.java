package bot.service.commandFactory.discounts;

import bot.service.commandFactory.interfaces.Command;
import bot.service.commandFactory.interfaces.CommandFactory;

public class DiscountsCommandFactory implements CommandFactory {
    @Override
    public Command setCommand() {
        return new DiscountsCommand();
    }
}
