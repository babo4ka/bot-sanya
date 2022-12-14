package bot.service.commandFactory.staff.setDiscounts;

import bot.database.entites.Discount;
import bot.database.entites.Tariff;
import bot.service.DataManager;
import bot.service.DataUpdateListener;
import bot.service.Message;
import bot.service.Observable;
import bot.service.commandFactory.CommandType;
import bot.service.commandFactory.interfaces.Command;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class SetDiscountsCommand implements Command, Observable {

    private final String name = "/setDiscounts";

    @Override
    public String[] getArgs() {
        return new String[0];
    }

    @Value("${bot.owner}")
    private long ownerId;
    @Value("${bot.subowner}")
    private long subOwner;

    private DataManager dataManager;
    @Override
    public void setDataManager() {
        this.dataManager = DataManager.getInstance();
    }


    @Override
    public List<Message> execute(Update update, String... args) {
        List<Message> msgs = new ArrayList<>();

        long chatId = update.hasMessage()?update.getMessage().getChatId():
                update.getCallbackQuery().getMessage().getChatId();

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> btns = new ArrayList<>();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if(chatId != ownerId){
            SendMessage sm = new SendMessage();
            sm.setChatId(chatId);
            sm.setText("У Вас нету прав использовать эту команду!");
            sm.enableMarkdown(true);


            btns.add(new InlineKeyboardButton().builder()
                    .text("ВЕРНУТЬСЯ В НАЧАЛО")
                    .callbackData("/start").build());
            rows.add(btns);
            keyboardMarkup.setKeyboard(rows);
            sm.setReplyMarkup(keyboardMarkup);
            Message m = new Message(Message.MESSAGE, true);
            msgs.add(m);
        }else{
            SendMessage sm = new SendMessage();
            sm.setChatId(chatId);

            if(args.length == 1){
                StringBuilder builder = new StringBuilder();
                for(Discount d: dataManager.getDiscountData()){
                    Tariff t = dataManager.getTariffRepository().findById((long)d.getTariff_id()).get();
                    builder.append(t.getName() + " " + d.getPrice() + "\n");
                }
                sm.setText(builder.toString());

                keyboardMarkup = new InlineKeyboardMarkup();
                btns = new ArrayList<>();
                rows = new ArrayList<>();

                btns.add(new InlineKeyboardButton().builder()
                        .text("ДОБАВИТЬ")
                        .callbackData("/setDiscounts add").build());
                btns.add(new InlineKeyboardButton().builder()
                        .text("УДАЛИТЬ")
                        .callbackData("/setDiscounts remove").build());
                rows.add(btns);
                btns = new ArrayList<>();

                btns.add(new InlineKeyboardButton().builder()
                        .text("ВЕРНУТЬСЯ В НАЧАЛО")
                        .callbackData("/start").build());
                rows.add(btns);
                keyboardMarkup.setKeyboard(rows);
                sm.enableMarkdown(true);
                sm.setReplyMarkup(keyboardMarkup);

                Message m = new Message(Message.MESSAGE, true);
                m.setSendMessage(sm);
                msgs.add(m);
            }else if(args.length == 2){
                StringBuilder builder = new StringBuilder();
                switch (args[1]){
                    case "add":
                        builder = new StringBuilder();
                        builder.append("Выбери номер тарифа для того, чтобы добавить акцию и акционную цену\n" +
                                "Вот так !номер_тарифа новая_цена\n");

                        for(Tariff t: dataManager.getTariffRepository().findAll()){
                            builder.append(t.getID() + " - " + t.getName() + "\n");
                        }
                        break;

                    case "remove":
                        builder = new StringBuilder();
                        builder.append("Выбери номер тарифа для того, чтобы убрать акцию\n");

                        keyboardMarkup = new InlineKeyboardMarkup();
                        btns = new ArrayList<>();
                        rows = new ArrayList<>();

                        for(Discount d: dataManager.getDiscountData()){
                            Tariff t = dataManager.getTariffRepository().findById((long)d.getTariff_id()).get();
                            builder.append(d.getTariff_id() + " - " + t.getName() + " " + d.getPrice() + "\n");

                            btns.add(new InlineKeyboardButton().builder()
                                    .text(String.valueOf(d.getTariff_id()))
                                    .callbackData("/setDiscounts remove " + d.getTariff_id()).build());
                            rows.add(btns);
                            btns = new ArrayList<>();
                        }
                        break;

                    default:
                        break;
                }

                sm.setText(builder.toString());

                btns.add(new InlineKeyboardButton().builder()
                        .text("ОТМЕНА")
                        .callbackData("/setDiscounts").build());
                rows.add(btns);

                keyboardMarkup.setKeyboard(rows);
                sm.enableMarkdown(true);
                sm.setReplyMarkup(keyboardMarkup);

                Message m = new Message(Message.MESSAGE, true);
                m.setSendMessage(sm);
                msgs.add(m);
            }else if(args.length >= 3){
                Discount d;
                switch (args[1]){
                    case "add":
                        int idToCreate = Integer.parseInt(args[2]);
                        int price = Integer.parseInt(args[3]);
                        d = new Discount();
                        d.setPrice(price);
                        d.setTariff_id(idToCreate);
                        dataManager.getDiscountRepository().save(d);
                        notifyObservers("reload", chatId);
                        sm.setText("Акция добавлена!");
                        btns.add(new InlineKeyboardButton().builder()
                                .text("НАЗАД")
                                .callbackData("/setDiscounts").build());
                        rows.add(btns);
                        break;

                    case "remove":
                        int idToRemove = Integer.parseInt(args[2]);
                        d = dataManager.getDiscountRepository().findByTariffId(idToRemove).get();
                        dataManager.getDiscountRepository().deleteById(d.getID());
                        notifyObservers("reload", chatId);
                        sm.setText("Акция удалена!");
                        btns.add(new InlineKeyboardButton().builder()
                                .text("НАЗАД")
                                .callbackData("/setDiscounts").build());
                        rows.add(btns);
                        break;

                    default:
                        break;
                }
                keyboardMarkup.setKeyboard(rows);
                sm.enableMarkdown(true);
                sm.setReplyMarkup(keyboardMarkup);
                Message m = new Message(Message.MESSAGE, true);
                m.setSendMessage(sm);
                msgs.add(m);

            }
        }

        return msgs;
    }


    @Override
    public CommandType getCommandType() {
        return CommandType.STAFF;
    }

    @Override
    public void setArgs(String... args) {

    }

    @Override
    public String getName() {
        return name;
    }


    DataUpdateListener listener;
    @Override
    public void addObserver(DataUpdateListener listener) {
        if(this.listener == null)
            this.listener = listener;
    }

    @Override
    public void removeObserver(DataUpdateListener listener) {
        if(this.listener != null)
            this.listener = null;
    }

    @Override
    public void notifyObservers(String action, long chatId) {
        listener.update(action, chatId);
    }
}
