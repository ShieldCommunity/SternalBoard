# SternalBoard

![image](https://user-images.githubusercontent.com/76608233/146663681-08cf1e75-e288-44f4-8c79-fdda3531980b.png)

Lightweight packet-based multi-version scoreboard using FastBoard API.

## Official Downloads

* [SpigotMC page](https://www.spigotmc.org/resources/sternalboard-lightweight-animated-scoreboard.89245/)
* [Polymart page](https://polymart.org/resource/sternalboard-lightweight.1379)
* [BBB page](https://www.mc-market.org/resources/20395/)

## Maintainers
* [xism4](https://github.com/xism4) Main developer
* [jonakls](https://github.com/jonakls) Developer

## Features
* Efficient - The server shouldn't notice the exist of this plugin. Most of tasks are running asynchronously. 
* Bedrock support - Connections will display the plugin correctly without line breaks
* Tab system - Modify the modern header and footer component through the settings. 
* World blacklist - Ability to disable the scoreboard in certain worlds.
* Auto tab-completer for modern Paper, Legacy Paper and Spigot.
* Animated scoreboard - The scoreboard data is constantly being modified
* Per world scoreboard - Ability to display different scoreboards in selected worlds.
* Configuration designed to be very simple and quick to set up.
* The plugin will start without relying on other plugins plus shouldn't affect team-based plugins.
* Multi-version plugin - Supports 1.7 to lastest.

## Java 
The software will continue to support older versions, but due to modern techniques, you will need to use any fork that supports a modern java version for it to function correctly.
  
## How to integrate
If you want to integrate SternalBoard to your proyect, you can use Maven and Gradle:
Before nothing, remember that you need jitpack for it (https://jitpack.io/#ShieldCommunity/SternalBoard)

## Maven
```xml
        <dependency>
            <groupId>com.github.ShieldCommunity</groupId>
            <artifactId>SternalBoard</artifactId>
            <version>LATEST</version>
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
    implementation 'com.xism4.sternalboard:LATEST'
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
    implementation("com.xism4:sternalboard:LATEST")
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
