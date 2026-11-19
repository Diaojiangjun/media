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
import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.trackselection.DecodeTrackSelector;
import java.util.ArrayList;
import java.util.List;

/**
 * Tracks the preferred decoder mode for audio and video and exposes it to renderers and track
 * selectors.
 *
 * <p>Phase 1 implementation: modes are stored and propagated to registered selectors. Renderer
 * level decoder enforcement is implemented separately by the FFmpeg renderers factory.
 */
@UnstableApi
public final class DecoderManager {

  private final List<DecodeTrackSelector> trackSelectors;
  private DecoderMode audioMode;
  private DecoderMode videoMode;
  @Nullable private ExoPlayer player;

  public DecoderManager(DecoderMode audioMode, DecoderMode videoMode) {
    this.audioMode = audioMode;
    this.videoMode = videoMode;
    this.trackSelectors = new ArrayList<>();
  }

  /** Attaches the manager to a player instance. */
  public void attach(ExoPlayer player) {
    this.player = player;
  }

  /** Detaches the manager from the current player instance. */
  public void detach() {
    player = null;
    trackSelectors.clear();
  }

  public DecoderMode getAudioMode() {
    return audioMode;
  }

  public DecoderMode getVideoMode() {
    return videoMode;
  }

  /** Selects the audio decoder mode. */
  public void selectAudioDecoder(DecoderMode mode) {
    if (audioMode == mode) {
      return;
    }
    audioMode = mode;
    invalidateSelectors();
  }

  /** Selects the video decoder mode. */
  public void selectVideoDecoder(DecoderMode mode) {
    if (videoMode == mode) {
      return;
    }
    videoMode = mode;
    invalidateSelectors();
  }

  /** Registers a track selector that should be invalidated when decoder modes change. */
  public void registerTrackSelector(DecodeTrackSelector selector) {
    if (!trackSelectors.contains(selector)) {
      trackSelectors.add(selector);
      applyDecodePreferences(selector);
    }
  }

  private void invalidateSelectors() {
    for (DecodeTrackSelector selector : trackSelectors) {
      applyDecodePreferences(selector);
    }
  }

  private void applyDecodePreferences(DecodeTrackSelector selector) {
    selector.setRendererDecodePreferences(toDecodeMode(audioMode), toDecodeMode(videoMode));
  }

  private static int toDecodeMode(DecoderMode mode) {
    return mode == DecoderMode.SOFTWARE || mode == DecoderMode.FFMPEG
        ? C.DECODE_SOFTWARE
        : C.DECODE_HARDWARE;
  }
}
