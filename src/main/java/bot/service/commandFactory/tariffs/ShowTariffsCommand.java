package bot.service.commandFactory.tariffs;

import bot.service.commandFactory.interfaces.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ShowTariffsCommand implements Command {

    private final String name = "/showtariffs";

    private final String[] args = {"alltariffs", "wifi", "tv", "mobile"};

    @Override
    public String[] getArgs() {
        return new String[0];
    }

    @Override
    public SendMessage execute(Update update, String... args) {


        return null;
    }




    @Override
    public void setArgs(String... args) {
    }

    @Override
    public String getName() {
        return null;
    }
}
