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

import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;

/** MPV video decoder modes exposed to the app. */
@UnstableApi
public enum MpvDecoderMode {
  AUTO(C.DECODE_HARDWARE),
  HARDWARE(C.DECODE_HARDWARE),
  SOFTWARE(C.DECODE_SOFTWARE),
  FFMPEG(C.DECODE_SOFTWARE);

  private final int decodeMode;

  MpvDecoderMode(int decodeMode) {
    this.decodeMode = decodeMode;
  }

  /** Returns the corresponding {@link C.DecodeMode} value. */
  public int toDecodeMode() {
    return decodeMode;
  }

  /** Converts a {@link C.DecodeMode} value to an MPV decoder mode. */
  public static MpvDecoderMode fromDecodeMode(int decodeMode) {
    return decodeMode == C.DECODE_SOFTWARE ? SOFTWARE : HARDWARE;
  }

  /** Converts a common decoder mode to an MPV decoder mode. */
  public static MpvDecoderMode fromCommonDecoderMode(androidx.media3.common.DecoderMode mode) {
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
