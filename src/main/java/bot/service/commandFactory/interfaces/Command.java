package bot.service.commandFactory.interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    String[] getArgs();
    SendMessage execute(Update update, String...args);
    void setArgs(String...args);
    String getName();
}
