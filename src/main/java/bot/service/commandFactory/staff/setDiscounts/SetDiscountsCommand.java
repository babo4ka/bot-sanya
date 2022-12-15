package bot.service.commandFactory.staff.setDiscounts;

import bot.database.entites.Discount;
import bot.database.entites.Tariff;
import bot.service.DataManager;
import bot.service.DataUpdateListener;
import bot.service.Message;
import bot.service.Observable;
import bot.service.commandFactory.CommandType;
import bot.service.commandFactory.MessageCreator;
import bot.service.commandFactory.interfaces.Command;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@PropertySource("app.properties")
public class SetDiscountsCommand implements Command{

    private final String name = "/setDiscounts";

    @Override
    public String[] getArgs() {
        return new String[0];
    }


    @Value("${bot.owner}")
    private long ownerId;
    @Value("${bot.subowner}")
    private long subOwner;


    MessageCreator creator = new MessageCreator();

    int tariffIdToCreateDiscount;
    @Override
    public List<Message> process(Update update, List<String> arguments) {
        List<Message> msgs = new ArrayList<>();

        long chatId = update.hasMessage()?update.getMessage().getChatId()
                :update.getCallbackQuery().getMessage().getChatId();

        List<List<HashMap<String, String>>> data = new ArrayList<>();
        List<HashMap<String, String>> btns = new ArrayList<>();

        if(step == 1){
            int price = Integer.parseInt(arguments.get(0));
            DataManager.getInstance().createDiscount(tariffIdToCreateDiscount, price);
            step = 0;
        }

        btns.add(new HashMap<>(){{
            put("text", "НАЗАД");
            put("callback", "/setDiscounts");
        }});

        msgs.add(creator.createTextMessage(
                data,
                chatId,
                "Добавлена новая акция!",
                true,
                ""
        ));

        return msgs;
    }

    int step = 0;

    @Override
    public List<Message> execute(Update update, List<String> arguments) {
        List<Message> msgs = new ArrayList<>();

        long chatId = update.hasMessage()?update.getMessage().getChatId()
                :update.getCallbackQuery().getMessage().getChatId();

        List<List<HashMap<String, String>>> data;
        List<HashMap<String, String>> btns;

        StringBuilder builder = new StringBuilder();

        if(chatId != ownerId){
            btns = new ArrayList<>();
            data = new ArrayList<>();

            btns.add(new HashMap<>(){{
                put("text", "ВЕРНУТЬСЯ В НАЧАЛО");
                put("callback", "/start");
            }});

            data.add(btns);

            msgs.add(creator.createTextMessage(
                    data,
                    chatId,
                    "У Вас нету прав использовать эту команду!",
                    true,
                    ""
            ));
        }else{
            btns = new ArrayList<>();
            data = new ArrayList<>();
            if(arguments.size() == 0){
                btns.add(new HashMap<>(){{
                    put("text", "ДОБАВИТЬ");
                    put("callback", "/setDiscounts add");
                }});
                data.add(btns);
                btns = new ArrayList<>();

                btns.add(new HashMap<>(){{
                    put("text", "УДАЛИТЬ");
                    put("callback", "/setDiscounts remove");
                }});
                data.add(btns);
                btns = new ArrayList<>();
                btns.add(new HashMap<>(){{
                    put("text", "ВЕРНУТЬСЯ В НАЧАЛО");
                    put("callback", "/start");
                }});
                data.add(btns);



                for(Discount d : DataManager.getInstance().getDiscountData()){
                    Tariff t = DataManager.getInstance().getTariffById((long)d.getTariff_id());
                    builder.append(t.getName() + " " + d.getPrice() + "\n");
                }

                msgs.add(creator.createTextMessage(
                        data,
                        chatId,
                        builder.toString(),
                        true,
                        ""
                ));
            }else if(arguments.size() == 1){
                switch (arguments.get(0)) {
                    case "add":
                        builder = new StringBuilder();
                        builder.append("Выбери номер тарифа для того, чтобы добавить акцию\n");

                        for(Tariff t : DataManager.getInstance().getTariffsData()){
                            if(DataManager.getInstance().hasDiscount(t.getID()) == -1){
                                builder.append(t.getID() + " - " + t.getName() + "\n");

                                btns = new ArrayList<>();
                                btns.add(new HashMap<>() {{
                                    put("text", String.valueOf(t.getID()));
                                    put("callback", "/setDiscounts add " + t.getID());
                                }});
                                data.add(btns);

                            }
                        }

                        msgs.add(creator.createTextMessage(
                                data,
                                chatId,
                                builder.toString(),
                                true,
                                name
                        ));
                        break;

                    case "remove":
                        data = new ArrayList<>();

                        builder = new StringBuilder();
                        builder.append("Выбери номер тарифа для того, чтобы убрать акцию\n");

                        for (Discount d : DataManager.getInstance().getDiscountData()) {
                            Tariff t = DataManager.getInstance().getTariffById((long) d.getTariff_id());
                            builder.append(d.getTariff_id() + " - " + t.getName() + " " + d.getPrice() + "\n");
                            btns = new ArrayList<>();
                            btns.add(new HashMap<>() {{
                                put("text", String.valueOf(d.getTariff_id()));
                                put("callback", "/setDiscounts remove " + d.getTariff_id());
                            }});
                            data.add(btns);
                        }

                        msgs.add(creator.createTextMessage(
                                data,
                                chatId,
                                builder.toString(),
                                true,
                                ""
                        ));

                        break;
                }
                }else if(arguments.size() == 2) {
                    switch (arguments.get(0)) {
                        case "add":
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.enableMarkdown(true);
                            sendMessage.setChatId(chatId);
                            sendMessage.setText("Введи новую цену для тарифа");
                            sendMessage.setParseMode("HTML");
                            Message message = new Message(Message.MESSAGE, true, "");
                            message.setSendMessage(sendMessage);
                            msgs.add(message);

                            tariffIdToCreateDiscount = Integer.parseInt(arguments.get(1));
                            step++;
                            break;

                        case "remove":
                            long id = Long.parseLong(arguments.get(1));
                            DataManager.getInstance().deleteDiscount(id);
                            btns.add(new HashMap<>() {{
                                put("text", "НАЗАД");
                                put("callback", "/setDiscounts");
                            }});
                            data.add(btns);
                            msgs.add(creator.createTextMessage(
                                    data,
                                    chatId,
                                    "Акция удалена!",
                                    true,
                                    ""
                            ));
                            break;
                }
            }

        }

        return msgs;
    }


    @Override
    public CommandType getCommandType() {
        return CommandType.STAFF;
    }


    @Override
    public String getName() {
        return name;
    }


}
