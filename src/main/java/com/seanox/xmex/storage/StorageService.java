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

import com.seanox.xmex.util.Codec;
import jakarta.servlet.http.HttpFilter;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
class StorageService extends HttpFilter {

    /**
     * Pattern for the Storage-Identifier
     *     Group 0. Full match
     *     Group 1. Storage
     *     Group 2. Name of the root element (optional)
     */
    private static final Pattern PATTERN_STORAGE_IDENTIFIER = Pattern
            .compile("^(\\w{1,64})(?:\\s+(\\w+)){0,1}$");

    @Value("${storage.directory}")
    private String directory;

    @Value("#{T(com.seanox.xmex.util.Number).parseLong('${storage.space}')}")
    private long space;

    @Value("#{T(com.seanox.xmex.util.Number).parseLong('${storage.quantity}')}")
    private int quantity;

    @Value("#{T(com.seanox.xmex.util.DateTime).parseDuration('${storage.expiration}')}")
    private int expiration;

    StorageMeta touch(final String storageIdentifier)
            throws IOException {
        return this.touch(storageIdentifier, false);
    }

    StorageMeta touch(final String storageIdentifier, final boolean exclusive)
            throws IOException {

        if (Objects.isNull(storageIdentifier))
            throw new StorageIdentifierException("Missing storage identifier");
        if (!PATTERN_STORAGE_IDENTIFIER.matcher(storageIdentifier).matches())
            throw new StorageIdentifierException("Invalid storage identifier");

        final File storageDirectory = new File(this.directory);
        if (storageDirectory.exists()
                && !storageDirectory.isDirectory())
            throw new IOException("Storage Data directory cannot be created due to a conflict");

        if (!storageDirectory.exists()) {
            synchronized (StorageService.class) {
                if (!storageDirectory.exists())
                    Files.createDirectories(storageDirectory.toPath());
            }
        }

        final StorageMeta storageMeta = new StorageMeta();
        final String storageName = storageIdentifier.replaceAll(PATTERN_STORAGE_IDENTIFIER.pattern(), "$1");
        storageMeta.storage = Codec.encodeBase64(storageName, StandardCharsets.UTF_8);
        final String storageRoot = storageIdentifier.replaceAll(PATTERN_STORAGE_IDENTIFIER.pattern(), "$2");
        storageMeta.root = !storageRoot.isBlank() ? storageRoot : "data";

        storageMeta.file = new File(this.directory, storageMeta.storage);
        if (!storageMeta.file.exists()) {
            synchronized (StorageService.class) {
                if (!storageMeta.file.exists()) {
                    storageMeta.unique = new StorageUnique().toString();
                    final String storageContent = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                            + "<%s ___rev=\"%s\">", storageMeta.root, storageMeta.unique);
                    storageMeta.stream = new RandomAccessFile(storageMeta.file, !exclusive ? "r" : "rw");
                    storageMeta.channel = storageMeta.stream.getChannel();
                    storageMeta.lock = storageMeta.channel.lock(0L, Long.MAX_VALUE, !exclusive);
                }
            }
        }

        if (Objects.isNull(storageMeta.lock)) {
            storageMeta.stream = new RandomAccessFile(storageMeta.file, !exclusive ? "r" : "rw");
            storageMeta.channel = storageMeta.stream.getChannel();
            storageMeta.lock = storageMeta.channel.lock(0L, Long.MAX_VALUE, !exclusive);
            storageMeta.unique = new StorageUnique().toString();
        }

        return storageMeta;
    }

    static class StorageMeta implements AutoCloseable {

        @Getter(AccessLevel.PACKAGE)
        private String unique;
        @Getter(AccessLevel.PACKAGE)
        private String revision;

        private String storage;
        private String root;
        private File file;
        private RandomAccessFile stream;
        private FileChannel channel;
        private FileLock lock;

        @Override
        public void close() throws IOException {
            this.lock.close();
            this.channel.close();
            this.stream.close();
        }
    }

    static class StorageInsufficientException extends IOException {
    }

    static class StorageIdentifierException extends IllegalArgumentException {

        StorageIdentifierException(final String message) {
            super(message);
        }
    }
}
