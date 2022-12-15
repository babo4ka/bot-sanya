package bot.service.commandFactory.user.start;

import bot.service.DataManager;
import bot.service.Message;
import bot.service.commandFactory.CommandType;
import bot.service.commandFactory.MessageCreator;
import bot.service.commandFactory.interfaces.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@PropertySource("app.properties")
public class StartCommand implements Command{

    private final String name = "/start";

    private final String info = "&#128129;Ведущий специалист по домашним услугам ПАО МТС Александр\n" +
            "Агент группы продаж, работаю в компании пятый год, суммарно через меня подключилось более 1000 квартир\n\n" +
            "&#128205;Веду работу по Республике Татарстан\n\n" +
            "&#128222;Можете позвонить мне по номеру <strong>8 - 917 - 292 - 84 - 45</strong>\n" +
            "&#128233;Или написать мне в телеграме @aleksandr_mtsRT\n\n" +
            "&#128240;Вы также можете подписаться на мой новостной канал @aleksandr_mtsRT_news\n" +
            "Или же подписаться на рассылку и я Вам сам буду присылать новые посты оттуда\n\n" +
            "&#129470;А я его бот-помощник:) Через меня вы вкратце можете узнать интересующую Вас информацию\n" +
            "Могу показать Вам тарифы или же сообщу Александру, чтобы он связался с Вами и обсудил все тарифы\n\n" +
            "&#8252;Убедитесь, что у Вас указано имя пользователя в телеграмм(username) или я не смогу сообщить Александру с кем надо связаться";

    @Override
    public String[] getArgs() {
        return new String[0];
    }



    @Value("${bot.owner}")
    private long ownerId;
    @Value("${bot.subowner}")
    private long subOwner;

    MessageCreator creator = new MessageCreator();

    @Override
    public List<Message> execute(Update update, List<String> arguments) {
        List<Message> msgs = new ArrayList<>();

        long chatId = update.hasMessage()?update.getMessage().getChatId()
                :update.getCallbackQuery().getMessage().getChatId();


        msgs.add(creator.createTextMessage(
                createData(chatId),
                chatId,
                info,
                true,
                ""
        ));

        if(chatId == ownerId){
            List<List<HashMap<String, String>>> data = new ArrayList<>();
            List<HashMap<String, String>> btns = new ArrayList<>();

            btns.add(new HashMap<String, String>(){{
                put("text", "ИЗМЕНИТЬ АКЦИИ");
                put("callback", "/setDiscounts");
            }});
            data.add(btns);

            msgs.add(creator.createTextMessage(
                    data,
                    ownerId,
                    "Служебные команды",
                    true,
                    ""
            ));
        }

        return msgs;
    }

    @Override
    public List<Message> process(Update update, List<String> arguments) {
        return null;
    }


    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }


    private List<List<HashMap<String, String>>> createData(long chatId){
        List<List<HashMap<String, String>>> data = new ArrayList<>();
        List<HashMap<String, String>> btns = new ArrayList<>();
        btns.add(new HashMap<String, String>(){{
            put("text", "ПОКАЖИ ВСЕ ТАРИФЫ");
            put("callback", "/showTariffs");
        }
        });
        data.add(btns);
        btns = new ArrayList<>();

        btns.add(new HashMap<String, String>(){{
            put("text", "ПОКАЖИ АКЦИОННЫЕ ТАРИФЫ");
            put("callback", "/discounts");
        }
        });
        data.add(btns);
        btns = new ArrayList<>();

        btns.add(new HashMap<String, String>(){{
            put("text", "ПОКУПКА ОБОРУДОВАНИЯ");
            put("callback", "/equip");
        }
        });
        data.add(btns);
        btns = new ArrayList<>();

        btns.add(new HashMap<String, String>(){{
            put("text", DataManager.getInstance().isSub(chatId)?"ОТПИСАТЬСЯ ОТ РАССЫЛКИ ИЗ НОВОСТНОГО КАНАЛА":"ПОДПИСАТЬСЯ НА РАССЫЛКУ ИЗ НОВОСТНОГО КАНАЛА");
            put("callback", "/subscribe");
        }
        });
        data.add(btns);
        btns = new ArrayList<>();
        btns.add(new HashMap<String, String>(){{
            put("text", "СПИСОК КАНАЛОВ");
            put("callback", "/channels");
        }
        });
        data.add(btns);
        btns = new ArrayList<>();
        btns.add(new HashMap<String, String>(){{
            put("text", "ПОЛУЧИТЬ КОНСУЛЬТАЦИЮ ПО ВСЕМ ТАРИФАМ");
            put("callback", "/consultation alltariffs");
        }
        });
        data.add(btns);

        return data;
    }


    @Override
    public String getName() {
        return this.name;
    }


}
