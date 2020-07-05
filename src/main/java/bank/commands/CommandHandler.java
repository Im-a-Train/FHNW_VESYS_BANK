package bank.commands;

import bank.commands.Command;

import java.io.IOException;

public interface CommandHandler {

    Command handle(Command command) throws IOException;

}
