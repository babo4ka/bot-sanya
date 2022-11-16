package bot.service;

import bot.config.BotConfig;
import bot.database.entites.*;
import bot.database.repositories.*;
import bot.service.commandFactory.user.subscribe.SubscribeCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotSanya extends TelegramLongPollingBot implements DataUpdateListener{

    //репозитории сущностей
    @Autowired
    EquipRepository equipRepository;
    List<Equip> equipData = new ArrayList<>();
    @Autowired
    ExtraRepository extraRepository;
    List<Extra> extraData = new ArrayList<>();
    @Autowired
    ServiceRepository serviceRepository;
    List<Service> serviceData = new ArrayList<>();
    @Autowired
    TagsRepository tagsRepository;
    List<Tags> tagsData = new ArrayList<>();
    @Autowired
    TariffRepository tariffRepository;
    List<Tariff> tariffsData = new ArrayList<>();
    @Autowired
    SubsRepository subsRepository;
    List<Subs> subsData = new ArrayList<>();

    //репозитории промежутков
    @Autowired
    EquipInterRepository equipInterRepository;
    List<Equip_inter> equipInterData = new ArrayList<>();
    @Autowired
    ExtraInterRepository extraInterRepository;
    List<Extra_inter> extraInterData = new ArrayList<>();
    @Autowired
    ServiceInterRepository serviceInterRepository;
    List<Service_inter> serviceInterData = new ArrayList<>();
    @Autowired
    TagsInterRepository tagsInterRepository;
    List<Tags_inter> tagsInterData = new ArrayList<>();
    @Autowired
    DiscountRepository discountRepository;
    List<Discount> discountData = new ArrayList<>();


    final BotConfig config;

    final CommandsManager manager;

    private DataManager dataManager;

    private void loadData(){
        equipRepository.findAll().forEach(equipData::add);
        extraRepository.findAll().forEach(extraData::add);
        serviceRepository.findAll().forEach(serviceData::add);
        tagsRepository.findAll().forEach(tagsData::add);

        tariffRepository.findAll().forEach(tariffsData::add);

        equipInterRepository.findAll().forEach(equipInterData::add);
        extraInterRepository.findAll().forEach(extraInterData::add);
        serviceInterRepository.findAll().forEach(serviceInterData::add);
        tagsInterRepository.findAll().forEach(tagsInterData::add);

        discountRepository.findAll().forEach(discountData::add);

        subsRepository.findAll().forEach(subsData::add);

        this.dataManager = DataManager.getInstance(
                equipData, extraData, serviceData, tagsData, tariffsData, equipInterData, extraInterData,
                serviceInterData, tagsInterData, subsData, discountData, tariffRepository, discountRepository
        );

        List<TariffReady> tr = dataManager.getAlltariffs();

        String[] args = new String[tr.size()];

        for(int i=0;i<args.length;i++){
            args[i] = tr.get(i).getName();
        }

        manager.setArgs("/consultation", args);

        SubscribeCommand sc = (SubscribeCommand) manager.getCommandByName("/subscribe");
        sc.addObserver(this);
        sc.setDataManager();

        manager.getCommandByName("/start").setDataManager();
        manager.getCommandByName("/sendPost").setDataManager();
        manager.getCommandByName("/setDiscounts").setDataManager();
    }

    private void reloadData(){
        discountData = new ArrayList<>();
        discountRepository.findAll().forEach(discountData::add);
        this.dataManager.setDiscountData(discountData);
    }



    public BotSanya(BotConfig config){
        this.config = config;
        this.manager = new CommandsManager();
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }


    boolean loaded = false;
    private final long ownerId = 268932900;

    private final String channelId = "-1001379659811";

    private Map<Long, List<Integer>> msgsToDelete = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        if(!loaded){
            if(update.getMessage().getChatId() == ownerId && update.getMessage().getText().equals("/load")){
                loadData();
                loaded = true;
                return;
            }
        }

        Message sentMessage;

        if(update.hasMessage() && update.getMessage().hasText()){
            try {
                deletePreviousMessages(update.getMessage().getChatId());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            List<bot.service.Message> sms = manager.executeCommand
                    (update, update.getMessage().getText());

            try {
                for(bot.service.Message sm: sms){
                    switch (sm.getType()){
                        case bot.service.Message.MESSAGE:
                            SendMessage s = sm.getSendMessage();
                            sentMessage = execute(s);
                            if(sm.isMarkable())
                                msgsToDelete.get(sentMessage.getChatId()).add(sentMessage.getMessageId());
                            break;

                        case bot.service.Message.PHOTO:
                            SendPhoto p = sm.getSendPhoto();
                            sentMessage = execute(p);
                            if(sm.isMarkable())
                                msgsToDelete.get(sentMessage.getChatId()).add(sentMessage.getMessageId());
                            break;
                    }

                }
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if(update.hasCallbackQuery()){
            if(update.getCallbackQuery().getData().equals("/channels")){
                File file = new File(getClass().getClassLoader().getResource("channels.pdf").getFile());
                SendDocument sd = new SendDocument();
                sd.setDocument(new InputFile(file));
                sd.setChatId(update.getCallbackQuery().getMessage().getChatId());
                try {
                    execute(sd);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }else{
                try {
                    deletePreviousMessages(update.getCallbackQuery().getMessage().getChatId());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                List<bot.service.Message> sms = manager.executeCommand(
                        update, update.getCallbackQuery().getData().split(" ")
                );
                try {
                    for(bot.service.Message sm: sms){
                        switch (sm.getType()){
                            case bot.service.Message.MESSAGE:
                                SendMessage s = sm.getSendMessage();
                                sentMessage = execute(s);
                                if(sm.isMarkable())
                                    msgsToDelete.get(sentMessage.getChatId()).add(sentMessage.getMessageId());
                                break;

                            case bot.service.Message.PHOTO:
                                SendPhoto p = sm.getSendPhoto();
                                sentMessage = execute(p);
                                if(sm.isMarkable())
                                    msgsToDelete.get(sentMessage.getChatId()).add(sentMessage.getMessageId());
                                break;
                        }
                    }
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if(update.hasChannelPost()
                && String.valueOf(update.getChannelPost().getChatId()).equals(channelId)){
            List<bot.service.Message> sms = manager.executeCommand(update, "/sendPost");

            for(bot.service.Message s: sms){
                try {
                    switch (s.getType()){
                        case bot.service.Message.MESSAGE:
                            SendMessage sm = s.getSendMessage();
                            execute(sm);
                            break;

                        case bot.service.Message.PHOTO:
                            SendPhoto p = s.getSendPhoto();
                            execute(p);
                            break;
                    }
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void deletePreviousMessages(long chatId) throws TelegramApiException {
        DeleteMessage msg = new DeleteMessage();

        List<Integer> msgsIds = msgsToDelete.get(chatId);

        if(msgsIds!=null){
            for(Integer i :msgsIds){
                msg.setMessageId(i);
                msg.setChatId(chatId);
                execute(msg);
            }
        }

        msgsToDelete.put(chatId, new ArrayList<>());
    }

    @Override
    public void update(String action, long chatId) {
        subsData = new ArrayList<>();
        Subs s;
        switch (action){
            case "subscribe":
                s = new Subs();
                s.setID(chatId);
                subsRepository.save(s);
                subsRepository.findAll().forEach(subsData::add);
                break;

            case "unsubscribe":
                s = new Subs();
                s.setID(chatId);
                subsRepository.delete(s);
                subsRepository.findAll().forEach(subsData::add);
                break;

            case "reload":
                reloadData();
                break;
        }
        dataManager.setSubsData(subsData);
    }
}
