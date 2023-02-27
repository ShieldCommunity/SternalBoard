package com.xism4.sternalboard.v1_8_R3;

import com.xism4.sternalboard.api.tablist.TabListHandler;

import java.util.Collection;

public class SternalTabListHandlerImpl implements TabListHandler {

    @Override
    public boolean header(String header) {
        return false;
    }

    @Override
    public boolean footer(String footer) {
        return false;
    }

    @Override
    public boolean update(String header, String footer) {
        return false;
    }

    @Override
    public boolean clear() {
        return false;
    }

    @Override
    public boolean stop() {
        return TabListHandler.super.stop();
    }

    @Override
    public boolean header(String... header) {
        return TabListHandler.super.header(header);
    }

    @Override
    public boolean footer(String... footer) {
        return TabListHandler.super.footer(footer);
    }

    @Override
    public boolean header(Collection<String> header) {
        return TabListHandler.super.header(header);
    }

    @Override
    public boolean footer(Collection<String> footer) {
        return TabListHandler.super.footer(footer);
    }
}
