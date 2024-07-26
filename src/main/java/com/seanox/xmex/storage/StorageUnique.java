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
package com.seanox.xmex.storage;

import java.util.stream.IntStream;

class StorageUnique implements CharSequence {

    private static final int RADIX = 36;

    private static final int TICKS_MAX = 10000;

    private static final long TIME_20TH_CENTURY_REDUCTION = 946684800000L;

    private static volatile long timestamp;

    private static volatile long ticks;

    private final String unique;

    StorageUnique() {
        synchronized (StorageUnique.class) {
            final long timestamp = Math.max(StorageUnique.timestamp, System.currentTimeMillis());
            if (StorageUnique.timestamp == timestamp) {
                if (++StorageUnique.ticks >= TICKS_MAX) {
                    StorageUnique.timestamp++;
                    StorageUnique.ticks = 0;
                }
            } else {
                StorageUnique.timestamp = timestamp;
                StorageUnique.ticks = 0;
            }
            this.unique = Long.toString(((StorageUnique.timestamp - TIME_20TH_CENTURY_REDUCTION)
                    * TICKS_MAX) + StorageUnique.ticks, RADIX).toUpperCase();
        }
    }

    @Override
    public int length() {
        return this.unique.length();
    }

    @Override
    public char charAt(final int index) {
        return this.unique.charAt(index);
    }

    @Override
    public CharSequence subSequence(final int start, final int end) {
        return this.unique.subSequence(start, end);
    }

    @Override
    public IntStream chars() {
        return this.unique.chars();
    }

    @Override
    public IntStream codePoints() {
        return this.unique.codePoints();
    }

    @Override
    public String toString() {
        return this.unique;
    }
}
