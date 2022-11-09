package bot.service.commandFactory.tariffs;

import bot.database.entites.Tags;
import bot.service.DataManager;
import bot.service.TariffReady;
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
    public List<SendMessage> execute(Update update, String... args) {
        List<SendMessage> sms = new ArrayList<>();


        List<TariffReady> tr = DataManager.getInstance().getAlltariffs();
        if(args.length == 1){
            choosedTags.clear();
            for(TariffReady t:tr){
                sms.add(setMessageToSend
                        (String.valueOf(update.hasMessage()?update.getMessage().getChatId():update.getCallbackQuery().getMessage().getChatId()),
                        t));
            }
        }else{
            if(choosedTags.contains(args[1]) && args[1] != null)choosedTags.remove(args[1]);
            else choosedTags.add(args[1]);

            for(TariffReady t:tr){
                if(checkTags(t)){
                    sms.add(setMessageToSend
                            (String.valueOf(update.hasMessage()?update.getMessage().getChatId():update.getCallbackQuery().getMessage().getChatId()),
                                    t));
                }
            }
        }

        System.out.println("===========================================\n" + sms.size());

        SendMessage tagChoose = new SendMessage();
        tagChoose.setText("Вы можете выбрать или убрать теги для фильтрации тарифов");
        tagChoose.setChatId(String.valueOf(update.hasMessage()?update.getMessage().getChatId():update.getCallbackQuery().getMessage().getChatId()));
        tagChoose.enableMarkdown(true);
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> btns = new ArrayList<>();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        btns.add(new InlineKeyboardButton().builder()
                .text("wifi")
                .callbackData("/showTariffs wifi").build());
        btns.add(new InlineKeyboardButton().builder()
                .text("tv")
                .callbackData("/showTariffs tv").build());
        btns.add(new InlineKeyboardButton().builder()
                .text("mobile")
                .callbackData("/showTariffs mobile").build());
        rows.add(btns);
        keyboardMarkup.setKeyboard(rows);
        tagChoose.setReplyMarkup(keyboardMarkup);
        sms.add(tagChoose);
        return sms;
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
