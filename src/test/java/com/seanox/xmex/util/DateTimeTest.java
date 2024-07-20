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

class DateTimeTest {

    @Test
    void testparseDuration_1() {

        Assertions.assertThrows(
                DateTime.DateTimeFormatException.class,
                () -> DateTime.parseDuration(null));
        Assertions.assertThrows(
                DateTime.DateTimeFormatException.class,
                () -> DateTime.parseDuration(""));
        Assertions.assertThrows(
                DateTime.DateTimeFormatException.class,
                () -> DateTime.parseDuration(" 1 s"));
        Assertions.assertThrows(
                DateTime.DateTimeFormatException.class,
                () -> DateTime.parseDuration("1 s "));
        Assertions.assertThrows(
                DateTime.DateTimeFormatException.class,
                () -> DateTime.parseDuration(" 1 s "));
        Assertions.assertThrows(
                DateTime.DateTimeFormatException.class,
                () -> DateTime.parseDuration("-1s"));
    }

    @Test
    void testparseDuration_2() {
        Assertions.assertEquals(0, DateTime.parseDuration("0 s"));
        Assertions.assertEquals(0, DateTime.parseDuration("00s"));
        Assertions.assertEquals(0, DateTime.parseDuration("000S"));

        Assertions.assertEquals(5000, DateTime.parseDuration("05 s"));
        Assertions.assertEquals(5000, DateTime.parseDuration("005s"));
        Assertions.assertEquals(5000, DateTime.parseDuration("0005S"));

        Assertions.assertEquals(5, DateTime.parseDuration("05 ms"));
        Assertions.assertEquals(5, DateTime.parseDuration("005ms"));
        Assertions.assertEquals(5, DateTime.parseDuration("0005mS"));
    }

    @Test
    void testparseDuration_3() {
        Assertions.assertEquals(500, DateTime.parseDuration("0.5 s"));
        Assertions.assertEquals(500, DateTime.parseDuration("00.5s"));
        Assertions.assertEquals(500, DateTime.parseDuration("000.5S"));

        Assertions.assertEquals(5500, DateTime.parseDuration("05.5 s"));
        Assertions.assertEquals(5500, DateTime.parseDuration("005.5s"));
        Assertions.assertEquals(5500, DateTime.parseDuration("0005.5S"));

        Assertions.assertEquals(6, DateTime.parseDuration("05.5 ms"));
        Assertions.assertEquals(6, DateTime.parseDuration("005.5ms"));
        Assertions.assertEquals(6, DateTime.parseDuration("0005.5mS"));
    }
}
