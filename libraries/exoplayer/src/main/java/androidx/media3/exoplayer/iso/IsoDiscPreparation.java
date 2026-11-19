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
package androidx.media3.exoplayer.iso;

import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import java.util.Locale;

/** Utilities for ISO (Blu-ray/DVD image) media items. */
@UnstableApi
public final class IsoDiscPreparation {

  private static final String ISO_MIME_TYPE = "application/x-iso9660-image";

  private IsoDiscPreparation() {}

  /** Returns whether the media item points at an ISO disc image. */
  public static boolean isIso(MediaItem mediaItem) {
    if (mediaItem == null || mediaItem.localConfiguration == null) {
      return false;
    }
    MediaItem.LocalConfiguration configuration = mediaItem.localConfiguration;
    if (ISO_MIME_TYPE.equals(configuration.mimeType)) {
      return true;
    }
    return isIsoUri(configuration.uri);
  }

  /** Returns whether the URI points at an ISO disc image. */
  public static boolean isIsoUri(@Nullable Uri uri) {
    if (uri == null) {
      return false;
    }
    String path = uri.getPath();
    if (path == null) {
      return false;
    }
    return path.toLowerCase(Locale.ROOT).endsWith(".iso");
  }

  /** Returns whether the MIME type denotes an ISO disc image. */
  public static boolean isIsoMimeType(@Nullable String mimeType) {
    return ISO_MIME_TYPE.equals(mimeType);
  }
}
