package bot.service.commandFactory.subscribe;

import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class SubscribeCommand implements Command {

    private final String name = "/subscribe";

    @Override
    public String[] getArgs() {
        return new String[0];
    }

    @Override
    public List<SendMessage> execute(Update update, String... args) {
        return null;
    }

    @Override
    public void setArgs(String... args) {

    }

    @Override
    public String getName() {
        return name;
    }
}
