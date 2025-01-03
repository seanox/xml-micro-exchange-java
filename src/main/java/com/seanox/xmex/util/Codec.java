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

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Pattern;

public class Codec {

    public static final Pattern PATTERN_BASE64 = Pattern
            .compile("^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$");

    public static final Pattern PATTERN_HEX = Pattern
            .compile("^([A-Fa-f0-9]{2})+$");

    public static String decodeHex(final String input) {
        return Codec.decodeHex(input, Charset.defaultCharset());
    }

    public static String decodeHex(final String input, final Charset charset) {
        if (Objects.isNull(input)
                || input.isEmpty())
            return input;
        if (!PATTERN_HEX.matcher(input).matches())
            throw new IllegalArgumentException("Invalid input, hexadecimal encoding is expected");
        final char[] inputBuffer = input.toCharArray();
        final byte[] outputBuffer = new byte[inputBuffer.length /2];
        for (int inputCursor = 0, outputCursor = 0;
            inputCursor < inputBuffer.length; inputCursor++) {
            final int digit = (Character.digit(inputBuffer[inputCursor], 16) << 4)
                    | Character.digit(inputBuffer[++inputCursor], 16);
            outputBuffer[outputCursor++] = (byte)(digit & 255);
        }
        return new String(outputBuffer, charset);
    }

    public static String decodeBase64(final String input) {
        return Codec.decodeBase64(input, Charset.defaultCharset());
    }

    public static String decodeBase64(final String input, final Charset charset) {
        if (Objects.isNull(input)
                || input.isEmpty())
            return input;
        if (!PATTERN_BASE64.matcher(input).matches())
            throw new IllegalArgumentException("Invalid input, Base64 encoding is expected");
        return new String(Base64.getDecoder().decode(input), charset);
    }

    public static String encodeBase64(final String input) {
        return Codec.encodeBase64(input, Charset.defaultCharset());
    }
    public static String encodeBase64(final String input, final Charset charset) {
        if (Objects.isNull(input)
                || input.isEmpty()
                || PATTERN_BASE64.matcher(input).matches())
            return input;
        return Base64.getEncoder().encodeToString(input.getBytes(charset));
    }
}
