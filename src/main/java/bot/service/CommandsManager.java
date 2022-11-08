package bot.service;

import bot.service.commandFactory.consultation.GetConsultationCommandFactory;
import bot.service.commandFactory.interfaces.Command;
import bot.service.commandFactory.interfaces.CommandFactory;
import bot.service.commandFactory.start.StartCommandFactory;
import bot.service.commandFactory.tariffs.ShowTariffsCommandFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandsManager {

    private Map<String, Command> commands = new HashMap<>();

    public List<String> getCommandNames(){
        List<String> names = new ArrayList<>();

        for(String c:commands.keySet()){
            names.add(commands.get(c).getName());
        }

        return names;
    }

    public void setCommands(){
        commands.put("/start", createCommand("start").setCommand());
        commands.put("/consultation", createCommand("consultation").setCommand());
    }

    public CommandsManager(){
        setCommands();
    }

    public SendMessage executeCommand(Update update, String... commandAndArgs){
        return commands.get(commandAndArgs[0]).execute(update, commandAndArgs);
    }





    private CommandFactory createCommand(String name){
        switch (name){
            case "start":
                return new StartCommandFactory();

            case "showTariffs":
                return new ShowTariffsCommandFactory();

            case "consultation":
                return new GetConsultationCommandFactory();

            default:
                return null;
        }
    }

}
