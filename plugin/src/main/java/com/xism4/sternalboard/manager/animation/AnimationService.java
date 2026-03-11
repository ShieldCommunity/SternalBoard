package com.xism4.sternalboard.manager.animation;

import com.xism4.sternalboard.service.Service;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AnimationService implements Service {

    private final Map<String, AnimationTrack> titleTracks = new ConcurrentHashMap<>();
    private final Map<String, AnimationTrack> lineTracks = new ConcurrentHashMap<>();

    public ConfigurationSection next(final ConfigurationSection animatedSection) {
        if (animatedSection == null) return null;

        final String path = animatedSection.getCurrentPath();
        final ConfigurationSection frame = new MemoryConfiguration();

        final ConfigurationSection titleSec = animatedSection.getConfigurationSection("title");
        if (titleSec != null) {
            final AnimationTrack track = titleTracks.computeIfAbsent(
                    path + ".title",
                    k -> buildTrack(titleSec)
            );
            frame.set("title", track.next());
        }

        final List<String> finalLines = new ArrayList<>();
        final ConfigurationSection linesSec = animatedSection.getConfigurationSection("score-lines");

        if (linesSec != null) {
            final List<Integer> keys = new ArrayList<>();

            for (final String key : linesSec.getKeys(false)) {
                keys.add(Integer.parseInt(key));
            }

            Collections.sort(keys);

            for (final int key : keys) {
                final ConfigurationSection lineSec = linesSec.getConfigurationSection(String.valueOf(key));

                if (lineSec == null) {
                    finalLines.add("");
                    continue;
                }

                final AnimationTrack track = lineTracks.computeIfAbsent(
                        path + ".line." + key,
                        k -> buildTrack(lineSec)
                );

                finalLines.add(track.next());
            }
        }

        frame.set("lines", finalLines);
        return frame;
    }

    private AnimationTrack buildTrack(final ConfigurationSection section) {
        final int updateRate = section.getInt("update-rate", 20);
        List<String> frames = section.getStringList("lines");

        if (frames.isEmpty()) {
            frames = Collections.singletonList("");
        }

        return new AnimationTrack(frames, updateRate);
    }

    private static class AnimationTrack {

        private final List<String> frames;
        private final int updateRate;

        private int index;
        private int ticks;

        AnimationTrack(final List<String> frames, final int updateRate) {
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