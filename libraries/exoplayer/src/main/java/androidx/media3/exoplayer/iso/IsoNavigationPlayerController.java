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
package androidx.media3.exoplayer.iso;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.media3.common.DiscMenuController;
import androidx.media3.common.DiscMenuState;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.MediaSource;

/**
 * Player-facing controller for ISO disc playback and disc menus.
 *
 * <p>When no disc navigation runtime is available, ISO sources are played as regular media and
 * menu requests report {@link OpenResult#RUNTIME_MISSING}.
 */
@UnstableApi
public final class IsoNavigationPlayerController implements DiscMenuController {

  private final ExoPlayer player;
  private boolean navigationStarted;
  private boolean menuActive;

  public IsoNavigationPlayerController(ExoPlayer player) {
    this.player = player;
  }

  /** Returns the player that should be used by the engine and UI. */
  public Player getPlayer() {
    return player;
  }

  /** Returns whether disc navigation has started. */
  public boolean isStarted() {
    return navigationStarted;
  }

  /**
   * Prepares the media source for playback.
   *
   * <p>Non-ISO sources are returned unchanged. ISO sources are returned unchanged as well because
   * disc navigation is unavailable, so playback falls back to the regular extraction pipeline.
   *
   * @param context Application context.
   * @param mediaSource The media source created for the media item.
   * @param dataSourceFactory Data source factory for disc access.
   * @return The media source to play, or {@code null} if it cannot be played.
   */
  @Nullable
  public MediaSource prepareMediaSource(
      Context context, MediaSource mediaSource, DataSource.Factory dataSourceFactory) {
    return mediaSource;
  }

  @Override
  public boolean isNavigationPlayback() {
    return navigationStarted;
  }

  @Override
  public boolean isActive() {
    return menuActive;
  }

  @Override
  public boolean supportsPointer() {
    return false;
  }

  @Override
  public boolean open(String action, OpenCallback callback) {
    if (callback != null) {
      callback.onOpenResult(OpenResult.RUNTIME_MISSING);
    }
    return true;
  }

  @Override
  public boolean sendAction(String action) {
    return false;
  }

  /** Returns the current disc menu state. */
  public DiscMenuState getDiscMenuState() {
    int availability =
        navigationStarted ? DiscMenuState.AVAILABILITY_AVAILABLE
            : DiscMenuState.AVAILABILITY_UNAVAILABLE;
    int phase = menuActive ? DiscMenuState.PHASE_ACTIVE : DiscMenuState.PHASE_IDLE;
    return new DiscMenuState(availability, phase, navigationStarted);
  }

  /** Releases disc navigation resources. */
  public void close() {
    navigationStarted = false;
    menuActive = false;
  }
}
