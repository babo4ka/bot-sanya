package bot.service.commandFactory.user.discounts;

import bot.database.entites.Discount;
import bot.database.entites.Tariff;
import bot.service.DataManager;
import bot.service.Message;
import bot.service.TariffReady;
import bot.service.commandFactory.CommandType;
import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class DiscountsCommand implements Command {
    private final String name = "/discounts";
    @Override
    public String[] getArgs() {
        return new String[0];
    }

    @Override
    public List<Message> execute(Update update, String... args) {
        List<Message> msgs = new ArrayList<>();
        List<Discount> discounts = DataManager.getInstance().getDiscountData();
        if(discounts.size() > 0){
            for(Discount d:discounts){
                System.out.println(DataManager.getInstance().getTariffReadyById().size());
                TariffReady t = DataManager.getInstance().getTariffReadyById().get((long)d.getTariff_id());
                SendMessage sm = new SendMessage();
                sm.setChatId(update.hasMessage()?update.getMessage().getChatId():update.getCallbackQuery().getMessage().getChatId());
                sm.setText(t.toString() +
                        "\n" + "Нажмите на кнопку ниже, и я с Вами свяжусь для консультации по этому тарифу");
                sm.enableMarkdown(true);
                InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
                List<InlineKeyboardButton> btns = new ArrayList<>();
                List<List<InlineKeyboardButton>> rows = new ArrayList<>();

                btns.add(new InlineKeyboardButton().builder()
                        .text("ПОЛУЧИТЬ КОНСУЛЬТАЦИЮ")
                        .callbackData("/consultation " + t.getName()).build());
                rows.add(btns);

                keyboardMarkup.setKeyboard(rows);
                sm.setReplyMarkup(keyboardMarkup);
                sm.setParseMode("HTML");
                Message message = new Message(Message.MESSAGE, true);
                message.setSendMessage(sm);
                msgs.add(message);
            }
        }else{
            SendMessage sm = new SendMessage();
            sm.setChatId(update.hasMessage()?update.getMessage().getChatId():update.getCallbackQuery().getMessage().getChatId());
            sm.setText("На данный момент нет тарифов по акции!");
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            List<InlineKeyboardButton> btns = new ArrayList<>();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
            btns.add(new InlineKeyboardButton().builder()
                    .text("ВЕРНУТЬСЯ В НАЧАЛО")
                    .callbackData("/start").build());
            rows.add(btns);

            keyboardMarkup.setKeyboard(rows);
            sm.setReplyMarkup(keyboardMarkup);
            Message message = new Message(Message.MESSAGE, true);
            message.setSendMessage(sm);
            msgs.add(message);
        }
        SendMessage sm = new SendMessage();
        sm.setChatId(update.hasMessage()?update.getMessage().getChatId():update.getCallbackQuery().getMessage().getChatId());
        sm.setText("вернуться в начало");
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> btns = new ArrayList<>();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        btns.add(new InlineKeyboardButton().builder()
                .text("ВЕРНУТЬСЯ В НАЧАЛО")
                .callbackData("/start").build());
        rows.add(btns);

        keyboardMarkup.setKeyboard(rows);
        sm.setReplyMarkup(keyboardMarkup);
        Message message = new Message(Message.MESSAGE, true);
        message.setSendMessage(sm);
        msgs.add(message);
        return msgs;
    }

    @Override
    public void setDataManager() {

    }

    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }

    @Override
    public void setArgs(String... args) {

    }

    @Override
    public String getName() {
        return name;
    }
}
