package bot.service;

import bot.service.commandFactory.staff.setDiscounts.SetDiscountsCommandFactory;
import bot.service.commandFactory.user.SendPostCommand.SendPostCommandFactory;
import bot.service.commandFactory.user.consultation.GetConsultationCommandFactory;
import bot.service.commandFactory.user.discounts.DiscountsCommandFactory;
import bot.service.commandFactory.user.equipment.ShowEquipmentCommandFactory;
import bot.service.commandFactory.interfaces.Command;
import bot.service.commandFactory.interfaces.CommandFactory;
import bot.service.commandFactory.user.start.StartCommandFactory;
import bot.service.commandFactory.user.subscribe.SubscribeCommandFactory;
import bot.service.commandFactory.user.tariffs.ShowTariffsCommandFactory;
import bot.service.commandFactory.user.unknown.UnknownCommandFactory;
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

    public Command getCommandByName(String name){
        return commands.get(name);
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
        commands.put("/sendPost", createCommand("sendPost").setCommand());
        commands.put("/discounts", createCommand("discounts").setCommand());
        commands.put("/equip", createCommand("equip").setCommand());

        commands.put("/setDiscounts", createCommand("setDiscounts").setCommand());
    }

    public CommandsManager(){
        setCommands();
    }
    public List<Message> executeCommand(Update update, String... commandAndArgs){
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

        for(int i=0;i<commandAndArgs.length;i++){
            System.out.println(commandAndArgs[i]);
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

            case "sendPost":
                return new SendPostCommandFactory();

            case "discounts":
                return new DiscountsCommandFactory();

            case "equip":
                return new ShowEquipmentCommandFactory();

            case "setDiscounts":
                return new SetDiscountsCommandFactory();

            default:
                return null;
        }
    }

}
