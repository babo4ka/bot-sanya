package bot.service.commandFactory.user.unknown;

import bot.service.Message;
import bot.service.commandFactory.CommandType;
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
    public List<Message> execute(Update update, String... args) {
        List<Message> msgs = new ArrayList<>();
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
        Message msg = new Message(Message.MESSAGE, true);
        msg.setSendMessage(sm);
        msgs.add(msg);

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
    public void setArgs(String... args) {

    }

    @Override
    public String getName() {
        return name;
    }
}
