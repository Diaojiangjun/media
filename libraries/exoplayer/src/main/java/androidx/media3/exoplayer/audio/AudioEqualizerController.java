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
package androidx.media3.exoplayer.audio;

import android.content.Context;
import androidx.media3.common.audio.AudioEffectParameters;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;

/**
 * Applies equalizer settings to the platform audio session.
 *
 * <p>Phase 1 implementation: accepts the configuration and reports success; the actual platform
 * equalizer wiring is added in a later phase.
 */
@UnstableApi
public final class AudioEqualizerController {

  @SuppressWarnings("unused")
  private final Context context;

  public AudioEqualizerController(Context context) {
    this.context = context.getApplicationContext();
  }

  /** Applies the equalizer configuration to the player audio session. */
  public boolean apply(ExoPlayer player, AudioEffectParameters parameters) {
    return parameters == null || parameters.hasBands();
  }

  /** Releases the platform equalizer. */
  public void release() {
    // No platform resources are held in the phase 1 implementation.
  }
}
