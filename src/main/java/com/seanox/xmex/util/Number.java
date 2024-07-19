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

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Number {

    private static final Pattern PATTERN_NUMBER = Pattern
            .compile("(?i)^((?:-){0,1}\\d+(?:\\.\\d+){0,1})(?:\\s*([kmgtpe])(i){0,1}){0,1}$");

    public static long parseLong(final String text) {
        if (Objects.isNull(text))
            throw new NumberFormatException("Cannot parse null string");
        final Matcher matcher = PATTERN_NUMBER.matcher(text);
        if (!matcher.find())
            throw new NumberFormatException(String.format("For input string: %s", text));
        final int base = Objects.nonNull(matcher.group(3))
                && matcher.group(3).equalsIgnoreCase("i") ? 1024 : 1000;
        final int exponent = Objects.nonNull(matcher.group(2)) ? ("kmgtpe").indexOf(matcher.group(2).toLowerCase()) +1 : 0;
        final double number = Double.valueOf(matcher.group(1));
        return (long)(number *Math.pow(base, exponent));
    }
}
