package bot.service.commandFactory.subscribe;

import bot.service.DataManager;
import bot.service.DataUpdateListener;
import bot.service.Observable;
import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

public class SubscribeCommand implements Command, Observable{

    private final String name = "/subscribe";


    @Override
    public String[] getArgs() {
        return new String[0];
    }


    DataManager dataManager;

    public void setDataManager(){
        this.dataManager = DataManager.getInstance();
    }


    @Override
    public List<SendMessage> execute(Update update, String... args) {
        List<SendMessage> sms = new ArrayList<>();
        SendMessage sm = new SendMessage();
        long chatId =update.hasMessage()?update.getMessage().getChatId():
                update.getCallbackQuery().getMessage().getChatId();
        sm.setChatId(String.valueOf(chatId));

        sm.enableMarkdown(true);
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> btns = new ArrayList<>();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if(!dataManager.isSub(chatId)){
            sm.setText("Отлично! Я буду держать Вас в курсе!");
            notifyObservers("subscribe", chatId);

            btns.add(new InlineKeyboardButton().builder()
                    .text("отписаться")
                    .callbackData("/subscribe").build());
            rows.add(btns);
        }else{
            sm.setText("Хорошо, я не буду Вам ничего присылать, но вы можете в любой момент подписаться снова!");
            notifyObservers("unsubscribe", chatId);
            btns.add(new InlineKeyboardButton().builder()
                    .text("подписаться")
                    .callbackData("/subscribe").build());
            rows.add(btns);
        }

        btns.add(new InlineKeyboardButton().builder()
                .text("в начало")
                .callbackData("/start").build());
        rows.add(btns);

        keyboardMarkup.setKeyboard(rows);
        sm.setReplyMarkup(keyboardMarkup);
        sms.add(sm);
        return sms;
    }

    @Override
    public void setArgs(String... args) {

    }

    @Override
    public String getName() {
        return name;
    }



    DataUpdateListener listener;
    @Override
    public void addObserver(DataUpdateListener listener) {
        if(this.listener == null)
            this.listener = listener;
    }

    @Override
    public void removeObserver(DataUpdateListener listener) {
        if(this.listener != null)
            this.listener = null;
    }

    @Override
    public void notifyObservers(String action, long chatId) {
        listener.update(action, chatId);
    }
}
