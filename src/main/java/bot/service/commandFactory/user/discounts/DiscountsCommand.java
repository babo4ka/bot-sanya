package bot.service.commandFactory.user.discounts;

import bot.database.entites.Discount;
import bot.database.entites.Tariff;
import bot.service.DataManager;
import bot.service.Message;
import bot.service.TariffReady;
import bot.service.commandFactory.CommandType;
import bot.service.commandFactory.MessageCreator;
import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiscountsCommand implements Command {
    private final String name = "/discounts";
    @Override
    public String[] getArgs() {
        return new String[0];
    }

    MessageCreator creator = new MessageCreator();


    @Override
    public List<Message> execute(Update update, List<String> arguments) {
        List<Message> msgs = new ArrayList<>();

        long chatId = update.hasMessage()?update.getMessage().getChatId()
                :update.getCallbackQuery().getMessage().getChatId();

        List<List<HashMap<String, String>>> data;
        List<HashMap<String, String>> btns;

        List<TariffReady> tr = DataManager.getInstance().getAlltariffs();

        boolean discounts = false;

        for(TariffReady t: tr){
            if(t.isHasDiscount()){
                discounts = true;
                data = new ArrayList<>();
                btns = new ArrayList<>();

                btns.add(new HashMap<>(){{
                    put("text", "ПОЛУЧИТЬ КОНСУЛЬТАЦИЮ");
                    put("callback", ("/consultation " + t.getName()));
                }});
                data.add(btns);
                msgs.add(creator.createTextMessage(
                        data,
                        chatId,
                        t.toString() +
                                "\n" + "Нажмите на кнопку ниже, и я с Вами свяжусь для консультации по этому тарифу",
                        true
                ));
            }
        }

        if(discounts){
            data = new ArrayList<>();
            btns = new ArrayList<>();

            btns.add(new HashMap<>(){{
                put("text", "ВЕРНУТЬСЯ В НАЧАЛО");
                put("callback", "/start");
            }});
            data.add(btns);

            msgs.add(creator.createTextMessage(
                    data,
                    chatId,
                    "вернуться в начало",
                    true
            ));
        }else{
            data = new ArrayList<>();
            btns = new ArrayList<>();

            btns.add(new HashMap<>(){{
                put("text", "ВЕРНУТЬСЯ В НАЧАЛО");
                put("callback", "/start");
            }});
            data.add(btns);

            msgs.add(creator.createTextMessage(
                    data,
                    chatId,
                    "На данный момент нет тарифов по акции!",
                    true
            ));
        }

        return msgs;
    }


    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }

    @Override
    public String getName() {
        return name;
    }
}
