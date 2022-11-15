package bot.service.commandFactory.interfaces;

import bot.service.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface Command{
    String[] getArgs();
    List<Message> execute(Update update, String...args);

    void setArgs(String...args);
    String getName();
}
