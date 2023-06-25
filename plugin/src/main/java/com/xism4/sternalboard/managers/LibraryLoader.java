package com.xism4.sternalboard.managers;

import com.xism4.sternalboard.SternalBoardPlugin;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;

public class LibraryLoader {

    public static void loadLibs(SternalBoardPlugin plugin) {
        BukkitLibraryManager manager = new BukkitLibraryManager(plugin);

        manager.addMavenCentral();
        manager.addJitPack();
        manager.addSonatype();
        manager.addRepository("https://jitpack.io");

        LibraryManager.load();
        LibraryManager.getLibs().forEach(manager::loadLibrary);

    }
}

