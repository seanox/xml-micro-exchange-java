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

import jakarta.servlet.http.HttpFilter;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Getter
@Service
class StorageService extends HttpFilter {

    @Value("${storage.uri}")
    private String serviceUri;

    @Value("${storage.directory}")
    private String directory;

    @Value("#{T(com.seanox.xmex.util.Number).parseLong(${storage.space})")
    private long space;

    @Value("#{T(com.seanox.xmex.util.Number).parseLong(${storage.quantity})")
    private int quantity;

    @Value("#{T(com.seanox.xmex.util.DateTime).parseLong(${storage.expiration})")
    private int expiration;

    static StorageShare share(final String storageIdentifier, final String xpath, final boolean exclusive)
            throws InsufficientStorageException {
        return null;
    }

    static class StorageShare {

        int getRevision() {
            return -1;
        }
    }

    static class InsufficientStorageException extends IOException {
    }
}
