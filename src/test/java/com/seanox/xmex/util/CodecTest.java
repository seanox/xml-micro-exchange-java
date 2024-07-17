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

class CodecTest {

    @Test
    void testDecodeHex_1() {
        Assertions.assertNull(Codec.decodeHex(null));
    }

    @Test
    void testDecodeHex_2() {
        Assertions.assertEquals("", Codec.decodeHex(""));
    }

    @Test
    void testDecodeHex_3() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> Codec.decodeHex(" "));
    }

    @Test
    void testDecodeHex_4() {
        Assertions.assertEquals("Seanox Software Solutions",
                Codec.decodeHex("5365616E6F7820536F66747761726520536F6C7574696F6E73"));
    }

    @Test
    void testDecodeHex_5() {
        Assertions.assertEquals("Seanox Software Solutions",
                Codec.decodeHex("5365616e6f7820536f66747761726520536f6c7574696f6e73"));
    }

    @Test
    void testDecodeHex_6() {
        Assertions.assertEquals("Seanox Software Solutions",
                Codec.decodeHex("5365616E6F7820536F66747761726520536f6c7574696f6e73"));
    }

    @Test
    void testDecodeHex_7() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> Codec.decodeHex(" 5365616E6F7820536F66747761726520536f6c7574696f6e73"));
    }

    @Test
    void testDecodeHex_8() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> Codec.decodeHex(" 5365616E6F7820536F66747761726520536f6c7574696f6e73 "));
    }

    @Test
    void testDecodeHex_9() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> Codec.decodeHex("5365616E6F7820536F66747761726520536f6c7574696f6e73 "));
    }

    @Test
    void testDecodeBase64_1() {
        Assertions.assertNull(Codec.decodeBase64(null));
    }

    @Test
    void testDecodeBase64_2() {
        Assertions.assertEquals("", Codec.decodeBase64(""));
    }

    @Test
    void testDecodeBase64_3() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> Codec.decodeBase64(" "));
    }

    @Test
    void testDecodeBase64_4() {
        Assertions.assertEquals("Seanox Software Solutions",
                Codec.decodeBase64("U2Vhbm94IFNvZnR3YXJlIFNvbHV0aW9ucw=="));
    }

    @Test
    void testDecodeBase64_5() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> Codec.decodeBase64(" U2Vhbm94IFNvZnR3YXJlIFNvbHV0aW9ucw=="));
    }

    @Test
    void testDecodeBase64_6() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> Codec.decodeBase64(" U2Vhbm94IFNvZnR3YXJlIFNvbHV0aW9ucw== "));
    }

    @Test
    void testDecodeBase64_7() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> Codec.decodeBase64("U2Vhbm94IFNvZnR3YXJlIFNvbHV0aW9ucw== "));
    }
}
