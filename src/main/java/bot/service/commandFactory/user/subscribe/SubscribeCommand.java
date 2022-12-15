package bot.service.commandFactory.user.subscribe;

import bot.service.DataManager;
import bot.service.Message;
import bot.service.commandFactory.CommandType;
import bot.service.commandFactory.MessageCreator;
import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubscribeCommand implements Command{

    private final String name = "/subscribe";


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

        if(!DataManager.getInstance().isSub(chatId)){
            DataManager.getInstance().reSub("subscribe", chatId);
            btns.add(new HashMap<>(){{
                put("text", "ОТПИСАТЬСЯ");
                put("callback", "/subscribe");
            }});
        }else{
            DataManager.getInstance().reSub("unsubscribe", chatId);
            btns.add(new HashMap<>(){{
                put("text", "ПОДПИСАТЬСЯ");
                put("callback", "/subscribe");
            }});
        }

        btns.add(new HashMap<>(){{
            put("text", "В НАЧАЛО");
            put("callback", "/start");
        }});
        data.add(btns);

        msgs.add(creator.createTextMessage(
                data,
                chatId,
                DataManager.getInstance().isSub(chatId)?"&#9989;Отлично! Я буду держать Вас в курсе!"
                        :"&#9989;Хорошо, я не буду Вам ничего присылать, но вы можете в любой момент подписаться снова!",
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
