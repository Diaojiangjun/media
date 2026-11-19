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
package androidx.media3.exoplayer.source;

import static com.google.common.base.Preconditions.checkNotNull;

import androidx.annotation.Nullable;
import androidx.media3.common.C;import androidx.media3.common.MediaItem;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.upstream.Allocator;

/**
 * Wraps a media source and stops playback at the configured ending offset.
 *
 * <p>The ending offset is measured backwards from the end of the media. The wrapped source is
 * clipped dynamically so the player reports STATE_ENDED at the requested position.
 */
@UnstableApi
public final class EndTrimMediaSource extends WrappingMediaSource {

  private final ClippingMediaSource clippingMediaSource;
  private final Timeline.Window window;
  private volatile long endingMs;
  private volatile long originalDurationMs;
  private volatile boolean endTrimApplied;
  private volatile boolean pendingEndTrimUpdate;

  public EndTrimMediaSource(MediaSource mediaSource, long endingMs) {
    super(createClippingSource(mediaSource));
    this.clippingMediaSource = (ClippingMediaSource) checkNotNull(getWrappedSource());
    this.window = new Timeline.Window();
    this.endingMs = endingMs;
    this.originalDurationMs = C.TIME_UNSET;
  }

  private static MediaSource createClippingSource(MediaSource mediaSource) {
    return new ClippingMediaSource.Builder(mediaSource)
        .setStartPositionUs(0)
        .setEndPositionUs(C.TIME_END_OF_SOURCE)
        .setEnableClippingInMediaPeriod(true)
        .build();
  }

  /** Returns the configured ending position in milliseconds. */
  public long getEndingMs() {
    return endingMs;
  }

  /** Sets the ending position in milliseconds. */
  public void setEndTrimDurationMs(long endingMs) {
    if (this.endingMs == endingMs) {
      return;
    }
    this.endingMs = endingMs;
    pendingEndTrimUpdate = true;
    maybeApplyEndTrim();
  }

  /**
   * Returns whether the wrapped source is actually clipped at the configured ending position.
   *
   * <p>When the source cannot be clipped the caller is expected to stop playback itself.
   */
  public boolean isEndTrimActive() {
    return endTrimApplied && endingMs > 0;
  }

  /** Returns the duration of the wrapped source in milliseconds, or {@link C#TIME_UNSET}. */
  public long getOriginalDurationMs() {
    return originalDurationMs;
  }

  @Override
  public void maybeThrowSourceInfoRefreshError() throws java.io.IOException {
    maybeApplyEndTrim();
    super.maybeThrowSourceInfoRefreshError();
  }

  @Override
  public MediaPeriod createPeriod(MediaPeriodId id, Allocator allocator, long startPositionUs) {
    maybeApplyEndTrim();
    return super.createPeriod(id, allocator, startPositionUs);
  }

  @Override
  protected void onChildSourceInfoRefreshed(Timeline newTimeline) {
    super.onChildSourceInfoRefreshed(newTimeline);
    if (originalDurationMs == C.TIME_UNSET && !newTimeline.isEmpty()) {
      long durationUs =
          newTimeline.getWindow(/* windowIndex= */ 0, window).durationUs;
      if (durationUs != C.TIME_UNSET && durationUs > 0) {
        originalDurationMs = Util.usToMs(durationUs);
        pendingEndTrimUpdate = true;
      }
    }
  }

  private void maybeApplyEndTrim() {
    if (!pendingEndTrimUpdate || originalDurationMs == C.TIME_UNSET) {
      return;
    }
    long durationMs = originalDurationMs;
    long endPositionMs = endingMs > 0 ? Math.max(0, durationMs - endingMs) : durationMs;
    pendingEndTrimUpdate = false;
    MediaItem currentItem = clippingMediaSource.getMediaItem();
    MediaItem clippedItem =
        currentItem
            .buildUpon()
            .setClippingConfiguration(
                new MediaItem.ClippingConfiguration.Builder()
                    .setEndPositionMs(endPositionMs)
                    .build())
            .build();
    if (!clippingMediaSource.canUpdateMediaItem(clippedItem)) {
      endTrimApplied = false;
      return;
    }
    clippingMediaSource.updateMediaItem(clippedItem);
    endTrimApplied = endingMs > 0 && endPositionMs < durationMs;
  }
}
