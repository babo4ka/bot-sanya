package bot.service.commandFactory.start;

import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class StartCommand implements Command {

    private final String name = "/start";

    private final String info = "Ведущий специалист по домашним услугам ПАО МТС Александр\n" +
            "Агент группы продаж, работаю в компании пятый год, суммарно через меня подключилось более 1000 квартир\n" +
            "Веду работу по республике Татарстан\n" +
            "8 - 917 - 292 - 84 - 45\n" +
            "ссылка на тг\n" +
            "А я его бот-помощник:) через меня вы вкратце можете узнать интересующую Вас информацию";

    @Override
    public String[] getArgs() {
        return new String[0];
    }

    @Override
    public SendMessage execute(Update update, String...args) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        System.out.println(update);
        sendMessage.setChatId(update.hasMessage()?String.valueOf(update.getMessage().getChatId())
                :String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        sendMessage.setText(info);

        sendMessage.setReplyMarkup(setKeyboard());
        return sendMessage;
    }

    private InlineKeyboardMarkup setKeyboard(){
        InlineKeyboardMarkup keyBoard = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> rows = new ArrayList<>();
        List<List<InlineKeyboardButton>> btns = new ArrayList<>();

        rows.add(new InlineKeyboardButton().builder()
                .text("покажи все тарифы")
                .callbackData("/showTariffs")
                .build());
        btns.add(rows);
        rows = new ArrayList<>();
        rows.add(new InlineKeyboardButton().builder()
                .text("посмотреть тарифы выборочно")
                .callbackData("/showTariffs")
                .build());
        btns.add(rows);
        rows = new ArrayList<>();
        rows.add(new InlineKeyboardButton().builder()
                .text("получить консультацию по всем тарифам")
                .callbackData("/consultation alltariffs")
                .build());
        btns.add(rows);

        keyBoard.setKeyboard(btns);
        return keyBoard;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
