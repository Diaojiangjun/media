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
package androidx.media3.common.util;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Parses ClearKey license strings of the form {@code kid:key[,kid:key]}. */
@UnstableApi
public final class LocalClearKeyLicense {

  private LocalClearKeyLicense() {}

  /** Parses a ClearKey license string into a key id to key mapping. */
  public static Map<String, String> parse(String license) {
    if (license == null || license.trim().isEmpty()) {
      return Collections.emptyMap();
    }
    Map<String, String> keys = new LinkedHashMap<>();
    String[] entries = license.split("[,;\\s]+");
    for (String entry : entries) {
      if (entry.isEmpty()) {
        continue;
      }
      int separator = entry.indexOf(':');
      if (separator <= 0 || separator == entry.length() - 1) {
        continue;
      }
      String keyId = entry.substring(0, separator).trim();
      String key = entry.substring(separator + 1).trim();
      if (!keyId.isEmpty() && !key.isEmpty()) {
        keys.put(keyId, key);
      }
    }
    return keys;
  }

  /** Returns whether the string is a valid ClearKey license. */
  public static boolean isValid(String license) {
    return !parse(license).isEmpty();
  }

  /** Parses the ClearKey keys carried by a media item DRM configuration. */
  public static Map<String, String> parse(
      @Nullable MediaItem.DrmConfiguration configuration) {
    if (configuration == null
        || !C.CLEARKEY_UUID.equals(configuration.scheme)
        || configuration.licenseUri == null) {
      return Collections.emptyMap();
    }
    return parse(configuration.licenseUri.toString());
  }

  /** Number of bytes in a ClearKey key. */
  public static int keySizeBytes() {
    return 16;
  }
}
