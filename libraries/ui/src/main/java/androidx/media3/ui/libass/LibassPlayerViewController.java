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
package androidx.media3.ui.libass;

import androidx.annotation.Nullable;
import androidx.media3.common.Player;
import androidx.media3.common.util.Consumer;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.ui.CaptionStyleCompat;
import androidx.media3.ui.PlayerView;
import androidx.media3.ui.SubtitleView;

/** Binds the libass subtitle pipeline to the player views. */
@UnstableApi
public final class LibassPlayerViewController {

  @SuppressWarnings("unused")
  private final Player player;

  @SuppressWarnings("unused")
  private final Object playbackSession;

  @SuppressWarnings("unused")
  private final Object subtitleController;

  @Nullable private CaptionStyleCompat styleOverride;
  @Nullable private String fontFamily;
  @Nullable private Consumer<SubtitleView> secondaryViewConfigurator;
  @Nullable private SubtitleView secondarySubtitleView;
  @Nullable private PlayerView playerView;

  public LibassPlayerViewController(
      Player player, Object playbackSession, Object subtitleController) {
    this.player = player;
    this.playbackSession = playbackSession;
    this.subtitleController = subtitleController;
  }

  /** Binds this controller to the player view that renders the primary subtitles. */
  public void bind(PlayerView playerView) {
    this.playerView = playerView;
    SubtitleView subtitleView = playerView.getSubtitleView();
    if (subtitleView != null) {
      setSecondarySubtitleView(subtitleView);
    }
  }

  /** Releases the binding to the current player view. */
  public void close() {
    playerView = null;
    secondarySubtitleView = null;
    secondaryViewConfigurator = null;
  }

  /** Sets the style applied to rendered subtitles, or {@code null} to keep embedded styles. */
  public void setStyleOverride(
      @Nullable CaptionStyleCompat styleOverride, @Nullable String fontFamily) {
    this.styleOverride = styleOverride;
    this.fontFamily = fontFamily;
    applyStyle();
  }

  /** Sets a configurator applied to the secondary subtitle view. */
  public void setSecondarySubtitleViewConfigurator(
      @Nullable Consumer<SubtitleView> configurator) {
    this.secondaryViewConfigurator = configurator;
    applySecondaryConfigurator();
  }

  /** Sets the view that renders the secondary subtitle track, or {@code null} to detach it. */
  public void setSecondarySubtitleView(@Nullable SubtitleView subtitleView) {
    this.secondarySubtitleView = subtitleView;
    applyStyle();
    applySecondaryConfigurator();
  }

  /** Returns the style applied to rendered subtitles, or {@code null}. */
  @Nullable
  public CaptionStyleCompat getStyleOverride() {
    return styleOverride;
  }

  /** Returns the configured font family, or {@code null}. */
  @Nullable
  public String getFontFamily() {
    return fontFamily;
  }

  private void applyStyle() {
    SubtitleView view = secondarySubtitleView;
    if (view == null) {
      return;
    }
    if (styleOverride != null) {
      view.setStyle(styleOverride);
    }
    if (fontFamily != null) {
      view.setApplyEmbeddedStyles(false);
    }
  }

  private void applySecondaryConfigurator() {
    SubtitleView view = secondarySubtitleView;
    Consumer<SubtitleView> configurator = secondaryViewConfigurator;
    if (view != null && configurator != null) {
      configurator.accept(view);
    }
  }
}
