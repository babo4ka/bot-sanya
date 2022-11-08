package bot.service.commandFactory.unknown;

import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class UnknownCommand implements Command {

    private final String name = "/unknown";

    @Override
    public String[] getArgs() {
        return new String[0];
    }

    @Override
    public List<SendMessage> execute(Update update, String... args) {
        List<SendMessage> sms = new ArrayList<>();
        SendMessage sm = new SendMessage();
        sm.setText("Я Вас не понимаю :(");
        sm.setChatId(String.valueOf(update.hasMessage()?update.getMessage().getChatId():update.getCallbackQuery().getMessage().getChatId()));

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rows = new ArrayList<>();
        List<List<InlineKeyboardButton>> btns = new ArrayList<>();

        rows.add(new InlineKeyboardButton().builder()
                .text("вернуться в начало")
                .callbackData("/start").build());
        btns.add(rows);

        keyboardMarkup.setKeyboard(btns);
        sm.enableMarkdown(true);
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
}
