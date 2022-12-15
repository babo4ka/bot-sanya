package bot.service.commandFactory.user.unknown;

import bot.service.Message;
import bot.service.commandFactory.CommandType;
import bot.service.commandFactory.MessageCreator;
import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UnknownCommand implements Command {

    private final String name = "/unknown";

    @Override
    public String[] getArgs() {
        return new String[0];
    }

    MessageCreator creator = new MessageCreator();

    @Override
    public List<Message> execute(Update update, List<String> arguments) {
        List<Message> msgs = new ArrayList<>();

        long chatId = update.hasMessage()?update.getMessage().getChatId()
                :update.getCallbackQuery().getMessage().getChatId();

        List<List<HashMap<String, String>>> data = new ArrayList<>();
        List<HashMap<String, String>> btns = new ArrayList<>();

        btns.add(new HashMap<>(){{
            put("text", "ВЕРНУТЬСЯ В НАЧАЛО");
            put("text", "/start");
        }});
        data.add(btns);

        msgs.add(creator.createTextMessage(
                data,
                chatId,
                "Я Вас не понимаю :(",
                true,
                ""
        ));

        return msgs;
    }

    @Override
    public List<Message> process(Update update, List<String> arguments) {
        return null;
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }


    @Override
    public String getName() {
        return name;
    }
}
