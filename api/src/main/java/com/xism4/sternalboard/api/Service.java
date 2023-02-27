package com.xism4.sternalboard.api;

public interface Service {

    void start();

    default void stop() {
        // Do nothing
    }

}
