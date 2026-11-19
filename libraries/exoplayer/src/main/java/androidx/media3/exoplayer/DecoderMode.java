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
package androidx.media3.exoplayer;

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;

/** ExoPlayer-specific decoder selection mode. */
@UnstableApi
public enum DecoderMode {
  AUTO,
  HARDWARE,
  SOFTWARE,
  FFMPEG;

  /** Converts a common decoder mode to an ExoPlayer decoder mode. */
  public static DecoderMode fromCommonDecoderMode(
      @Nullable androidx.media3.common.DecoderMode mode) {
    if (mode == null) {
      return AUTO;
    }
    switch (mode) {
      case HARDWARE:
        return HARDWARE;
      case SOFTWARE:
        return SOFTWARE;
      case FFMPEG:
        return FFMPEG;
      case AUTO:
      default:
        return AUTO;
    }
  }

  /** Converts this mode to the common decoder mode. */
  public androidx.media3.common.DecoderMode toCommonDecoderMode() {
    switch (this) {
      case HARDWARE:
        return androidx.media3.common.DecoderMode.HARDWARE;
      case SOFTWARE:
        return androidx.media3.common.DecoderMode.SOFTWARE;
      case FFMPEG:
        return androidx.media3.common.DecoderMode.FFMPEG;
      case AUTO:
      default:
        return androidx.media3.common.DecoderMode.AUTO;
    }
  }
}
