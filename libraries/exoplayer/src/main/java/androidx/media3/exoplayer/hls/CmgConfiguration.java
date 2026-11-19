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
package androidx.media3.exoplayer.hls;

import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;

/**
 * Configuration for CMG (China Mobile) HLS streams.
 *
 * <p>Phase 1 implementation: URI detection is a no-op, so CMG streams fall back to the regular
 * HLS pipeline until the protocol handler is added.
 */
@UnstableApi
public final class CmgConfiguration {

  /** Returns a configuration when the URI uses the CMG scheme, otherwise {@code null}. */
  @Nullable
  public static CmgConfiguration fromMediaUri(@Nullable Uri uri) {
    if (uri == null || uri.getScheme() == null) {
      return null;
    }
    return "cmg".equalsIgnoreCase(uri.getScheme()) ? new CmgConfiguration(uri) : null;
  }

  private final Uri uri;

  private CmgConfiguration(Uri uri) {
    this.uri = uri;
  }

  /** Returns the original media URI. */
  public Uri getUri() {
    return uri;
  }
}
