# Ryder
My own extension library for JDA<br>
The Ryder library handles the annoying aspecst of creating a Discord Bot using JDA<br>
for example creating commands and handling them properly or creating EmbedBuilder templates,<br>
eliminating duplicate EmbedBuilder code.
<br>
### Usage
This library is not hosted anywhere so you will have to install it locally to be able to use it as a dependency
```bash
git clone https://github.com/nikoci/Ryder
cd ./Ryder
mvn clean install
```

Then specify it as a dependency inside of your pom.xml file
```xml
<dependency>
    <groupId>se.nikoci</groupId>
    <artifactId>Ryder</artifactId>
    <version>VERSION</version>
</dependency>
```

The code block below showcases how you can use Ryder to implement commands with no effort using lambda expressions.<br>
In this example we are creating a total of 4 commands:<br>
`!info`, `!info link`, `/greet hello` and `/greet hi`<br>
Note that we don't get a `/greet` command due to the structure of a [Slash Command](https://discord.com/developers/docs/interactions/application-commands)
```java
public static void main(String[] args) throws InterruptedException {
    // Creating an instance of Ryder
    var ryder = Ryder.create("<TOKEN HERE>", Set.of(
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.MESSAGE_CONTENT
    )).online();

    // Creating a normal command. Example: !info
    var normalCmd = Command.create("info", "Shows information");
    normalCmd.onMessage(ca -> ca.getEvent().getChannel().sendMessage("Made by Arijan Nikoci").queue());

    // Creating a subcommand, setting the execution of the command using constructor
    var normalSubCmd = Command.create("link", "Link to repository",
            ca -> ca.getEvent().getChannel().sendMessage("https://github.com/nikoci/Ryder").queue());

    // Adding normalSubCmd as a subcommand for normalCmd
    normalCmd.addSubcommands(normalSubCmd);

    // Creation of a slash command. Example: /greet
    var slashCmd = Command.createSlash("greet", "A greeting command");

    // Creating a subcommand, setting the execution of the command using Command#onSlash(SlashCommand)
    var slashSubCmd1 = Command.createSlash("hello", "says hello");
    slashSubCmd1.onSlash(ca -> ca.getEvent().reply("Hello there " + e.getUser().getAsTag()).queue());

    // Creating a subcommand, setting the execution of the command using constructor
    var slashSubCmd2 = Command.createSlash("hi", "says hi",
            ca -> ca.getEvent().reply("Hi there " + e.getUser().getAsTag()).queue());

    // Adding slashSubCmd1 and slashSubCmd2 as subcommands for slashCmd
    slashCmd.addSubcommands(slashSubCmd1, slashSubCmd2);


    // Finally registering the commands (Only parents)
    ryder.getCommandHandler().registerCommand(normalCmd, slashCmd);
}
```
You can also extend the Command class to create a command per class, overriding the execution methods. Although this is possible, it is not recommended.
The other option is to create your own class and implement either `SlashCommand` or `MessageCommand` and setting the execution of
your command to the new class by `onSlash(Object<? implements SlashCommand>)` and/or `onMessage(Object<? implements MessageCommand>)`

Example:

```java
class MyCommand implements MessageCommand, SlashCommand {
    @Override
    public void execute(MessageCommand.CommandAction commandAction) {
        commandAction.getEvent().getChannel().sendMessage(command.getDescription()).queue();
    }

    @Override
    public void execute(SlashCommand.CommandAction commandAction) {
        commandAction.getEvent().reply(command.getDescription()).queue();
    }
}

class MyBot {
    public static void main(String[] args) throws InterruptedException {
        // Creating an instance of Ryder
        var ryder = Ryder.create("<TOKEN HERE>", Set.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT
        )).online();

        var exec = new MyCommand();

        var myNormalCmd = Command.create("normalcmd", "a normal command", exec);
        var mySlashCmd = Command.createSlash("slashcmd", "a slash command", exec);

        ryder.getCommandHandler().registerCommand(myNormalCmd, mySlashCmd);
    }
}
```
