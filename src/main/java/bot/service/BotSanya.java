package bot.service;

import bot.config.BotConfig;
import bot.database.entites.*;
import bot.database.repositories.*;
import bot.service.commandFactory.SendPostCommand.SendPostCommand;
import bot.service.commandFactory.start.StartCommand;
import bot.service.commandFactory.subscribe.SubscribeCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

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

        subsRepository.findAll().forEach(subsData::add);

        this.dataManager = DataManager.getInstance(
                equipData, extraData, serviceData, tagsData, tariffsData, equipInterData, extraInterData,
                serviceInterData, tagsInterData, subsData
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

        StartCommand stc = (StartCommand) manager.getCommandByName("/start");
        stc.setDataManager();

        SendPostCommand spc = (SendPostCommand) manager.getCommandByName("/sendPost");
        spc.setDataManager();
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
                //if(update.getMessage().getChatId() != ownerId)
                    deletePreviousMessages(update.getMessage().getChatId());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            List<SendMessage> sms = manager.executeCommand
                    (update, update.getMessage().getText());
            try {
                for(SendMessage sm: sms){
                    sentMessage = execute(sm);
                    msgsToDelete.get(sentMessage.getChatId()).add(sentMessage.getMessageId());
                }
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if(update.hasCallbackQuery()){
            try {
                //if(update.getCallbackQuery().getMessage().getChatId() != ownerId)
                    deletePreviousMessages(update.getCallbackQuery().getMessage().getChatId());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            List<SendMessage> sms = manager.executeCommand(
                    update, update.getCallbackQuery().getData().split(" ")
            );
            try {
                for(SendMessage sm: sms){
                    sentMessage = execute(sm);
                    msgsToDelete.get(sentMessage.getChatId()).add(sentMessage.getMessageId());
                }
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if(update.hasChannelPost()
                && String.valueOf(update.getChannelPost().getChatId()).equals(channelId)){
            List<SendMessage> sms = manager.executeCommand(update, "/sendPost");

            for(SendMessage s: sms){
                try {
                    execute(s);
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
        }
        dataManager.setSubsData(subsData);
    }
}
