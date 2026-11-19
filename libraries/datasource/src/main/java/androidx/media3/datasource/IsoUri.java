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
package androidx.media3.datasource;

import android.net.Uri;
import androidx.media3.common.util.UnstableApi;

/** Helpers for ISO (Blu-ray image) URIs. */
@UnstableApi
public final class IsoUri {

  /** Query parameter used to select an edition inside an ISO image. */
  public static final String PARAM_EDITION_INDEX = "iso_edition_index";

  private IsoUri() {}

  /** Returns a URI carrying the requested ISO edition index. */
  public static Uri withEditionIndex(Uri uri, int editionIndex) {
    if (uri == null || editionIndex < 0) {
      return uri;
    }
    return uri
        .buildUpon()
        .appendQueryParameter(PARAM_EDITION_INDEX, Integer.toString(editionIndex))
        .build();
  }

  /** Returns the edition index carried by the URI, or -1 when absent. */
  public static int getEditionIndex(Uri uri) {
    if (uri == null) {
      return -1;
    }
    String value = uri.getQueryParameter(PARAM_EDITION_INDEX);
    if (value == null) {
      return -1;
    }
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException ignored) {
      return -1;
    }
  }
}
