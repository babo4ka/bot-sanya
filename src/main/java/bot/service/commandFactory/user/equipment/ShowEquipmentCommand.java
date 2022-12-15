package bot.service.commandFactory.user.equipment;

import bot.service.Message;
import bot.service.commandFactory.CommandType;
import bot.service.commandFactory.MessageCreator;
import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowEquipmentCommand implements Command {

    private String name = "/equip";

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
            put("callback", "/start");
        }});

        data.add(btns);

        msgs.add(creator.createTextMessage(
                data,
                chatId,
                "Покупка нового роутера - 2500 руб\n" +
                        "Покупка б/у роутера - 1350 руб\n" +
                        "Покупка кам-модуля - 1300 руб\n" +
                        "Покупка тв-приставки - 2900 руб",
                true
        ));

        return msgs;
    }

    @Override
    public void setDataManager() {

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
