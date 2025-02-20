# SternalBoard

![image](https://user-images.githubusercontent.com/76608233/146663681-08cf1e75-e288-44f4-8c79-fdda3531980b.png)

Lightweight & Async packet-based scoreboard using FastBoard API, implementing animations, tab and supporting all versions.

## Official Downloads

* [SpigotMC page](https://www.spigotmc.org/resources/sternalboard-lightweight-animated-scoreboard.89245/)
* [Polymart page](https://polymart.org/resource/sternalboard-lightweight.1379)
* [MC-Market page](https://www.mc-market.org/resources/20395/)

## Maintainers
* [xism4](https://github.com/xism4) Main developer
* [jonakls](https://github.com/jonakls) Developer

## Features
* High-performance - Well-known libraries are used by their good resource management and cleanup. In addition to running most features asynchronously.
* Fully bedrock support - Other plugins have's strange view problems
* Tab system for header and footer - Customize your player's tab view
* World blacklist - Ability to disable the scoreboard in certain worlds
* Auto tab-completer for modern Paper, legacy, Spigot and forks
* MiniMessage formatting support - Adventure colours like <red>hello<reset>
* Automatic dependency download to avoid high weights - Also makes all Minecraft versions support
* Not dependency based - The plugin will start without relying on other plugins
* Packet-based - The plugin will not bug with plugins that use teams
* Animated scoreboard - The scoreboard data is constantly being modified
* Per world scoreboard - Ability to display different scoreboards in selected worlds
* Multi-version plugin - Supports 1.7 to lastest
* Mini-games support - The API was implemented in several mini-games supporting their custom scoreboards
* Simple to use - Very intuitive configuration for new people

## How to integrate
If you want to integrate SternalBoard to your proyect, you can use Maven and Gradle:
Before nothing, remember that you need jitpack for it (https://jitpack.io/#ShieldCommunity/SternalBoard)

## Maven
```xml
        <dependency>
            <groupId>com.github.ShieldCommunity</groupId>
            <artifactId>SternalBoard</artifactId>
            <version>2.2.9</version>
            <scope>compile</scope>
        </dependency>
```

## Gradle
```gradle
plugins {
    id 'com.github.johnrengelman.shadow' version '7.1.2'
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'com.github:shieldcommunity:2.2.9'
}

shadowJar {
    relocate 'com.shieldcommunity.sternalboard', 'you.yourpackage.like'
}
```

## Gradle DSL
```kts
plugins {
    id("com.github.johnrengelman.shadow") version("7.1.2")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github:shieldcommunity:2.2.9")
}

shadowJar {
    relocate("com.shieldcommunity.sternalboard", "you.yourpackage.like")
}
```

## Manually
If you just don't want to use a dependency-manager, you can copy our api classes to your proyect.

## How to hook into it
With SternalBoard, it's really easy to set your first scoreboard!

Either you can use our method of #setBoard, or manually.

```java
public class ExampleBoard {

    public void setYourScoreboard(Player player){
        SternalBoardHandler board = new SternalBoardHandler(player);

        board.updateTitle(ChatColor.GREEN+
                "Hypixel.net" //You can make it configurable trough strings tho
        );

        board.updateLines("Hello", //You can do lines as your version allows!
                "Bye",
                "Hello again :)"
        );
    }
}

```

#Servers using modern SternalBoard

<img src="https://bstats.org/signatures/bukkit/SternalBoard.svg" alt="ResourceHolders statics">
