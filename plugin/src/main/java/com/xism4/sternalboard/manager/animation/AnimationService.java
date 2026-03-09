package com.xism4.sternalboard.manager.animation;

import com.xism4.sternalboard.service.Service;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AnimationService implements Service {

    private final Map<String, AnimationTrack> titleTracks = new ConcurrentHashMap<>();
    private final Map<String, AnimationTrack> lineTracks = new ConcurrentHashMap<>();

    public ConfigurationSection next(ConfigurationSection animatedSection) {
        if (animatedSection == null) return null;

        String path = animatedSection.getCurrentPath();
        ConfigurationSection frame = new org.bukkit.configuration.MemoryConfiguration();

        ConfigurationSection titleSec = animatedSection.getConfigurationSection("title");
        if (titleSec != null) {
            String trackKey = path + ".title";
            AnimationTrack track = titleTracks.computeIfAbsent(trackKey, k -> buildTrack(titleSec));
            frame.set("title", track.next());
        }

        List<String> finalLines = new ArrayList<>();
        ConfigurationSection linesSec = animatedSection.getConfigurationSection("score-lines");

        if (linesSec != null) {
            Set<String> rawKeys = linesSec.getKeys(false);
            int[] keys = new int[rawKeys.size()];
            int i = 0;

            for (String k : rawKeys) {
                keys[i++] = Integer.parseInt(k);
            }

            Arrays.sort(keys);

            for (int key : keys) {
                ConfigurationSection lineSec = linesSec.getConfigurationSection(String.valueOf(key));
                if (lineSec != null) {
                    String trackKey = path + ".line." + key;
                    AnimationTrack track = lineTracks.computeIfAbsent(trackKey, k -> buildTrack(lineSec));
                    finalLines.add(track.next());
                } else {
                    finalLines.add("");
                }
            }
        }

        frame.set("lines", finalLines);
        return frame;
    }

    private AnimationTrack buildTrack(ConfigurationSection section) {
        int updateRate = section.getInt("update-rate", 20);
        List<String> frames = section.getStringList("lines");

        if (frames.isEmpty()) {
            frames = Collections.singletonList("");
        }

        return new AnimationTrack(frames, updateRate);
    }

    private static class AnimationTrack {

        private final List<String> frames;
        private final int updateRate;
        private int index = 0;
        private int ticks = 0;

        AnimationTrack(List<String> frames, int updateRate) {
            this.frames = frames;
            this.updateRate = Math.max(1, updateRate);
        }

        public String next() {
            if (++ticks >= updateRate) {
                ticks = 0;
                index = (index + 1) % frames.size();
            }
            return frames.get(index);
        }
    }

    public void reset() {
        titleTracks.clear();
        lineTracks.clear();
    }
}