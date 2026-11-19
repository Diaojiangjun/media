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
package androidx.media3.exoplayer.libass;

import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.Renderer;
import androidx.media3.exoplayer.text.SubtitleTranscript;
import androidx.media3.exoplayer.text.SubtitleTranscriptParserFactory;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.text.SubtitleParser;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Futures;

/**
 * Shared state of the libass subtitle renderer for the current playback.
 *
 * <p>This build does not bundle the libass native runtime, so {@link #isAvailable()} returns
 * {@code false} and callers fall back to the Java subtitle pipeline.
 */
@UnstableApi
public final class LibassPlaybackSession {

  /** Extractors and subtitle parser configured for a media item. */
  public static final class MediaComponents {

    public final ExtractorsFactory extractorsFactory;

    public final SubtitleParser.Factory subtitleParserFactory;

    MediaComponents(
        ExtractorsFactory extractorsFactory, SubtitleParser.Factory subtitleParserFactory) {
      this.extractorsFactory = extractorsFactory;
      this.subtitleParserFactory = subtitleParserFactory;
    }
  }

  private final LibassConfiguration configuration;
  @Nullable private MediaItem preloadMediaItem;
  private float bottomPositionFraction = 1.0f;
  private float secondaryBottomPositionFraction = 1.0f;
  private float fontScale = 1.0f;

  public LibassPlaybackSession(LibassConfiguration configuration) {
    this.configuration = configuration;
  }

  /** Returns whether the libass native runtime is available. */
  public boolean isAvailable() {
    return false;
  }

  /** Returns the configuration of this session. */
  public LibassConfiguration getConfiguration() {
    return configuration;
  }

  /**
   * Creates the extractors and subtitle parser used for the given media item.
   *
   * @param mediaItem The media item being prepared.
   * @param extractorsFactory The default extractors factory.
   * @param transcript Transcript that collects parsed subtitle cues.
   * @return The components to use when creating the media source.
   */
  public MediaComponents createMediaComponents(
      MediaItem mediaItem, ExtractorsFactory extractorsFactory, SubtitleTranscript transcript) {
    return new MediaComponents(extractorsFactory, new SubtitleTranscriptParserFactory(transcript));
  }

  /** Sets the bottom position of the primary subtitle track as a fraction of the video height. */
  public void setBottomPositionFraction(float fraction) {
    bottomPositionFraction = fraction;
  }

  /** Sets the bottom position of the secondary subtitle track. */
  public void setSecondaryBottomPositionFraction(float fraction) {
    secondaryBottomPositionFraction = fraction;
  }

  /** Sets the font scale, optionally applying it to embedded subtitle styles. */
  public void setFontScale(float scale, boolean applyToEmbeddedStyles) {
    fontScale = scale;
  }

  /** Returns the bottom position of the primary subtitle track. */
  public float getBottomPositionFraction() {
    return bottomPositionFraction;
  }

  /** Returns the bottom position of the secondary subtitle track. */
  public float getSecondaryBottomPositionFraction() {
    return secondaryBottomPositionFraction;
  }

  /** Returns the configured font scale. */
  public float getFontScale() {
    return fontScale;
  }

  /** Sets the media item that is being preloaded, or {@code null} to clear it. */
  public void setPreloadMediaItem(@Nullable MediaItem mediaItem) {
    preloadMediaItem = mediaItem;
  }

  /** Returns the media item that is being preloaded, or {@code null}. */
  @Nullable
  public MediaItem getPreloadMediaItem() {
    return preloadMediaItem;
  }

  /**
   * Creates the renderer that advances the libass clock.
   *
   * @throws UnsupportedOperationException Always, because the native runtime is unavailable.
   */
  public Renderer createClockRenderer() {
    throw new UnsupportedOperationException("Libass native runtime is unavailable");
  }

  /** Releases session resources. */
  public void close() {
    preloadMediaItem = null;
  }

  /**
   * Applies updated playback options.
   *
   * <p>The options are stored for future use; there is no native renderer to update.
   */
  public ListenableFuture<Void> applyOptions() {
    return Futures.immediateVoidFuture();
  }
}
