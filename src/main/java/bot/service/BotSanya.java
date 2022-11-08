package bot.service;

import bot.config.BotConfig;
import bot.database.entites.*;
import bot.database.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotSanya extends TelegramLongPollingBot {

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

        this.dataManager = new DataManager(
                equipData, extraData, serviceData, tagsData, tariffsData, equipInterData, extraInterData,
                serviceInterData, tagsInterData
        );

        List<TariffReady> tr = dataManager.getAlltariffs();

        String[] args = new String[tr.size()];

        manager.setArgs("/consultation", args);
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
    @Override
    public void onUpdateReceived(Update update) {
        if(!loaded){
            loadData();
            loaded = true;
        }

        System.out.println(dataManager.getAlltariffs());

        if(update.hasMessage() && update.getMessage().hasText()){
            SendMessage sm = manager.executeCommand
                    (update, update.getMessage().getText());
            try {
                execute(sm);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if(update.hasCallbackQuery()){
            SendMessage sm = manager.executeCommand(
                    update, update.getCallbackQuery().getData().split(" ")
            );
            try {
                execute(sm);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
