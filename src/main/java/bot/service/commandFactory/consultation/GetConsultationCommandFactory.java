package bot.service.commandFactory.consultation;

import bot.service.commandFactory.interfaces.Command;
import bot.service.commandFactory.interfaces.CommandFactory;

public class GetConsultationCommandFactory implements CommandFactory {
    @Override
    public Command setCommand() {
        return new GetConsultationCommand();
    }
}
