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
package androidx.media3.common.text;

import androidx.media3.common.util.UnstableApi;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Text content extracted from a subtitle track, used by the subtitle content panel. */
@UnstableApi
public final class SubtitleContent {

  /** Loading state of the extracted subtitle content. */
  public enum State {
    LOADING,
    COMPLETE,
    PARTIAL,
    TRUNCATED,
    ERROR,
    UNSUPPORTED
  }

  /** Content is still being extracted. */
  public static final SubtitleContent LOADING = new SubtitleContent(State.LOADING, List.of());

  /** The current track cannot be converted to text. */
  public static final SubtitleContent UNSUPPORTED =
      new SubtitleContent(State.UNSUPPORTED, List.of());

  public final State state;
  public final List<Entry> entries;

  public SubtitleContent(State state, List<Entry> entries) {
    this.state = state;
    this.entries = Collections.unmodifiableList(new ArrayList<>(entries));
  }

  /** Creates a completed content instance. */
  public static SubtitleContent complete(List<Entry> entries) {
    return new SubtitleContent(State.COMPLETE, entries);
  }

  /** A single subtitle entry. */
  public static final class Entry {

    public final long startTimeUs;
    public final long endTimeUs;
    public final String text;
    public final boolean image;

    public Entry(long startTimeUs, long endTimeUs, String text, boolean image) {
      this.startTimeUs = startTimeUs;
      this.endTimeUs = endTimeUs;
      this.text = text;
      this.image = image;
    }
  }
}
