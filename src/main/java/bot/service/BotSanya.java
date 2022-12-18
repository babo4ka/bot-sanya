package bot.service;

import bot.config.BotConfig;
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
import java.util.*;

@Component
public class BotSanya extends TelegramLongPollingBot{

    final BotConfig config;

    final CommandsManager manager;

    public static long ownerId;
    public static long subOwner;
    public static long channelId;

    public static long getOwnerId(){return ownerId;}

    public static long getSubOwner(){return subOwner;}

    public static long getChannelId(){return channelId;}

    public BotSanya(BotConfig config){
        this.config = config;
        this.manager = new CommandsManager();
        ownerId = config.getOwnerId();
        subOwner = config.getSubOwner();
        channelId = config.getChannelId();
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }


    private Map<Long, List<Integer>> msgsToDelete = new HashMap<>();

    private String processingCommand = "";

    @Autowired
    public void setDataManager(DataManager dataManager){
        DataManager.setInstance(dataManager);
        DataManager.getInstance().loadData();
    }

    @Override
    public void onUpdateReceived(Update update) {

        Message sentMessage;

        if(update.hasMessage() && update.getMessage().hasText()){

            try {
                deletePreviousMessages(update.getMessage().getChatId());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }


            List<String> text = new ArrayList<>(Arrays.asList(update.getMessage().getText().split(" ")));
            List<bot.service.Message> sms;
            if(!processingCommand.equals("")){
                sms = manager.processCommand(update, processingCommand, text);
            }else{
                String command = text.remove(0);
                sms = manager.executeCommand
                        (update, command, text);
            }


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

                    processingCommand = sm.getProcess();
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

                List<String> text = new ArrayList<>(Arrays.asList(update.getCallbackQuery().getData().split(" ")));
                String command = text.remove(0);
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
                        processingCommand = sm.getProcess();
                    }
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if(update.hasChannelPost()
                && String.valueOf(update.getChannelPost().getChatId()).equals(config.getChannelId())){
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
