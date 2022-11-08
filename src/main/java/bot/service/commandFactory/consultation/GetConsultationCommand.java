package bot.service.commandFactory.consultation;

import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class GetConsultationCommand implements Command {

    private final String name = "/consultation";

    private List<String> tariffsArgs = new ArrayList<>();

    public void setArgs(String...args){
        for(int i=0;i< args.length;i++){
            tariffsArgs.add(args[i]);
        }
    }

    public GetConsultationCommand(){
        this.tariffsArgs.add("alltariffs");
    }

    @Override
    public String[] getArgs() {
        return this.tariffsArgs.toArray(new String[0]);
    }

    @Override
    public List<SendMessage> execute(Update update, String... args) {
        List<SendMessage> sms = new ArrayList<>();
        SendMessage sm = new SendMessage();

        if(args.length == 0 || !tariffsArgs.contains(args[1])){
            sm.setText("Неправильные аргументы для этой команды");
            sm.setChatId(update.hasMessage()?String.valueOf(update.getMessage().getChatId())
                    :String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        }else{
            sm.setText("Отлично, я свяжусь с Вами в телеграме в ближайшее время!");
            sm.enableMarkdown(true);
            sm.setChatId(update.hasMessage()?String.valueOf(update.getMessage().getChatId())
                    :String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        }
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            List<InlineKeyboardButton> rows = new ArrayList<>();
            List<List<InlineKeyboardButton>> btns = new ArrayList<>();

            rows.add(new InlineKeyboardButton().builder()
                    .text("вернуться в начало")
                    .callbackData("/start").build());
            btns.add(rows);

            keyboardMarkup.setKeyboard(btns);

            sm.setReplyMarkup(keyboardMarkup);
            sms.add(sm);

        return sms;
    }


    @Override
    public String getName() {
        return this.name;
    }
}
