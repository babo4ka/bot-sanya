package bot.service.commandFactory.tariffs;

import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ShowTariffsCommand implements Command {


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
