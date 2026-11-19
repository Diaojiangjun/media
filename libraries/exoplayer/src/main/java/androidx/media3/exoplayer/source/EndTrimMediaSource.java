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

import androidx.media3.common.C;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;

/** Wraps a media source and stops playback at the configured ending position. */
@UnstableApi
public final class EndTrimMediaSource extends WrappingMediaSource {

  private final Timeline.Window window;
  private volatile long endingMs;
  private volatile long originalDurationMs;

  public EndTrimMediaSource(MediaSource mediaSource, long endingMs) {
    super(mediaSource);
    this.endingMs = endingMs;
    this.originalDurationMs = C.TIME_UNSET;
    this.window = new Timeline.Window();
  }

  /** Returns the configured ending position in milliseconds. */
  public long getEndingMs() {
    return endingMs;
  }

  /** Sets the ending position in milliseconds. */
  public void setEndTrimDurationMs(long endingMs) {
    this.endingMs = endingMs;
  }

  /** Returns whether an ending position is configured. */
  public boolean isEndTrimActive() {
    return endingMs > 0;
  }

  /** Returns the duration of the wrapped source in milliseconds, or {@link C#TIME_UNSET}. */
  public long getOriginalDurationMs() {
    return originalDurationMs;
  }

  @Override
  protected void onChildSourceInfoRefreshed(Timeline newTimeline) {
    super.onChildSourceInfoRefreshed(newTimeline);
    long durationMs = C.TIME_UNSET;
    if (!newTimeline.isEmpty()) {
      durationMs = Util.usToMs(newTimeline.getWindow(/* windowIndex= */ 0, window).durationUs);
    }
    originalDurationMs = durationMs;
  }
}
