package bot.service.commandFactory.SendPostCommand;

import bot.database.entites.Subs;
import bot.service.DataManager;
import bot.service.Message;
import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class SendPostCommand implements Command {

    private final String name = "/sendPost";

    @Override
    public String[] getArgs() {
        return new String[0];
    }

    DataManager dataManager;

    public void setDataManager(){
        this.dataManager = DataManager.getInstance();
    }

    private final String channelId = "-1001379659811";

    @Override
    public List<Message> execute(Update update, String... args) {
        List<Message> msgs = new ArrayList<>();

        if(!update.hasChannelPost() || !String.valueOf(update.getChannelPost().getChatId()).equals(channelId)){
            SendMessage sm = new SendMessage();
            sm.setText("Вы не можете использовать эту команду!");
            sm.setChatId(update.getMessage().getChatId());
            sm.enableMarkdown(true);
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            List<InlineKeyboardButton> btns = new ArrayList<>();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();

            btns.add(new InlineKeyboardButton().builder()
                    .text("в начало")
                    .callbackData("/start").build());
            rows.add(btns);
            keyboardMarkup.setKeyboard(rows);
            sm.setReplyMarkup(keyboardMarkup);
            Message msg = new Message(Message.MESSAGE);
            msg.setSendMessage(sm);
            msgs.add(msg);
        }else{
            if(update.getChannelPost().hasPhoto()){
                for(Subs s : dataManager.getSubsData()){
                    SendPhoto sm = new SendPhoto();
                    sm.setCaption(update.getChannelPost().getText());
                    sm.setPhoto((InputFile) update.getChannelPost().getPhoto());
                    sm.setChatId(s.getID());
                    sm.setParseMode("HTML");
                    Message msg = new Message(Message.MESSAGE);
                    msg.setSendPhoto(sm);
                    msgs.add(msg);
                }
            }else{
                for(Subs s : dataManager.getSubsData()){
                    SendMessage sm = new SendMessage();
                    sm.setText(update.getChannelPost().getText());
                    sm.setChatId(s.getID());
                    sm.setParseMode("HTML");
                    Message msg = new Message(Message.MESSAGE);
                    msg.setSendMessage(sm);
                    msgs.add(msg);
                }
            }

        }

        return msgs;
    }

    @Override
    public void setArgs(String... args) {

    }

    @Override
    public String getName() {
        return name;
    }
}
