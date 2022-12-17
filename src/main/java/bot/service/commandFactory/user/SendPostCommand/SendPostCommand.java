package bot.service.commandFactory.user.SendPostCommand;

import bot.database.entites.Subs;
import bot.service.DataManager;
import bot.service.Message;
import bot.service.commandFactory.CommandType;
import bot.service.commandFactory.MessageCreator;
import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendPostCommand implements Command {

    private final String name = "/sendPost";

    @Override
    public String[] getArgs() {
        return new String[0];
    }


    private final String channelId = "-1001788432377";

    MessageCreator creator = new MessageCreator();


    @Override
    public List<Message> execute(Update update, List<String> arguments) {
        List<Message> msgs = new ArrayList<>();

        long chatId = update.hasMessage()?update.getMessage().getChatId()
                :update.getCallbackQuery().getMessage().getChatId();

        List<List<HashMap<String, String>>> data;
        List<HashMap<String, String>> btns;

        if(!update.hasChannelPost() || !String.valueOf(update.getChannelPost().getChatId()).equals(channelId)){
            data = new ArrayList<>();
            btns = new ArrayList<>();

            btns.add(new HashMap<>(){{
                put("text", "В НАЧАЛО");
                put("callback", "/start");
            }});

            data.add(btns);

            msgs.add(creator.createTextMessage(
                    data,
                    chatId,
                    "Вы не можете использовать эту команду!",
                    true,
                    ""
            ));

        }else{
            if(update.getChannelPost().hasPhoto()){
                for(Subs s : DataManager.getInstance().getSubsData()){
                    SendPhoto sm = new SendPhoto();
                    sm.setCaption(update.getChannelPost().getText());
                    sm.setPhoto((InputFile) update.getChannelPost().getPhoto());
                    sm.setChatId(s.getID());
                    sm.setParseMode("HTML");
                    Message msg = new Message(Message.MESSAGE,false, "");
                    msg.setSendPhoto(sm);
                    msgs.add(msg);
                }
            }else{
                for(Subs s : DataManager.getInstance().getSubsData()){
                    SendMessage sm = new SendMessage();
                    sm.setText(update.getChannelPost().getText());
                    sm.setChatId(s.getID());
                    sm.setParseMode("HTML");
                    Message msg = new Message(Message.MESSAGE, false, "");
                    msg.setSendMessage(sm);
                    msgs.add(msg);
                }
            }

        }

        return msgs;
    }

    @Override
    public List<Message> process(Update update, List<String> arguments) {
        return null;
    }

    @Override
    public CommandType getCommandType() {
        return null;
    }


    @Override
    public String getName() {
        return name;
    }
}
