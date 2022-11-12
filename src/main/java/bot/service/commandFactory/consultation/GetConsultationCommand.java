package bot.service.commandFactory.consultation;

import bot.service.DataUpdateListener;
import bot.service.Message;
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
    public List<Message> execute(Update update, String... args) {
        List<Message> msgs = new ArrayList<>();
        SendMessage sm = new SendMessage();

        if(args.length == 0 || !tariffsArgs.contains(args[1])){
            sm.setText("Неправильные аргументы для этой команды");
            sm.setChatId(update.hasMessage()?String.valueOf(update.getMessage().getChatId())
                    :String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        }else{
            sm.setText("&#9989;Отлично, я свяжусь с Вами в телеграме в ближайшее время!");
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
            sm.setParseMode("HTML");
            Message msg = new Message(Message.MESSAGE);
            msg.setSendMessage(sm);
            msgs.add(msg);

            Message msg_s = new Message(Message.MESSAGE);
            msg_s.setSendMessage(messageForSanya(update.hasMessage()?update.getMessage().getFrom().getUserName():
                    update.getCallbackQuery().getFrom().getUserName(), args[1]));
            msgs.add(msg_s);
        return msgs;
    }


    private SendMessage messageForSanya(String clientUserName, String tariffName){
        SendMessage sm = new SendMessage();
        sm.setChatId(String.valueOf(268932900));
        sm.setText("Консультация по тарифу " + tariffName + "\n" +
                "@" + clientUserName);

        return sm;
    }

    @Override
    public String getName() {
        return this.name;
    }


}
