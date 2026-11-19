/*
 * Copyright (C) 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package androidx.media3.mpvplayer;

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

/** Reads effective option names from an {@code mpv.conf} file. */
@UnstableApi
public final class MpvConfigFileInspector {

  private final File directory;

  public MpvConfigFileInspector(File directory) {
    this.directory = directory;
  }

  /** Returns the subtitle font directory configured in {@code mpv.conf}, if any. */
  @Nullable
  public File readSubtitleFontsDirectory() {
    File config = new File(directory, "mpv.conf");
    if (!config.isFile()) {
      return null;
    }
    try {
      String content = new String(java.nio.file.Files.readAllBytes(config.toPath()));
      for (String line : content.split("\\R")) {
        String option = line.trim();
        if (!option.startsWith("sub-fonts-dir")) {
          continue;
        }
        int separator = option.indexOf('=');
        if (separator < 0) {
          continue;
        }
        String value = option.substring(separator + 1).trim();
        if (value.isEmpty()) {
          continue;
        }
        File file = new File(value);
        return file.isAbsolute() ? file : new File(directory, value);
      }
    } catch (java.io.IOException ignored) {
      // Ignore unreadable configuration.
    }
    return null;
  }

  /** Returns the effective option names from the given configuration content. */
  public Set<String> getEffectiveOptionNames(CharSequence content) {
    Set<String> names = new LinkedHashSet<>();
    if (content == null) {
      return names;
    }
    for (String line : content.toString().split("\\R")) {
      String option = line.trim();
      if (option.isEmpty() || option.startsWith("#") || option.startsWith(";")) {
        continue;
      }
      int separator = option.indexOf('=');
      String name = separator < 0 ? option : option.substring(0, separator).trim();
      if (!name.isEmpty()) {
        names.add(name);
      }
    }
    return names;
  }
}
