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

import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTime {

    public static final SimpleDateFormat TIMESTAMP_RFC822_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

    public static final Pattern PATTERN_DURATION = Pattern
            .compile("^(?i)(\\d+(?:\\.\\d+){0,1})\\s*(ms|s|m|h)$");

    public static long parseDuration(final String text) {
        if (Objects.isNull(text))
            throw new DateTimeFormatException("Cannot parse null string");
        final Matcher matcher = PATTERN_DURATION.matcher(text.toLowerCase());
        if (!matcher.find())
            throw new DateTimeFormatException(String.format("For input string: %s", text));
        final double time = Double.parseDouble(matcher.group(1));
        final String unit = matcher.group(2);
        int factor = 1;
        if (unit.equals("s"))
            factor *= 1000;
        else if (unit.equals("m"))
            factor *= 1000 *60;
        else if (unit.equals("h"))
            factor *= 1000 *60 *60;
        return Math.round(time *factor);
    }

    static class DateTimeFormatException extends IllegalArgumentException {
        DateTimeFormatException(final String message){
            super(message);
        }
    }
}
