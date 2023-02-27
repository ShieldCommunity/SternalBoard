package com.xism4.sternalboard.api.tablist;

import java.util.Collection;

public interface TabListHandler {

    boolean header(String header);

    boolean footer(String footer);

    boolean update(String header, String footer);

    boolean clear();

    default boolean stop() {
        return clear();
    }

    default boolean header(String... header) {
        return header(String.join("\n", header));
    }

    default boolean footer(String... footer) {
        return footer(String.join("\n", footer));
    }

    default boolean header(Collection<String> header) {
        return header(header.toArray(new String[0]));
    }

    default boolean footer(Collection<String> footer) {
        return footer(footer.toArray(new String[0]));
    }

}
