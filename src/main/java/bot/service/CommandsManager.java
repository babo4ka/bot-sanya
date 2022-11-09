package bot.service;

import bot.service.commandFactory.consultation.GetConsultationCommandFactory;
import bot.service.commandFactory.interfaces.Command;
import bot.service.commandFactory.interfaces.CommandFactory;
import bot.service.commandFactory.start.StartCommandFactory;
import bot.service.commandFactory.subscribe.SubscribeCommandFactory;
import bot.service.commandFactory.tariffs.ShowTariffsCommandFactory;
import bot.service.commandFactory.unknown.UnknownCommandFactory;
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

    public void setArgs(String command, String... args){
        commands.get(command).setArgs(args);
    }

    public void setCommands(){
        commands.put("/start", createCommand("start").setCommand());
        commands.put("/consultation", createCommand("consultation").setCommand());
        commands.put("/showTariffs", createCommand("showTariffs").setCommand());
        commands.put("/unknown", createCommand("unknown").setCommand());
        commands.put("/subscribe", createCommand("subscribe").setCommand());
    }

    public CommandsManager(){
        setCommands();
    }

    public List<SendMessage> executeCommand(Update update, String... commandAndArgs){
        String [] args = commandAndArgs;
        if(commandAndArgs[0].equals("/consultation")){
            StringBuilder subArgs = new StringBuilder();
            for(int i=1;i<args.length;i++){
                subArgs.append(args[i] + (i< args.length-1?" ":""));
            }
            args = new String[2];
            args[0] = commandAndArgs[0];
            args[1] = subArgs.toString();
        }
        commandAndArgs = args;

        if(commands.get(commandAndArgs[0]) == null){
            return commands.get("/unknown").execute(update, commandAndArgs);
        }
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

            case "unknown":
                return new UnknownCommandFactory();

            case "subscribe":
                return new SubscribeCommandFactory();

            default:
                return null;
        }
    }

}
