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
package androidx.media3.decoder.ffmpeg;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.DefaultRenderersFactory;
import androidx.media3.exoplayer.DecoderManager;

/**
 * Renderers factory that enables FFmpeg based frame interpolation.
 *
 * <p>Phase 1 implementation: exposes the configuration surface; the interpolation renderer is added
 * in a later phase.
 */
@UnstableApi
public class FfmpegMotionRenderersFactory extends DefaultRenderersFactory {

  private final int targetFps;
  private final int maxFps;
  private final boolean enabled;
  @Nullable private DecoderManager decoderManager;
  private boolean audioPassthroughEnabled;

  public FfmpegMotionRenderersFactory(
      Context context, int targetFps, int maxFps, boolean enabled) {
    super(context);
    this.targetFps = targetFps;
    this.maxFps = maxFps;
    this.enabled = enabled;
  }

  /** Sets the decoder manager used by the renderers. */
  public FfmpegMotionRenderersFactory setDecoderManager(DecoderManager decoderManager) {
    this.decoderManager = decoderManager;
    return this;
  }

  /** Enables or disables audio passthrough. */
  public FfmpegMotionRenderersFactory setAudioPassthroughEnabled(boolean enabled) {
    this.audioPassthroughEnabled = enabled;
    return this;
  }

  /** Enables or disables runtime updates of the interpolation configuration. */
  public FfmpegMotionRenderersFactory setEnableRuntimeUpdates(boolean enabled) {
    return this;
  }

  /** Enables or disables surface control frame retention. */
  public FfmpegMotionRenderersFactory setEnableSurfaceControlFrameRetention(boolean enabled) {
    return this;
  }

  /** Returns the configured target frame rate. */
  public int getTargetFps() {
    return targetFps;
  }

  /** Returns the configured maximum frame rate. */
  public int getMaxFps() {
    return maxFps;
  }

  /** Returns whether interpolation is enabled. */
  public boolean isEnabled() {
    return enabled;
  }
}
