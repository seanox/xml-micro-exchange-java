/**
 * LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 * im Folgenden Seanox Software Solutions oder kurz Seanox genannt.
 * Diese Software unterliegt der Version 2 der Apache License.
 *
 * XMEX XML-Micro-Exchange
 * Copyright (C) 2024 Seanox Software Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.seanox.xmex.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NumberTest {

    @Test
    void testparseLongError_1() {
        Assertions.assertThrows(
                Number.NumberFormatException.class,
                () -> Number.parseLong(null));
        Assertions.assertThrows(
                Number.NumberFormatException.class,
                () -> Number.parseLong(""));
        Assertions.assertThrows(
                Number.NumberFormatException.class,
                () -> Number.parseLong(" 1"));
        Assertions.assertThrows(
                Number.NumberFormatException.class,
                () -> Number.parseLong("1 "));
        Assertions.assertThrows(
                Number.NumberFormatException.class,
                () -> Number.parseLong(" 1 "));
    }

    @Test
    void testparseLongError_2() {
        Assertions.assertThrows(
                Number.NumberFormatException.class,
                () -> Number.parseLong("1MiB", true));
        Assertions.assertThrows(
                Number.NumberFormatException.class,
                () -> Number.parseLong("1MiB", false));
    }

    @Test
    void testparseLong_1() {
        Assertions.assertEquals(0, Number.parseLong("0"));
        Assertions.assertEquals(0, Number.parseLong("00"));
        Assertions.assertEquals(0, Number.parseLong("000"));

        Assertions.assertEquals(5, Number.parseLong("05"));
        Assertions.assertEquals(5, Number.parseLong("005"));
        Assertions.assertEquals(5, Number.parseLong("0005"));

        Assertions.assertEquals(-5, Number.parseLong("-05"));
        Assertions.assertEquals(-5, Number.parseLong("-005"));
        Assertions.assertEquals(-5, Number.parseLong("-0005"));
    }

    @Test
    void testparseLong_2() {
        Assertions.assertEquals(0, Number.parseLong("0k"));
        Assertions.assertEquals(0, Number.parseLong("00 M"));
        Assertions.assertEquals(0, Number.parseLong("000  g"));

        Assertions.assertEquals(5000, Number.parseLong("05k"));
        Assertions.assertEquals(5000000, Number.parseLong("005 M"));
        Assertions.assertEquals(5000000000L, Number.parseLong("0005  g"));

        Assertions.assertEquals(-5000, Number.parseLong("-05k"));
        Assertions.assertEquals(-5000000, Number.parseLong("-005 M"));
        Assertions.assertEquals(-5000000000L, Number.parseLong("-0005  g"));
    }

    @Test
    void testparseLong_3() {
        Assertions.assertEquals(0, Number.parseLong("0ki"));
        Assertions.assertEquals(0, Number.parseLong("00 Mi"));
        Assertions.assertEquals(0, Number.parseLong("000  gI"));

        Assertions.assertEquals(5120, Number.parseLong("05ki"));
        Assertions.assertEquals(5242880, Number.parseLong("005 Mi"));
        Assertions.assertEquals(5368709120L, Number.parseLong("0005  gI"));

        Assertions.assertEquals(-5120, Number.parseLong("-05ki"));
        Assertions.assertEquals(-5242880, Number.parseLong("-005 Mi"));
        Assertions.assertEquals(-5368709120L, Number.parseLong("-0005  gI"));
    }

    @Test
    void testparseLong_4() {
        Assertions.assertEquals(512, Number.parseLong("0.5ki"));
        Assertions.assertEquals(524288, Number.parseLong("00.5 Mi"));
        Assertions.assertEquals(536870912, Number.parseLong("000.5  gI"));

        Assertions.assertEquals(5632, Number.parseLong("05.5ki"));
        Assertions.assertEquals(5767168, Number.parseLong("005.5 Mi"));
        Assertions.assertEquals(5905580032L, Number.parseLong("0005.5  gI"));

        Assertions.assertEquals(-5632, Number.parseLong("-05.5ki"));
        Assertions.assertEquals(-5767168, Number.parseLong("-005.5 Mi"));
        Assertions.assertEquals(-5905580032L, Number.parseLong("-0005.5  gI"));
    }

    @Test
    void testparseLong_5() {
        Assertions.assertEquals(0, Number.parseLong("0kiB"));
        Assertions.assertEquals(0, Number.parseLong("00 Mib"));
        Assertions.assertEquals(0, Number.parseLong("000  gIB"));

        Assertions.assertEquals(5120, Number.parseLong("05kib"));
        Assertions.assertEquals(5242880, Number.parseLong("005 MiB"));
        Assertions.assertEquals(5368709120L, Number.parseLong("0005  gIb"));

        Assertions.assertEquals(-5120, Number.parseLong("-05kiB"));
        Assertions.assertEquals(-5242880, Number.parseLong("-005 Mib"));
        Assertions.assertEquals(-5368709120L, Number.parseLong("-0005  gIB"));
    }

    @Test
    void testparseLong_6() {
        Assertions.assertEquals(512, Number.parseLong("0.5kiB"));
        Assertions.assertEquals(524288, Number.parseLong("00.5 Mib"));
        Assertions.assertEquals(536870912, Number.parseLong("000.5  gIB"));

        Assertions.assertEquals(5632, Number.parseLong("05.5kiB"));
        Assertions.assertEquals(5767168, Number.parseLong("005.5 Mib"));
        Assertions.assertEquals(5905580032L, Number.parseLong("0005.5  gIB"));

        Assertions.assertEquals(-5632, Number.parseLong("-05.5kib"));
        Assertions.assertEquals(-5767168, Number.parseLong("-005.5 MiB"));
        Assertions.assertEquals(-5905580032L, Number.parseLong("-0005.5  gIb"));
    }

    @Test
    void testparseLong_7() {
        Assertions.assertEquals(512, Number.parseLong("0.5kB", true));
        Assertions.assertEquals(524288, Number.parseLong("00.5 Mb", true));
        Assertions.assertEquals(536870912, Number.parseLong("000.5  gB", true));

        Assertions.assertEquals(5632, Number.parseLong("05.5kB", true));
        Assertions.assertEquals(5767168, Number.parseLong("005.5 Mb", true));
        Assertions.assertEquals(5905580032L, Number.parseLong("0005.5  gB", true));

        Assertions.assertEquals(-5632, Number.parseLong("-05.5kb", true));
        Assertions.assertEquals(-5767168, Number.parseLong("-005.5 MB", true));
        Assertions.assertEquals(-5905580032L, Number.parseLong("-0005.5  gb", true));
    }

    @Test
    void testparseLong_8() {
        Assertions.assertEquals(0, Number.parseLong("0k", false));
        Assertions.assertEquals(0, Number.parseLong("00 M", false));
        Assertions.assertEquals(0, Number.parseLong("000  g", false));

        Assertions.assertEquals(5000, Number.parseLong("05k", false));
        Assertions.assertEquals(5000000, Number.parseLong("005 M", false));
        Assertions.assertEquals(5000000000L, Number.parseLong("0005  g", false));

        Assertions.assertEquals(-5000, Number.parseLong("-05k", false));
        Assertions.assertEquals(-5000000, Number.parseLong("-005 M", false));
        Assertions.assertEquals(-5000000000L, Number.parseLong("-0005  g", false));
    }
}
