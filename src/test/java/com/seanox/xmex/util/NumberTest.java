/**
 * LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 * im Folgenden Seanox Software Solutions oder kurz Seanox genannt.
 * Diese Software unterliegt der Version 2 der Apache License.
 *
 * XMEX XML-Micro-ExchangExchange
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
    void testparseLong_1() {
        Assertions.assertThrows(
                NumberFormatException.class,
                () -> Number.parseLong(null));
        Assertions.assertThrows(
                NumberFormatException.class,
                () -> Number.parseLong(""));
        Assertions.assertThrows(
                NumberFormatException.class,
                () -> Number.parseLong(" 1"));
        Assertions.assertThrows(
                NumberFormatException.class,
                () -> Number.parseLong("1 "));
        Assertions.assertThrows(
                NumberFormatException.class,
                () -> Number.parseLong(" 1 "));
    }

    @Test
    void testparseLong_2() {
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
    void testparseLong_3() {
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
    void testparseLong_4() {
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
}
