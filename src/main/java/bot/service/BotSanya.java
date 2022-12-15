package bot.service;

import bot.config.BotConfig;
import bot.database.entites.*;
import bot.database.repositories.*;
import bot.service.commandFactory.staff.setDiscounts.SetDiscountsCommand;
import bot.service.commandFactory.user.subscribe.SubscribeCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.*;

@Component
public class BotSanya extends TelegramLongPollingBot{

    final BotConfig config;

    final CommandsManager manager;

    private DataManager dataManager;


    public BotSanya(BotConfig config){
        this.config = config;
        this.manager = new CommandsManager();
        dataManager = DataManager.getInstance();
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
    @Value("${bot.owner}")
    private long ownerId;
    @Value("${bot.subowner}")
    private long subOwner;

    private final String channelId = "-1001788432377";

    private Map<Long, List<Integer>> msgsToDelete = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {

        Message sentMessage;

        if(update.hasMessage() && update.getMessage().hasText()){

            if(update.getMessage().getChatId() == ownerId || update.getMessage().getChatId() == subOwner){
                if(!loaded && update.getMessage().getText().equals("/load")){
                    loaded = true;
                    return;
                }

                if(update.getMessage().getText().startsWith("!")){
                    try {
                        deletePreviousMessages(update.getMessage().getChatId());
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    List<bot.service.Message> msgs = manager.executeCommand
                            (update, ("/setDiscounts add " + update.getMessage().getText().substring(1)).split(" "));
                    for(bot.service.Message m:msgs){
                        try {
                            sentMessage = execute(m.getSendMessage());
                            msgsToDelete.get(sentMessage.getChatId()).add(sentMessage.getMessageId());
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return;
                }
            }

            try {
                deletePreviousMessages(update.getMessage().getChatId());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            List<String> text = Arrays.asList(update.getMessage().getText().split(" "));
            String command = text.get(0);
            text.remove(0);
            List<bot.service.Message> sms = manager.executeCommand
                    (update, command, text);

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
                File file = new File(File.separator + "root" + File.separator + "channels.pdf");
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

                List<String> text = Arrays.asList(update.getMessage().getText().split(" "));
                String command = text.get(0);
                text.remove(0);
                List<bot.service.Message> sms = manager.executeCommand
                        (update, command, text);

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
            List<bot.service.Message> sms = manager.executeCommand(update, "/sendPost", new ArrayList<>());

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

}
