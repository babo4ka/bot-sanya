package bot.service.commandFactory.user.start;

import bot.service.DataManager;
import bot.service.DataUpdateListener;
import bot.service.Message;
import bot.service.commandFactory.CommandType;
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
            "Могу показать Вам тарифы или же сообщу Александру, чтобы он связался с Вами и обсудил все тарифы\n\n" +
            "&#8252;Убедитесь, что у Вас указано имя пользователя в телеграмм(username) или я не смогу сообщить Александру с кем надо связаться";

    @Override
    public String[] getArgs() {
        return new String[0];
    }

    DataManager dataManager;
    public void setDataManager(){
        dataManager = DataManager.getInstance();
    }

    private final long ownerId = 268932900;

    @Override
    public List<Message> execute(Update update, String...args) {
        List<Message> msgs = new ArrayList<>();
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        long chatId = update.hasMessage()?update.getMessage().getChatId()
                :update.getCallbackQuery().getMessage().getChatId();

        sendMessage.setChatId(chatId);
        sendMessage.setText(info);

        sendMessage.setReplyMarkup(setKeyboard(update.hasMessage()?update.getMessage().getChatId():
                update.getCallbackQuery().getMessage().getChatId()));
        sendMessage.setParseMode("HTML");
        Message msg = new Message(Message.MESSAGE, true);
        msg.setSendMessage(sendMessage);
        msgs.add(msg);

        if(chatId == ownerId){
            SendMessage sm = new SendMessage();
            sm.setChatId(ownerId);
            sm.setText("Служебные команды");

            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            List<InlineKeyboardButton> btns = new ArrayList<>();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();

            btns.add(new InlineKeyboardButton().builder()
                    .text("изменить акции")
                    .callbackData("/setDiscounts").build());
            rows.add(btns);
            keyboardMarkup.setKeyboard(rows);
            sm.setReplyMarkup(keyboardMarkup);
            Message m = new Message(Message.MESSAGE, true);
            m.setSendMessage(sm);
            msgs.add(m);
        }

        return msgs;
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
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
                .text("покажи акционные тарифы")
                .callbackData("/discounts")
                .build());
        btns.add(rows);
        rows = new ArrayList<>();

        rows.add(new InlineKeyboardButton().builder()
                .text("покупка оборудования")
                .callbackData("/equip")
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
