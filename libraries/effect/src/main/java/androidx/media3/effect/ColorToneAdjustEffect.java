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

import androidx.media3.common.Effect;
import androidx.media3.common.util.UnstableApi;

/**
 * Color tone adjustment effect.
 *
 * <p>Phase 1 implementation: records the adjustment so the effect chain can be configured by the
 * player. Shader based rendering is added in a later phase.
 */
@UnstableApi
public final class ColorToneAdjustEffect implements Effect {

  private VideoAdjustment adjustment;

  /** Sets the adjustment applied by this effect. */
  public void setAdjustment(VideoAdjustment adjustment) {
    this.adjustment = adjustment;
  }

  /** Returns the current adjustment. */
  public VideoAdjustment getAdjustment() {
    return adjustment;
  }
}
