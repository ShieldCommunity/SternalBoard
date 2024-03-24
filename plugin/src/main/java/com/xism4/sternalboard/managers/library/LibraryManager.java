package com.xism4.sternalboard.managers.library;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.alessiodp.libby.BukkitLibraryManager;
import com.alessiodp.libby.Library;

import java.util.ArrayList;

public class LibraryManager {

    public static ArrayList<Library> libs = new ArrayList<>();

    public static String ADVENTURE_VERSION = "4.17.0-SNAPSHOT";

    public static ArrayList<Library> getLibs() {
        return libs;
    }

    public static void load(){
        Library miniMessage = Library.builder()
                .groupId("net{}kyori")
                .artifactId("adventure-text-minimessage")
                .version(ADVENTURE_VERSION)
                .isolatedLoad(false)
                .relocate("net{}kyori", "com{}xism4{}sternalboard{}libs{}kyori")
                .build();

        Library adventureAPI = Library.builder()
                .groupId("net{}kyori")
                .artifactId("adventure-api")
                .version(ADVENTURE_VERSION)
                .isolatedLoad(false)
                .relocate("net{}kyori", "com{}xism4{}sternalboard{}libs{}kyori")
                .build();

        Library adventureBukkitPlatform = Library.builder()
                .groupId("net{}kyori")
                .artifactId("adventure-platform-bukkit")
                .version("4.3.3-SNAPSHOT")
                .isolatedLoad(false)
                .relocate("net{}kyori", "com{}xism4{}sternalboard{}libs{}kyori")
                .build();

        Library examination = Library.builder()
                .groupId("net{}kyori")
                .artifactId("examination-api")
                .version("1.3.1-SNAPSHOT")
                .isolatedLoad(false)
                .relocate("net{}kyori", "com{}xism4{}sternalboard{}libs{}kyori")
                .build();

        Library adventurePlatformApi = Library.builder()
                .groupId("net{}kyori")
                .artifactId("adventure-platform-api")
                .version("4.3.3-SNAPSHOT")
                .isolatedLoad(false)
                .relocate("net{}kyori", "com{}xism4{}sternalboard{}libs{}kyori")
                .build();

        Library adventurePlatformFacet = Library.builder()
                .groupId("net{}kyori")
                .artifactId("adventure-platform-facet")
                .version("4.3.3-SNAPSHOT")
                .isolatedLoad(false)
                .relocate("net{}kyori", "com{}xism4{}sternalboard{}libs{}kyori")
                .build();

        Library adventureKey = Library.builder()
                .groupId("net{}kyori")
                .artifactId("adventure-key")
                .version(ADVENTURE_VERSION)
                .isolatedLoad(false)
                .relocate("net{}kyori", "com{}xism4{}sternalboard{}libs{}kyori")
                .build();

        libs.add(getAdventureLib("adventure-text-serializer-legacy"));
        libs.add(getAdventureLib("adventure-text-serializer-plain"));
        libs.add(getAdventureLib("adventure-text-serializer-json"));
        libs.add(getAdventureLib("adventure-text-serializer-ansi"));
        libs.add(getAdventureLib("adventure-text-serializer-gson"));
        libs.add(getAdventureLib("adventure-nbt"));
        libs.add(getAdventureLib("adventure-text-serializer-json-legacy-impl"));
        libs.add(getAdventureLib("adventure-text-serializer-gson-legacy-impl"));


        libs.add(miniMessage);
        libs.add(adventureAPI);
        libs.add(examination);
        libs.add(adventurePlatformApi);
        libs.add(adventureBukkitPlatform);
        libs.add(adventurePlatformFacet);
        libs.add(adventureKey);
    }

    /**
     * Get a library from Maven Central or JitPack
     * @param groupID The group ID of the library
     * @param artifact The artifact ID of the library
     * @param version The version of the library
     * @param relocate The package to relocate the library to
     * @return The library
     */
    public static Library getLib(String groupID, String artifact, String version, String relocate) {
        return Library.builder()
                .groupId(groupID)
                .artifactId(artifact)
                .version(version)
                .isolatedLoad(false)
                .relocate(groupID, relocate)
                .build();
    }

    /**
     * @param groupID The groupID of the library
     * @param artifact The artifact of the library
     * @param version The version of the library
     * @return The library
     */
    public static Library getLib(String groupID, String artifact, String version) {
        return Library.builder()
                .groupId(groupID)
                .artifactId(artifact)
                .version(version)
                .isolatedLoad(false)
                .build();
    }

    /**
     * Get a library by its ID
     * @param lib The ID of the library
     * @param relocation The relocation of the library
     * @return The library
     */
    public static Library getLib(String lib, String relocation){
        String[] libInfo = lib.split(":");
        return getLib(
                libInfo[0],
                libInfo[1],
                libInfo[2],
                relocation
        );
    }

    public static Library getAdventureLib(String artifact) {
        return Library.builder()
                .groupId("net{}kyori")
                .artifactId(artifact)
                .version(ADVENTURE_VERSION)
                .isolatedLoad(false)
                .relocate("net{}kyori", "com{}xism4{}sternalboard{}libs{}kyori")
                .build();
    }

    public static void loadLibs(SternalBoardPlugin plugin) {
        BukkitLibraryManager manager = new BukkitLibraryManager(plugin);

        manager.addMavenCentral();
        manager.addJitPack();
        manager.addSonatype();
        manager.addRepository("https://jitpack.io");
        manager.addRepository("https://s01.oss.sonatype.org/content/repositories/snapshots/");

        LibraryManager.load();
        LibraryManager.getLibs().forEach(manager::loadLibrary);

    }
}