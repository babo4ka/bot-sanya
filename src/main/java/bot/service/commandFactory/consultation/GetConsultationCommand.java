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


    private final String[] args;

    private List<String> tariffsArgs = new ArrayList<>();

    public void setArgs(String...args){
        for(int i=0;i< args.length;i++){
            tariffsArgs.add(args[i]);
        }
    }

    public GetConsultationCommand(){
        this.args = new String[]{"alltariffs"};
        this.tariffsArgs.add("alltariffs");
    }

    @Override
    public String[] getArgs() {
        return this.args;
    }

    @Override
    public SendMessage execute(Update update, String... args) {
        if(args.length == 0)return null;
        if(!tariffsArgs.contains(args[1]))return null;

        SendMessage sm = new SendMessage();

        sm.setText("Отлично, я свяжусь с Вами в телеграме в ближайшее время!");
        sm.enableMarkdown(true);
        sm.setChatId(update.hasMessage()?String.valueOf(update.getMessage().getChatId())
                :String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rows = new ArrayList<>();
        List<List<InlineKeyboardButton>> btns = new ArrayList<>();

        rows.add(new InlineKeyboardButton().builder()
                .text("вернуться в начало")
                .callbackData("/start").build());
        btns.add(rows);

        keyboardMarkup.setKeyboard(btns);

        sm.setReplyMarkup(keyboardMarkup);

        return sm;
    }


    @Override
    public String getName() {
        return this.name;
    }
}
