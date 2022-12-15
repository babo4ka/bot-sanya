package bot.service.commandFactory.user.consultation;

import bot.service.Message;
import bot.service.commandFactory.CommandType;
import bot.service.commandFactory.MessageCreator;
import bot.service.commandFactory.interfaces.Command;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@PropertySource("app.properties")
public class GetConsultationCommand implements Command {

    private final String name = "/consultation";


    @Value("${bot.owner}")
    private long ownerId;
    @Value("${bot.subowner}")
    private long subOwner;


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

        String tariffName = "";

        for(String s : arguments){
            tariffName = String.join(" ", tariffName, s);
        }

        btns.add(new HashMap<>(){{
            put("text", "ВЕРНУТЬСЯ В НАЧАЛО");
            put("callback", "/start");
        }});

        data.add(btns);

        msgs.add(creator.createTextMessage(
                data,
                chatId,
                "&#9989;Отлично, я свяжусь с Вами в телеграме в ближайшее время!",
                true,
                ""
        ));


        Message msg_s = new Message(Message.MESSAGE, false, "");
        msg_s.setSendMessage(messageForSanya(update.hasMessage()?update.getMessage().getFrom().getUserName():
                    update.getCallbackQuery().getFrom().getUserName(), tariffName));
        msgs.add(msg_s);
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


    private SendMessage messageForSanya(String clientUserName, String tariffName){
        SendMessage sm = new SendMessage();
        sm.setChatId(String.valueOf(ownerId));
        sm.setText(((tariffName.equals("alltariffs")?"Консультация по всем тарифам":"Консультация по тарифу " + tariffName) + "\n" +
                "@" + clientUserName));

        return sm;
    }

    @Override
    public String getName() {
        return this.name;
    }


}
