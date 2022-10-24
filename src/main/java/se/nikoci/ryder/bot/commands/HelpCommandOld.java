package se.nikoci.ryder.bot.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;
import se.nikoci.ryder.lib.Ryder;
import se.nikoci.ryder.lib.models.CommandOld;
import se.nikoci.ryder.lib.models.SlashCommand;

public class HelpCommandOld implements SlashCommand, CommandOld {

    private final CommandData commandData;

    public HelpCommandOld(Ryder instance){
        this.commandData = new CommandData(getName(), getDescription());
        for (String entry : instance.getCommandHandler().getRequestMap().keySet()){
            this.commandData.addSubcommands(new SubcommandData(entry.trim(), "Help for "+entry.trim()+" command"));
        }
    }

    @Override
    public @NotNull String getName() {
        return "help";
    }

    @Override
    public @NotNull String getDescription() {
        return "Information about the bot";
    }

    @Override
    public void execute(SlashCommandEvent event) {

    }

    @Override
    public CommandData getCommandData() {
        return this.commandData;
    }

    @Override
    public void execute(MessageReceivedEvent event) {

    }

    @Override
    public String[] getAliases() {
        return CommandOld.super.getAliases();
    }
}