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
package com.seanox.xmex.content;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Objects;

@Getter
@Service
class ContentService {

    // If the directory is empty, the working directory is used.
    // In the event of an incorrect configuration, this protects agains
    // unwanted access to the root directory.
    @Value("#{T(java.nio.file.Paths).get(('${content.directory}').isBlank() ? '.' : '${content.directory}').normalize().toFile().absoluteFile}")
    private File contentDirectory;

    @Value("#{('${content.default}').split('[,\\s]+')}")
    private String[] contentDefaults;

    @Value("#{('${content.redirect}').trim()}")
    private String contentRedirect;

    File getContentEntry(final String contentEntryPath) {
        if (Objects.isNull(contentEntryPath))
            return null;
        return new File(this.contentDirectory, contentEntryPath);
    }

    String getContentEntryPath(final String contentEntryPath) {
        if (Objects.isNull(contentEntryPath))
            return null;
        return this.getContentEntry(contentEntryPath).toString();
    }

    File getContentEntry(final HttpServletRequest request) {
        if (Objects.isNull(request))
            return null;
        return Paths.get(URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8)).normalize().toFile();
    }

    String getContentEntryPath(final HttpServletRequest request) {
        if (Objects.isNull(request))
            return null;
        return this.getContentEntry(request).toString().replace('\\', '/');
    }

    String getContentDirectoryPath() {
        return this.contentDirectory.toString();
    }

    File getContentDirectory(final String contentDirectoryPath) {
        if (Objects.isNull(contentDirectoryPath))
            return null;
        final File contentEntry = this.getContentEntry(contentDirectoryPath);
        return contentEntry.isDirectory() ? new File(this.contentDirectory, contentDirectoryPath) : null;
    }

    String getContentDirectoryPath(final String contentDirectoryPath) {
        final File contentDirectory = this.getContentDirectory(contentDirectoryPath);
        if (Objects.isNull(contentDirectory))
            return null;
        return contentDirectory.toString();
    }

    File getContentDirectoryDefault(final String contentDirectoryPath) {
        final File contentDirectory = this.getContentDirectory(contentDirectoryPath);
        if (Objects.isNull(contentDirectory))
            return null;
        for (final String contentDefault : this.contentDefaults) {
            if (!contentDefault.matches("[^/\\\\]+"))
                continue;
            final File contentDefaultFile = new File(contentDirectory, contentDefault);
            if (contentDefaultFile.exists()
                    || contentDefaultFile.isFile())
                return contentDefaultFile;
        }
        return null;
    }
}
