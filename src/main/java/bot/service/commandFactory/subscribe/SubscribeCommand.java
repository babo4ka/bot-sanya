package bot.service.commandFactory.subscribe;

import bot.service.DataManager;
import bot.service.DataUpdateListener;
import bot.service.Message;
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
    public List<Message> execute(Update update, String... args) {
        List<Message> msgs = new ArrayList<>();
        SendMessage sm = new SendMessage();
        long chatId = update.hasMessage()?update.getMessage().getChatId():
                update.getCallbackQuery().getMessage().getChatId();
        sm.setChatId(String.valueOf(chatId));

        sm.enableMarkdown(true);
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> btns = new ArrayList<>();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if(!dataManager.isSub(chatId)){
            sm.setText("&#9989;Отлично! Я буду держать Вас в курсе!");
            notifyObservers("subscribe", chatId);

            btns.add(new InlineKeyboardButton().builder()
                    .text("отписаться")
                    .callbackData("/subscribe").build());
        }else{
            sm.setText("&#9989;Хорошо, я не буду Вам ничего присылать, но вы можете в любой момент подписаться снова!");
            notifyObservers("unsubscribe", chatId);
            btns.add(new InlineKeyboardButton().builder()
                    .text("подписаться")
                    .callbackData("/subscribe").build());
        }

        btns.add(new InlineKeyboardButton().builder()
                .text("в начало")
                .callbackData("/start").build());
        rows.add(btns);

        keyboardMarkup.setKeyboard(rows);
        sm.setReplyMarkup(keyboardMarkup);
        sm.setParseMode("HTML");
        Message msg = new Message(Message.MESSAGE);
        msg.setSendMessage(sm);
        msgs.add(msg);
        return msgs;
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
