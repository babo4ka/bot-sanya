package bot.service.commandFactory.SendPostCommand;

import bot.database.entites.Subs;
import bot.service.DataManager;
import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
    public List<SendMessage> execute(Update update, String... args) {
        List<SendMessage> sms = new ArrayList<>();

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
            sms.add(sm);
        }else{

            for(Subs s : dataManager.getSubsData()){
                SendMessage sm = new SendMessage();
                sm.setText(update.getChannelPost().getText());
                sm.setChatId(s.getID());
                sm.setParseMode("HTML");
                sms.add(sm);
            }
        }

        return sms;
    }

    @Override
    public void setArgs(String... args) {

    }

    @Override
    public String getName() {
        return name;
    }
}
