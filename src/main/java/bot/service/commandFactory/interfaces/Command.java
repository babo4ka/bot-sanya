package bot.service.commandFactory.interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface Command{
    String[] getArgs();
    List<SendMessage> execute(Update update, String...args);
    void setArgs(String...args);
    String getName();
}
