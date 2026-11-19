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
package androidx.media3.common.audio;

import androidx.annotation.IntDef;
import androidx.media3.common.util.UnstableApi;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Output channel layout selection for audio effects. */
@UnstableApi
public final class AudioChannelMode {

  /** Keep the original channel layout. */
  public static final int AUTO = 0;
  /** Downmix to stereo. */
  public static final int STEREO = 1;
  /** Downmix to mono. */
  public static final int MONO = 2;
  /** Swap left and right channels. */
  public static final int REVERSE = 3;

  /** Int definition for audio channel modes. */
  @Documented
  @Retention(RetentionPolicy.SOURCE)
  @Target({ElementType.TYPE_USE})
  @IntDef({AUTO, STEREO, MONO, REVERSE})
  public @interface Mode {}

  private AudioChannelMode() {}

  /** Clamps an arbitrary value to a supported channel mode. */
  public static int clamp(int mode) {
    return mode < AUTO || mode > REVERSE ? AUTO : mode;
  }

  /** Returns whether the given mode needs a channel mixing matrix for the channel count. */
  public static boolean isAvailable(int mode, int channelCount) {
    return channelCount >= 2 && channelCount <= 8 && mode >= AUTO && mode <= REVERSE;
  }

  /** Returns whether channel mixing is available for the channel count. */
  public static boolean isAvailable(int channelCount) {
    return channelCount >= 2 && channelCount <= 8;
  }

  /** Resolves the effective mode for the channel count. */
  public static int resolve(int mode, int channelCount) {
    mode = clamp(mode);
    if (!isAvailable(mode, channelCount)) {
      return AUTO;
    }
    return mode;
  }

  /** Returns whether the mode keeps the original channel layout. */
  public static boolean isAuto(int mode) {
    return clamp(mode) == AUTO;
  }

  /** Returns whether balance can be applied for the mode. */
  public static boolean canBalance(int mode) {
    mode = clamp(mode);
    return mode == AUTO || mode == STEREO || mode == REVERSE;
  }
}
