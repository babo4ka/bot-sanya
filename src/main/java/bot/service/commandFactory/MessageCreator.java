package bot.service.commandFactory;

import bot.service.Message;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageCreator {

    public MessageCreator(){}


    public Message createTextMessage(
            List<List<HashMap<String, String>>> data,
            long chatId,
            String text,
            boolean markable
    ){
        SendMessage sendMessage = new SendMessage();

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setParseMode("HTML");

        InlineKeyboardMarkup keyBoard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> btns = new ArrayList<>();

        for(List<HashMap<String, String>> buttons: data){

            for(HashMap<String, String> button : buttons){
                btns.add(new InlineKeyboardButton().builder().
                        text(button.get("text"))
                        .callbackData(button.get("callback")).build());
            }
            rows.add(btns);
            rows = new ArrayList<>();
        }
        keyBoard.setKeyboard(rows);

        sendMessage.setReplyMarkup(keyBoard);

        Message message = new Message(Message.MESSAGE, markable);
        message.setSendMessage(sendMessage);

        return message;
    }
}
