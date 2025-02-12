package com.xism4.sternalboard.test.utils;

import com.xism4.sternalboard.util.TextUtils;
import org.junit.jupiter.api.Test;

public class TextUtilsTest {
    @Test
    public void transformLegacyHexTest() {
        String input = "#FFFFFFTest";
        String expected = "<color:#FFFFFF>Test";

        String output = TextUtils.transformLegacyHex(input);

        assert output.equals(expected);
    }

    @Test
    public void transformLegacyHexTest2() {
        String input = "&#FFFFFFTest#FFFFFFTest";
        String expected = "<color:#FFFFFF>Test<color:#FFFFFF>Test";

        String output = TextUtils.transformLegacyHex(input);

        assert output.equals(expected);
    }
}
