# SternalBoard

![image](https://user-images.githubusercontent.com/76608233/146663681-08cf1e75-e288-44f4-8c79-fdda3531980b.png)

Lightweight & Async packet-based multi-version scoreboard using FastBoard API.

## Official Downloads

* [SpigotMC page](https://www.spigotmc.org/resources/sternalboard-lightweight-animated-scoreboard.89245/)
* [Polymart page](https://polymart.org/resource/sternalboard-lightweight.1379)
* [MC-Market page](https://www.mc-market.org/resources/20395/)

## Maintainers
* [xism4](https://github.com/xism4) Main developer
* [jonakls](https://github.com/jonakls) Developer

## Features
* High-performance - Well-known libraries are used by their good resource management and cleanup. In addition to running most features asynchronously.
* Bedrock support - Will look exactly the same as on other platforms with no vision problems
* Tab system - You will be able to modify the header and footer of the player view 
* World blacklist - Ability to disable the scoreboard in certain worlds
* Auto tab-completer for modern Paper, legacy, Spigot and forks
* MiniMessage formatting support - Adventure colours like <red>hello<reset>
* Packet-based - The plugin will not bug with plugins that use teams
* Animated scoreboard - The scoreboard data is constantly being modified
* Per world scoreboard - Ability to display different scoreboards in selected worlds
* Multi-version plugin - Supports 1.7 to lastest
* Mini-games support - The API was implemented in several mini-games supporting their custom scoreboards
* Simple to use - Very intuitive configuration for new people
* Maximum weight compression thanks to library downloader
* The plugin will start without relying on other plugins
  
## How to integrate
If you want to integrate SternalBoard to your proyect, you can use Maven and Gradle:
Before nothing, remember that you need jitpack for it (https://jitpack.io/#ShieldCommunity/SternalBoard)

## Maven
```xml
        <dependency>
            <groupId>com.github.ShieldCommunity</groupId>
            <artifactId>SternalBoard</artifactId>
            <version>2.3.1</version>
            <scope>compile</scope>
        </dependency>
```

## Gradle
```gradle
plugins {
    id 'com.github.johnrengelman.shadow' version '8.1.1'
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'com.xism4.sternalboard:2.3.1'
}

shadowJar {
    relocate 'com.xism4.sternalboard', 'you.package.like.scoreboard'
}
```

## Gradle DSL
```kts
plugins {
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.xism4:sternalboard:2.3.1")
}

shadowJar {
    relocate("com.xism4.sternalboard", "you.yourpackage.like.scoreboard")
}
```

## Manually
You can copy the classes and paste them into your project if you are not yet using a dependency system such as gradle or maven

## How to hook into it
With a simple call you can create your first scoreboard

```java
public class ExampleBoard {

    public void setScoreboard(Player player){
        SternalBoardHandler board = new SternalBoardHandler(player);

        board.updateTitle(ChatColor.GREEN+
                "Hypixel.net" //You can make it configurable trough strings tho
        );

        board.updateLines("Hello", //You can do lines as your version allows!
                "shieldcommunity.net",
        );
    }
}

```

#Servers using SternalBoard

<img src="https://bstats.org/signatures/bukkit/SternalBoard.svg" alt="ResourceHolders statics">
