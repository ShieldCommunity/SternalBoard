# SternalBoard

![image](https://user-images.githubusercontent.com/76608233/146663681-08cf1e75-e288-44f4-8c79-fdda3531980b.png)

Ligthweigth & Async scoreboard based on FastBoard, implementing Animations. Packet based & supporting all versions.

## Official Downloads

* [SpigotMC page](https://www.spigotmc.org/resources/sternalboard-lightweight-animated-scoreboard.89245/)
* [Polymart page](https://polymart.org/resource/sternalboard-lightweight.1379)
* [MC-Market page](https://www.mc-market.org/resources/20395/)

## Maintainers
* [ShieldCommunity](https://github.com/ShieldCommunity) Currently mantainers mostly are community owners.

## Features
* Animated scoreboard - With all tasks running asynchronous
* Per world scoreboard - Optimal for multi-games
* Gradient scoreboard support - 1.16 up
* Multi-version scoreboard - Supports 1.7 to lastest
* Mini-games support, such as "koths, skywars, bedwars"
* Simple to use, for people who are relatively new to the industry
* Ready to run on large servers, with no animation limits

## How to integrate
If you want to integrate SternalBoard to your proyect, you can use Maven and Gradle:
Before nothing, remember that you need jitpack for it.

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
With sternalboard is really easy to set your first scoreboard!

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
