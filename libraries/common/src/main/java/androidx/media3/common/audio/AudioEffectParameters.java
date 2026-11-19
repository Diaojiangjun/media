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

/** Immutable audio effect configuration used by the equalizer and channel mixer. */
@UnstableApi
public final class AudioEffectParameters {

  /** Parameters that apply no effect. */
  public static final AudioEffectParameters DEFAULT = new Builder().build();

  private final int[] frequencies;
  private final short[] levels;
  private final float stability;
  private final int boostMillibels;
  private final int preampMillibels;
  private final int centerGainMillibels;
  private final float balance;
  private final int channelMode;
  private final boolean loudnessEnabled;

  private AudioEffectParameters(Builder builder) {
    frequencies = builder.frequencies;
    levels = builder.levels;
    stability = builder.stability;
    boostMillibels = builder.boostMillibels;
    preampMillibels = builder.preampMillibels;
    centerGainMillibels = builder.centerGainMillibels;
    balance = builder.balance;
    channelMode = builder.channelMode;
    loudnessEnabled = builder.loudnessEnabled;
  }

  /** Returns whether any effect is configured. */
  public boolean hasEffect() {
    return hasBands()
        || stability > 0
        || boostMillibels != 0
        || preampMillibels != 0
        || centerGainMillibels != 0
        || balance != 0
        || channelMode != AudioChannelMode.AUTO
        || loudnessEnabled;
  }

  /** Returns whether equalizer bands are configured. */
  public boolean hasBands() {
    return levels.length > 0;
  }

  public int[] getEqualizerFrequencies() {
    return frequencies;
  }

  public short[] getEqualizerLevelsMillibels() {
    return levels;
  }

  public float getStability() {
    return stability;
  }

  public int getBoostMillibels() {
    return boostMillibels;
  }

  public int getPreampMillibels() {
    return preampMillibels;
  }

  public int getCenterGainMillibels() {
    return centerGainMillibels;
  }

  public float getCenterGainFactor() {
    return (float) Math.pow(10, centerGainMillibels / 2000.0);
  }

  public float getBalance() {
    return balance;
  }

  public int getChannelMode() {
    return channelMode;
  }

  public boolean isLoudnessEnabled() {
    return loudnessEnabled;
  }

  /** Returns whether a center gain applies for the channel count. */
  public boolean hasCenterGain(int channelCount) {
    return centerGainMillibels != 0 && isCenterGainAvailable(channelCount);
  }

  /** Returns whether the channel layout has a dedicated center channel. */
  public static boolean isCenterGainAvailable(int channelCount) {
    return channelCount == 6 || channelCount == 8;
  }

  /** Returns whether the output should be limited to avoid clipping. */
  public boolean shouldLimitOutput(int channelCount) {
    return boostMillibels > 0 || preampMillibels > 0;
  }

  /** Builder for {@link AudioEffectParameters}. */
  public static final class Builder {

    private int[] frequencies = new int[0];
    private short[] levels = new short[0];
    private float stability;
    private int boostMillibels;
    private int preampMillibels;
    private int centerGainMillibels;
    private float balance;
    private int channelMode = AudioChannelMode.AUTO;
    private boolean loudnessEnabled;

    public Builder setEqualizerBands(int[] frequencies, short[] levels) {
      this.frequencies = frequencies == null ? new int[0] : frequencies.clone();
      this.levels = levels == null ? new short[0] : levels.clone();
      return this;
    }

    public Builder setStability(float stability) {
      this.stability = stability;
      return this;
    }

    public Builder setBoostMillibels(int boostMillibels) {
      this.boostMillibels = boostMillibels;
      return this;
    }

    public Builder setPreampMillibels(int preampMillibels) {
      this.preampMillibels = preampMillibels;
      return this;
    }

    public Builder setCenterGainMillibels(int centerGainMillibels) {
      this.centerGainMillibels = centerGainMillibels;
      return this;
    }

    public Builder setBalance(float balance) {
      this.balance = balance;
      return this;
    }

    public Builder setChannelMode(int channelMode) {
      this.channelMode = AudioChannelMode.clamp(channelMode);
      return this;
    }

    public Builder setLoudnessEnabled(boolean enabled) {
      this.loudnessEnabled = enabled;
      return this;
    }

    public AudioEffectParameters build() {
      return new AudioEffectParameters(this);
    }
  }
}
