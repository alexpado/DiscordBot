## DiscordBot Library
![Java CI with Gradle](https://github.com/alexpado/DiscordBot/workflows/Java%20CI%20with%20Gradle/badge.svg)


***Note** This is sill in beta but should be pretty stable. If you have any problem or suggestion, please feel free
 to open an issue.*
 
This project exists mainly because I was tired to copy paste my "command manager" over and over across all projects.
By doing this library, I enhanced my command system with a kind of command templating system allowing fast command
creation.
 
### Creating the bot class

To create a bot, we need the bot class, of couse ! Here's a simple example:

```java
public class MyBot extends DiscordBotImpl {

    public MyBot() {
        super("!");
        this.login("YOUR_BOT_TOKEN_GOES_HERE");
    }

}
```

If you run this piece of code (by replacing the token placeholder howerever you want, of course), you'll see your bot 
online !

For now, your bot is quite... pointless. Let's add a way to interact with it !

### Creating a Command
 
First, let's create a simple command allowing you to see on how many server your bot is, and maybe displaying a list with
one server per line.

```java
public class GuildCommand extends DiscordCommand {

    protected BotCommand(IDiscordBot discordBot) {
        super(discordBot);
    }

    @Command("guild count")
    public void getGuildCount(JDA jda, MessageChannel channel) {
        String message = String.format("I'm on %s guilds !", jda.getGuilds().size());
        channel.sendMessage(message).queue();
    }

    @Command("guild list")
    public void getGuildNames(JDA jda, MessageChannel channel) {
        StringBuilder builder = new StringBuilder();

        for (Guild guild : jda.getGuilds()) {
            builder.append(guild.getName()).append("\n");
        }

        channel.sendMessage(builder.toString()).queue();
    }

}
```

We use the `@Command` annotation to tell the program that a method is, in fact, a "command executor". These
will be auto-detected by the code when a command will be executed.
But before going any further, let's register it by going back to our bot class.

```java
public class MyBot extends DiscordBotImpl {

    public MyBot() {
        super("!");
        this.getCommandHandler().register("info", new GuildCommand(this));
        this.login("YOUR_BOT_TOKEN_GOES_HERE");
    }
    
}
```

As you can see here, our command will be register as "info", using the prefix "!".
If we run it now and type `!info guild count`, we'll be able to see on how many server our bot is !

> But hold on, how the hell the objects "JDA" and "MessageChannel" get passed to our methods here ?

Good question ! Simple answer : Every object commonly used when handling a command can be obtained just by
adding them in the parameters list !

You want the guild ? just add `Guild guild` in your method signature ! The author ? `User author` !
The message received ? `Message message` ! The name of your parameter doesn't matter, the type does.

Simple !

> Okay, neat. But if I want to retrieve something the user have to type, how I'm supposed to do it ?

Here comes the power of the templating system ! *(please don't look at the source code, it's quite messy for now)*

You can create custom syntax very easy, and there is multiple type !

###### The Regular Syntax
This is what we already used, one word, and only one can be matched at the position defined !

`guild count` will only match if the user type `guild count` and nothing else!

###### The Pass-Through Syntax
This one is used when you want to retrieve one word from the user's input.

`guild rename [name]` will match `guild rename Amazing` but also `guild rename EOAEOUNACAECIAYBEPVCIAYEBVCP` !

> How do I retrieve it in my command ?

Very simple !

```java
    @Command("guild rename [name]")
    public void renameGuild(@Param("name") String something) {
        // Do something with "something"
    }
```

> Does this work with multiple syntax parameter ?

Absolutly ! In fact, you can write something like this:

```java
    @Command("say [some] [word] [are] [bad]")
    public void sayFourWords(@Param("some") String some, @Param("word") String word, @Param("are") String are, @Param
("bad") String bad) {
        // Do something
    }
```

You can also use a pass-through syntax between two regulars syntax like so:

```java
    @Command("dog [name] pet")
    public void petTheDog(@Param("name") String name) {
        // Pet the dog ! Peeeeet the dooooog !
    }
```

###### The Filler Syntax
This one is used when you want to retrieve a variable amount of words from your user.

```java
    @Command("say message...")
    public void echoBackMessage(@Param("message") String message) {
        // Do something
    }
```

You can notice the `...`: This is a filler. That means that everything after "say" will be captured in the "message" parameter.
Quite useful when you don't known if your user is going to write a word or a complete book.

**Please do note that the filler syntax is only supported at the end of a command template !**


### Adding the library to your project:

First, you need to add the Jitpack.io repository:

```gradle
repositories {
    // Your other repositories
    maven { url 'https://jitpack.io' }
}
```

Then you can add the dependency:

```gradle
    implementation 'com.github.alexpado:DiscordBot:v1.0b1'
```

**Note** The library doesn't include the JDA dependency, you need to add it to your dependencies.