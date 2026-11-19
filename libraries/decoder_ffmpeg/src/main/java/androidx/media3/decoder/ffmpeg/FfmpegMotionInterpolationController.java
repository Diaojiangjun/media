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

import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Controls FFmpeg based frame interpolation.
 *
 * <p>Phase 1 implementation: exposes the control surface and reports an unsupported state. The
 * native filter chain is added in a later phase.
 */
@UnstableApi
public final class FfmpegMotionInterpolationController {

  /** Whether the current format can be interpolated. */
  public enum FormatSupport {
    UNKNOWN,
    SUPPORTED,
    UNSUPPORTED
  }

  /** Current output switching state. */
  public enum OutputState {
    INACTIVE,
    SWITCHING,
    ACTIVE
  }

  private final Set<Runnable> listeners = new CopyOnWriteArraySet<>();
  @SuppressWarnings("unused")
  private final ExoPlayer player;
  private int targetFps;

  public FfmpegMotionInterpolationController(ExoPlayer player) {
    this.player = player;
  }

  /** Returns whether interpolation is supported for the current playback. */
  public boolean isSupported() {
    return false;
  }

  /** Returns the configured target frame rate, or 0 when disabled. */
  public int getTargetFps() {
    return targetFps;
  }

  /** Returns whether the current format can be interpolated. */
  public FormatSupport getFormatSupport() {
    return FormatSupport.UNKNOWN;
  }

  /** Returns the current output state. */
  public OutputState getOutputState() {
    return OutputState.INACTIVE;
  }

  /** Returns whether a target frame rate update is in progress. */
  public boolean isUpdatePending() {
    return false;
  }

  /** Sets the target frame rate, or 0 to disable interpolation. */
  public ListenableFuture<Void> setTargetFps(int fps) {
    targetFps = Math.max(0, fps);
    for (Runnable listener : listeners) {
      listener.run();
    }
    return Futures.immediateVoidFuture();
  }

  /** Adds a listener notified when the interpolation state changes. */
  public void addListener(Runnable listener) {
    if (listener != null) {
      listeners.add(listener);
    }
  }

  /** Removes a previously added listener. */
  public void removeListener(Runnable listener) {
    listeners.remove(listener);
  }

  /** Releases interpolation resources. */
  public void release() {
    listeners.clear();
    targetFps = 0;
  }
}
