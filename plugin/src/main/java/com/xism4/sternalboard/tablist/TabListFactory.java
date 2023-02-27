package com.xism4.sternalboard.tablist;

import com.xism4.sternalboard.api.tablist.TabListHandler;
import com.xism4.sternalboard.v1_8_R3.SternalTabListHandlerImpl;

public class TabListFactory {

    public static TabListHandler create() {
        return new SternalTabListHandlerImpl();
    }

}
