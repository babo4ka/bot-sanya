package bot.service.commandFactory.tariffs;

import bot.service.DataManager;
import bot.service.TariffReady;
import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class ShowTariffsCommand implements Command {

    private final String name = "/showtariffs";

    private final String[] args = {"alltariffs", "wifi", "tv", "mobile"};

    @Override
    public String[] getArgs() {
        return new String[0];
    }

    @Override
    public List<SendMessage> execute(Update update, String... args) {
        List<SendMessage> sms = new ArrayList<>();
        List<TariffReady> tr = DataManager.getInstance().getAlltariffs();
        if(args[1].equals("alltariffs")){
            for(TariffReady t:tr){
                sms.add(setMessageToSend
                        (String.valueOf(update.hasMessage()?update.getMessage().getChatId():update.getCallbackQuery().getMessage().getChatId()),
                        t));
            }
        }

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
