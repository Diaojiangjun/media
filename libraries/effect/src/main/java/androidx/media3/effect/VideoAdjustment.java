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
package androidx.media3.effect;

import androidx.media3.common.util.UnstableApi;

/** Color and detail adjustments applied by the video effect chain. */
@UnstableApi
public final class VideoAdjustment {

  public final float saturation;
  public final float contrast;
  public final float brightness;
  public final float gamma;
  public final float hue;
  public final float redGain;
  public final float blueGain;
  public final float sharpness;
  public final float threshold;
  public final float shadowLift;

  private VideoAdjustment(Builder builder) {
    saturation = builder.saturation;
    contrast = builder.contrast;
    brightness = builder.brightness;
    gamma = builder.gamma;
    hue = builder.hue;
    redGain = builder.redGain;
    blueGain = builder.blueGain;
    sharpness = builder.sharpness;
    threshold = builder.threshold;
    shadowLift = builder.shadowLift;
  }

  /** Builder for {@link VideoAdjustment}. */
  public static final class Builder {

    private float saturation = 1.0f;
    private float contrast = 1.0f;
    private float brightness;
    private float gamma = 1.0f;
    private float hue;
    private float redGain = 1.0f;
    private float blueGain = 1.0f;
    private float sharpness;
    private float threshold;
    private float shadowLift;

    public Builder setSaturation(float saturation) {
      this.saturation = saturation;
      return this;
    }

    public Builder setContrast(float contrast) {
      this.contrast = contrast;
      return this;
    }

    public Builder setBrightness(float brightness) {
      this.brightness = brightness;
      return this;
    }

    public Builder setGamma(float gamma) {
      this.gamma = gamma;
      return this;
    }

    public Builder setHue(float hue) {
      this.hue = hue;
      return this;
    }

    public Builder setRedGain(float redGain) {
      this.redGain = redGain;
      return this;
    }

    public Builder setBlueGain(float blueGain) {
      this.blueGain = blueGain;
      return this;
    }

    public Builder setSharpness(float sharpness) {
      this.sharpness = sharpness;
      return this;
    }

    public Builder setThreshold(float threshold) {
      this.threshold = threshold;
      return this;
    }

    public Builder setShadowLift(float shadowLift) {
      this.shadowLift = shadowLift;
      return this;
    }

    public VideoAdjustment build() {
      return new VideoAdjustment(this);
    }
  }
}
