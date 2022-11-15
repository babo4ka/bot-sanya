package bot.service.commandFactory.equipment;

import bot.service.Message;
import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class ShowEquipmentCommand implements Command {

    private String name = "/equip";

    @Override
    public String[] getArgs() {
        return new String[0];
    }

    @Override
    public List<Message> execute(Update update, String... args) {
        List<Message> msgs = new ArrayList<>();

        SendMessage sm = new SendMessage();
        sm.setChatId(update.hasMessage()?String.valueOf(update.getMessage().getChatId())
                :String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        sm.setText("Покупка нового роутера - 2500 руб\n" +
                "Покупка б/у роутера - 1350 руб\n" +
                "Покупка кам-модуля - 1300 руб\n" +
                "Покупка тв-приставки - 2900 руб");

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rows = new ArrayList<>();
        List<List<InlineKeyboardButton>> btns = new ArrayList<>();

        rows.add(new InlineKeyboardButton().builder()
                .text("вернуться в начало")
                .callbackData("/start").build());
        btns.add(rows);

        keyboardMarkup.setKeyboard(btns);

        sm.setReplyMarkup(keyboardMarkup);
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
}
