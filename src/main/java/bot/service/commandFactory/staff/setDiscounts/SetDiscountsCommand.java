package bot.service.commandFactory.staff.setDiscounts;

import bot.database.entites.Discount;
import bot.database.entites.Tariff;
import bot.service.BotSanya;
import bot.service.DataManager;
import bot.service.Message;
import bot.service.commandFactory.CommandType;
import bot.service.commandFactory.MessageCreator;
import bot.service.commandFactory.interfaces.Command;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.time.Month;
import java.util.*;

@PropertySource("app.properties")
public class SetDiscountsCommand implements Command{

    private final String name = "/setDiscounts";

    @Override
    public String[] getArgs() {
        return new String[0];
    }

    private final Map<Integer, String> months = new HashMap<>(){{
        put(1, "Январь");
        put(2, "Февраль");
        put(3, "Март");
        put(4, "Апрель");
        put(5, "Май");
        put(6, "Июнь");
        put(7, "Июль");
        put(8, "Август");
        put(9, "Сентябрь");
        put(10, "Октябрь");
        put(11, "Ноябрь");
        put(12, "Декабрь");
    }};


    MessageCreator creator = new MessageCreator();

    int tariffIdToCreateDiscount;
    int monthOfEnd;
    int dayOfEnd;
    @Override
    public List<Message> process(Update update, List<String> arguments) {
        List<Message> msgs = new ArrayList<>();

        long chatId = update.hasMessage()?update.getMessage().getChatId()
                :update.getCallbackQuery().getMessage().getChatId();

        List<List<HashMap<String, String>>> data = new ArrayList<>();
        List<HashMap<String, String>> btns = new ArrayList<>();

        if(step == 1){
            int price = Integer.parseInt(arguments.get(0));
            Date endOfAction = new Date(
                    ((new Date().getMonth()+1)-monthOfEnd)<0?new Date().getYear()+1:new Date().getYear(),
                    monthOfEnd-1,
                    dayOfEnd
            );
            Timestamp end = new Timestamp(endOfAction.getTime());
            DataManager.getInstance().createDiscount(tariffIdToCreateDiscount, price, end);
            step = 0;
            monthOfEnd = 0;
            dayOfEnd = 0;
            tariffIdToCreateDiscount = -1;
        }

        btns.add(new HashMap<>(){{
            put("text", "НАЗАД");
            put("callback", "/setDiscounts");
        }});
        data.add(btns);

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

        if(chatId != BotSanya.getOwnerId() && chatId != BotSanya.getSubOwner()){
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


                if(DataManager.getInstance().getDiscountData().size() != 0){
                    for(Discount d : DataManager.getInstance().getDiscountData()){
                        Tariff t = DataManager.getInstance().getTariffById((long)d.getTariff_id());
                        builder.append(t.getName() + " " + d.getPrice() + " до "
                                + (d.getEndDate().getDate()<10?"0"+d.getEndDate().getDate():d.getEndDate().getDate()) + "."
                                + (d.getEndDate().getMonth()+1<10?""+d.getEndDate().getMonth()+1:d.getEndDate().getMonth()+1)
                                + "\n");
                    }
                }else{
                    builder.append("Нет акционных тарифов");
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

                        btns = new ArrayList<>();
                        btns.add(new HashMap<>() {{
                            put("text", "НАЗАД");
                            put("callback", "/setDiscounts");
                        }});
                        data.add(btns);

                        msgs.add(creator.createTextMessage(
                                data,
                                chatId,
                                builder.toString(),
                                true,
                                ""
                        ));
                        break;

                    case "remove":
                        data = new ArrayList<>();

                        builder = new StringBuilder();
                        builder.append("Выбери номер тарифа для того, чтобы убрать акцию\n");

                        for (Discount d : DataManager.getInstance().getDiscountData()) {
                            Tariff t = DataManager.getInstance().getTariffById(d.getTariff_id());
                            builder.append(d.getID() + " - " + t.getName() + " " + d.getPrice() + "\n");
                            btns = new ArrayList<>();
                            btns.add(new HashMap<>() {{
                                put("text", String.valueOf(d.getID()));
                                put("callback", "/setDiscounts remove " + d.getID());
                            }});
                            data.add(btns);
                        }

                        btns = new ArrayList<>();
                        btns.add(new HashMap<>() {{
                            put("text", "НАЗАД");
                            put("callback", "/setDiscounts");
                        }});
                        data.add(btns);

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
                            Date today = new Date();
                            int now = today.getMonth() + 1;

                            for(int i=0; i<3;i++){
                                for(int j=0; j<4;j++){
                                    int finalNow = now;
                                    btns.add(new HashMap<>(){{
                                        put("text", months.get(finalNow));
                                        put("callback", ("/setDiscounts add " + arguments.get(1) +  " " + finalNow));
                                    }});

                                    if(now == 12)now = 1;
                                    else now++;
                                }
                                data.add(btns);
                                btns = new ArrayList<>();
                            }
                            btns.add(new HashMap<>() {{
                                put("text", "НАЗАД");
                                put("callback", "/setDiscounts");
                            }});
                            data.add(btns);

                            msgs.add(creator.createTextMessage(
                                    data,
                                    chatId,
                                    "Выбери месяц окончания акции",
                                    true,
                                    ""
                            ));

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
            }else if(arguments.size() == 3){
                int days;
                switch (arguments.get(2)){
                    case "1", "3", "5", "7", "8", "10", "12":
                        days = 31;
                        break;

                    case "2":
                        days = 28;
                        break;

                    case "4", "6", "9", "11":
                        days = 30;
                        break;

                    default:
                        days = 0;
                        break;
                }
                int daysUsed = days;
                int day = 1;
                for(int i=0;i<days/4;i++){
                    for(int j=0; j<7;j++){
                        if(daysUsed == 0)break;
                        int finalDay = day;
                        btns.add(new HashMap<>(){{
                            put("text", String.valueOf(finalDay));
                            put("callback", ("/setDiscounts add " + arguments.get(1) +  " " + arguments.get(2) + " " + finalDay));
                        }});

                        daysUsed--;
                        day++;
                    }

                    data.add(btns);
                    btns = new ArrayList<>();
                }

                btns.add(new HashMap<>() {{
                    put("text", "НАЗАД");
                    put("callback", "/setDiscounts");
                }});
                data.add(btns);

                msgs.add(creator.createTextMessage(
                   data,
                   chatId,
                   "Выберите день окончания акции",
                   true,
                   ""
                ));
            }else if(arguments.size() == 4){
                btns.add(new HashMap<>() {{
                    put("text", "НАЗАД");
                    put("callback", "/setDiscounts");
                }});
                data.add(btns);

                msgs.add(creator.createTextMessage(
                        data,
                        chatId,
                        "Введи новую цену для тарифа",
                        true,
                        name
                ));

                tariffIdToCreateDiscount = Integer.parseInt(arguments.get(1));
                monthOfEnd = Integer.parseInt(arguments.get(2));
                dayOfEnd = Integer.parseInt(arguments.get(3));
                step++;
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
