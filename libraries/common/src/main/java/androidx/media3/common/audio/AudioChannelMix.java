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

import androidx.media3.common.util.UnstableApi;

/** Utility methods for building audio channel mixing matrices. */
@UnstableApi
public final class AudioChannelMix {

  private AudioChannelMix() {}

  /** Creates an identity matrix for the channel count. */
  public static float[][] createIdentity(int channelCount) {
    float[][] mix = new float[channelCount][channelCount];
    for (int channel = 0; channel < channelCount; channel++) {
      mix[channel][channel] = 1.0f;
    }
    return mix;
  }

  /** Creates a matrix that applies gain to the front center channel. */
  public static float[][] createFrontCenterGainMix(int channelCount, float gain) {
    float[][] mix = createIdentity(channelCount);
    int center = findCenterChannel(channelCount);
    if (center >= 0) {
      mix[center][center] = gain;
    }
    return mix;
  }

  /** Creates a stereo matrix, optionally swapping left and right. */
  public static float[][] createStereoMix(int channelCount, boolean reverse) {
    float[][] mix = createIdentity(channelCount);
    if (channelCount >= 2 && reverse) {
      mix[0][0] = 0.0f;
      mix[1][1] = 0.0f;
      mix[0][1] = 1.0f;
      mix[1][0] = 1.0f;
    }
    return mix;
  }

  /** Creates a matrix that mixes all channels into mono. */
  public static float[][] createMonoMix(int channelCount) {
    float[][] mix = new float[channelCount][channelCount];
    float value = 1.0f / channelCount;
    for (int output = 0; output < channelCount; output++) {
      for (int input = 0; input < channelCount; input++) {
        mix[output][input] = value;
      }
    }
    return mix;
  }

  /** Creates a matrix that applies a front balance adjustment. */
  public static float[][] createFrontBalanceMix(int channelCount, float balance) {
    float[][] mix = createIdentity(channelCount);
    if (channelCount >= 2) {
      float left = balance <= 0 ? 1.0f : 1.0f - balance;
      float right = balance >= 0 ? 1.0f : 1.0f + balance;
      mix[0][0] = left;
      mix[1][1] = right;
    }
    return mix;
  }

  /** Composes two mixing matrices. */
  public static float[][] compose(float[][] first, float[][] second) {
    int size = Math.min(first.length, second.length);
    float[][] result = new float[size][size];
    for (int row = 0; row < size; row++) {
      for (int column = 0; column < size; column++) {
        float sum = 0.0f;
        for (int index = 0; index < size; index++) {
          sum += first[row][index] * second[index][column];
        }
        result[row][column] = sum;
      }
    }
    return result;
  }

  private static int findCenterChannel(int channelCount) {
    if (channelCount == 6 || channelCount == 8) {
      return 2;
    }
    return channelCount >= 3 ? channelCount / 2 : -1;
  }
}
