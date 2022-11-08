package bot.service;

import bot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class BotSanya extends TelegramLongPollingBot {


    final BotConfig config;

    final CommandsManager manager;

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



    @Override
    public void onUpdateReceived(Update update) {
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
