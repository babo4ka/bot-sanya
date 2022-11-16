package bot.service.commandFactory.user.tariffs;

import bot.database.entites.Tags;
import bot.service.DataManager;
import bot.service.DataUpdateListener;
import bot.service.Message;
import bot.service.TariffReady;
import bot.service.commandFactory.CommandType;
import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowTariffsCommand implements Command {

    private final String name = "/showtariffs";

    private final String[] args = {"alltariffs", "wifi", "tv", "mobile"};

    @Override
    public String[] getArgs() {
        return args;
    }

    private List<String> choosedTags = new ArrayList<>();

    private boolean checkTags(TariffReady tr){
        boolean all = true;

        List<String> trTags = new ArrayList<>();
        for(Tags t: tr.getTags()){
            trTags.add(t.getName());
        }

        for(String tag : choosedTags){
            if(!trTags.contains(tag)){
                all = false;
                break;
            }
        }
        return all;
    }

    @Override
    public List<Message> execute(Update update, String... args) {
        List<Message> msgs = new ArrayList<>();


        List<TariffReady> tr = DataManager.getInstance().getAlltariffs();
        if(args.length == 1){
            choosedTags.clear();
            for(TariffReady t:tr){
                Message msg = new Message(Message.MESSAGE, true);
                msg.setSendMessage(setMessageToSend
                        (String.valueOf(update.hasMessage()?update.getMessage().getChatId():update.getCallbackQuery().getMessage().getChatId()),
                                t));
                msgs.add(msg);
            }
        }else{
            if(choosedTags.contains(args[1]) && args[1] != null)choosedTags.remove(args[1]);
            else choosedTags.add(args[1]);

            for(TariffReady t:tr){
                if(checkTags(t)){
                    Message msg = new Message(Message.MESSAGE, true);
                    msg.setSendMessage(setMessageToSend
                            (String.valueOf(update.hasMessage()?update.getMessage().getChatId():update.getCallbackQuery().getMessage().getChatId()),
                                    t));
                    msgs.add(msg);
                }
            }
        }

        SendMessage tagChoose = new SendMessage();
        tagChoose.setText("Вы можете добавить или убрать теги для фильтрации тарифов");
        tagChoose.setChatId(String.valueOf(update.hasMessage()?update.getMessage().getChatId():update.getCallbackQuery().getMessage().getChatId()));
        tagChoose.enableMarkdown(true);
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> btns = new ArrayList<>();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        btns.add(new InlineKeyboardButton().builder()
                .text((choosedTags.contains("wifi")?"убрать тег ":"добавить тег ") + "wifi")
                .callbackData("/showTariffs wifi").build());
        rows.add(btns);
        btns = new ArrayList<>();
        btns.add(new InlineKeyboardButton().builder()
                .text((choosedTags.contains("tv")?"убрать тег ":"добавить тег ") + "tv")
                .callbackData("/showTariffs tv").build());
        rows.add(btns);
        btns = new ArrayList<>();
        btns.add(new InlineKeyboardButton().builder()
                .text((choosedTags.contains("mobile")?"убрать тег ":"добавить тег ") + "mobile")
                .callbackData("/showTariffs mobile").build());
        rows.add(btns);
        btns = new ArrayList<>();
        btns.add(new InlineKeyboardButton().builder()
                .text("вернуться в начало")
                .callbackData("/start").build());
        rows.add(btns);
        keyboardMarkup.setKeyboard(rows);
        tagChoose.setReplyMarkup(keyboardMarkup);
        Message msg = new Message(Message.MESSAGE, true);
        msg.setSendMessage(tagChoose);
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

    private SendMessage setMessageToSend(String chatId, TariffReady tr){
        SendMessage sm = new SendMessage();

        sm.setChatId(chatId);
        String text = tr.toString() + "\n" +
                "Нажмите на кнопку ниже, и я с Вами свяжусь для консультации по этому тарифу";
        sm.setText(text);

        sm.enableMarkdown(true);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> btns = new ArrayList<>();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        btns.add(new InlineKeyboardButton().builder()
                .text("получить консультацию")
                .callbackData("/consultation " + tr.getName()).build());
        rows.add(btns);

        keyboardMarkup.setKeyboard(rows);
        sm.setReplyMarkup(keyboardMarkup);
        sm.setParseMode("HTML");
        return sm;
    }


    @Override
    public void setArgs(String... args) {
    }

    @Override
    public String getName() {
        return null;
    }

}