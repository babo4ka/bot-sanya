package bot.service.commandFactory.interfaces;

import bot.service.Message;
import bot.service.commandFactory.CommandType;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface Command{
    String[] getArgs();
    List<Message> execute(Update update, List<String> arguments);
    CommandType getCommandType();
    String getName();
}
