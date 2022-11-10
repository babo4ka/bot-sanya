package bot.service.commandFactory.start;

import bot.service.DataManager;
import bot.service.DataUpdateListener;
import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class StartCommand implements Command{

    private final String name = "/start";

    private final String info = "&#128129;Ведущий специалист по домашним услугам ПАО МТС Александр\n" +
            "Агент группы продаж, работаю в компании пятый год, суммарно через меня подключилось более 1000 квартир\n\n" +
            "&#128205;Веду работу по республике Татарстан\n\n" +
            "&#128222;Можете позвонить мне по номеру <strong>8 - 917 - 292 - 84 - 45</strong>\n" +
            "&#128233;Или написать мне в телеграме @ссылка\n\n" +
            "&#128240;Вы также можете подписаться на мой новостной канал @ссылка\n" +
            "Или же подписаться на рассылку и я Вам сам буду присылать новые посты оттуда\n\n" +
            "&#129470;А я его бот-помощник:) через меня вы вкратце можете узнать интересующую Вас информацию\n" +
            "Могу показать Вам тарифы или же сообщу Александру, чтобы он связался с Вами и обсудил все тарифы";

    @Override
    public String[] getArgs() {
        return new String[0];
    }

    DataManager dataManager;
    public void setDataManager(){
        dataManager = DataManager.getInstance();
    }

    @Override
    public List<SendMessage> execute(Update update, String...args) {
        List<SendMessage> sms = new ArrayList<>();
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(update.hasMessage()?String.valueOf(update.getMessage().getChatId())
                :String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        sendMessage.setText(info);

        sendMessage.setReplyMarkup(setKeyboard(update.hasMessage()?update.getMessage().getChatId():
                update.getCallbackQuery().getMessage().getChatId()));
        sendMessage.setParseMode("HTML");
        sms.add(sendMessage);

        return sms;
    }

    @Override
    public void setArgs(String... args) {

    }

    private InlineKeyboardMarkup setKeyboard(long chatId){
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
                .text(dataManager.isSub(chatId)?"отписаться от рассылки из новостного канала":"подписаться на рассылку из новостного канала")
                .callbackData("/subscribe")
                .build());
        btns.add(rows);
        rows = new ArrayList<>();

        rows.add(new InlineKeyboardButton().builder()
                .text("список каналов")
                .callbackData("/channels")
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
