package bot.service.commandFactory.tags;

import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SetTagCommand implements Command {


    @Override
    public String[] getArgs() {
        return new String[0];
    }

    @Override
    public SendMessage execute(Update update, String... args) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
