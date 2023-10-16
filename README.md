# SternalBoard

![image](https://user-images.githubusercontent.com/76608233/146663681-08cf1e75-e288-44f4-8c79-fdda3531980b.png)

Lightweight & Async scoreboard based on FastBoard, implementing animations, packet based & supporting all versions.

## Official Downloads

* [SpigotMC page](https://www.spigotmc.org/resources/sternalboard-lightweight-animated-scoreboard.89245/)
* [Polymart page](https://polymart.org/resource/sternalboard-lightweight.1379)
* [MC-Market page](https://www.mc-market.org/resources/20395/)

## Maintainers
* [ShieldCommunity](https://github.com/ShieldCommunity) Currently mantainers are mostly community owners.

## Why another branch?
The way SternalBoard works has been changed, since before it was not possible to implement other features because of the way it was thought, so now it will be modularized in versions to avoid the use of excessive Reflection.
Implementation of new APIs such as inject and caffeine to replace the old ones
This will be merged on main branch when its ready, feel free to help on the new system


## Features
Fully bedrock support - Other plugins have's strange view problems
World blacklist - If you don't want the scoreboard appear on some worlds you can!
Auto tab-completer for modern Paper, legacy, Spigot and forks
MiniMessage formatting support - Intutive colours like hello
Automatic dependency download to avoid high weights - Also makes all Minecraft versions support
Not dependency based - Doesn't need other software to run properly
Packet-based - The plugin will not bug with plugins that use teams
Animated scoreboard - With all tasks running asynchronous
Per world scoreboard - Optimal for multi-games
Gradient scoreboard support - 1.16 up
Multi-version scoreboard - Supports 1.7 to lastest
Mini-games support, such as "koths, skywars, bedwars"
Simple to use, for people who are relatively new to the industry
Ready to run on large servers, with no animation limits

## How to integrate
If you want to integrate SternalBoard to your proyect, you can use Maven and Gradle:
Before nothing, remember that you need jitpack for it (https://jitpack.io/#ShieldCommunity/SternalBoard)

## Maven
```xml
        <dependency>
            <groupId>com.github.ShieldCommunity</groupId>
            <artifactId>SternalBoard</artifactId>
            <version>2.0.1</version>
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
    implementation 'com.github:shieldcommunity:2.0.2'
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
    implementation("com.github:shieldcommunity:2.0.2")
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
