package bot.service.commandFactory.user.tariffs;

import bot.database.entites.Tags;
import bot.service.DataManager;
import bot.service.DataUpdateListener;
import bot.service.Message;
import bot.service.TariffReady;
import bot.service.commandFactory.CommandType;
import bot.service.commandFactory.MessageCreator;
import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ShowTariffsCommand implements Command {

    private final String name = "/showtariffs";

    private final String[] args = {"alltariffs", "wifi", "tv", "mobile"};

    @Override
    public String[] getArgs() {
        return args;
    }

    private List<String> choosedTags = new ArrayList<>();

    private boolean checkTags(TariffReady tr){
        boolean all = true;

        List<String> trTags = new ArrayList<>();
        for(Tags t: tr.getTags()){
            trTags.add(t.getName());
        }

        for(String tag : choosedTags){
            if(!trTags.contains(tag)){
                all = false;
                break;
            }
        }
        return all;
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

        if(arguments.size() == 0){
            choosedTags.clear();
            for(TariffReady t:tr){
                data = new ArrayList<>();
                btns = new ArrayList<>();

                btns.add(new HashMap<>(){{
                    put("text", "ПОЛУЧИТЬ КОНСУЛЬТАЦИЮ");
                    put("callback", "/consultation " + t.getName());
                }});
                data.add(btns);

                msgs.add(creator.createTextMessage(
                        data,
                        chatId,
                        t.toString() + "\n" +
                                "Нажмите на кнопку ниже, и я с Вами свяжусь для консультации по этому тарифу",
                        true
                ));
            }
        }else{
            if(choosedTags.contains(arguments.get(0)))choosedTags.remove(arguments.get(0));
            else choosedTags.add(arguments.get(0));

            for(TariffReady t:tr){
                if(checkTags(t)){
                    data = new ArrayList<>();
                    btns = new ArrayList<>();

                    btns.add(new HashMap<>(){{
                        put("text", "ПОЛУЧИТЬ КОНСУЛЬТАЦИЮ");
                        put("callback", "/consultation " + t.getName());
                    }});
                    data.add(btns);

                    msgs.add(creator.createTextMessage(
                            data,
                            chatId,
                            t.toString() + "\n" +
                                    "Нажмите на кнопку ниже, и я с Вами свяжусь для консультации по этому тарифу",
                            true
                    ));
                }
            }
        }

        data = new ArrayList<>();
        btns = new ArrayList<>();

        btns.add(new HashMap<>(){{
            put("text", choosedTags.contains("wifi")?"УБРАТЬ ТЕГ ":"ДОБАВИТЬ ТЕГ ");
            put("callback", "/showTariffs wifi");
        }});
        data.add(btns);
        btns = new ArrayList<>();
        btns.add(new HashMap<>(){{
            put("text", choosedTags.contains("tv")?"УБРАТЬ ТЕГ ":"ДОБАВИТЬ ТЕГ ");
            put("callback", "/showTariffs tv");
        }});
        data.add(btns);
        btns = new ArrayList<>();
        btns.add(new HashMap<>(){{
            put("text", choosedTags.contains("mobile")?"УБРАТЬ ТЕГ ":"ДОБАВИТЬ ТЕГ ");
            put("callback", "/showTariffs mobile");
        }});
        data.add(btns);
        btns = new ArrayList<>();

        btns.add(new HashMap<>(){{
            put("text", "ВЕРНУТЬСЯ В НАЧАЛО");
            put("callback", "/start");
        }});
        data.add(btns);

        msgs.add(creator.createTextMessage(
                data,
                chatId,
                "Вы можете добавить или убрать теги для фильтрации тарифов",
                true
        ));
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
