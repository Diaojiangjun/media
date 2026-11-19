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
package androidx.media3.exoplayer.text;

import androidx.media3.common.text.SubtitleContent;
import androidx.media3.common.util.UnstableApi;
import java.util.ArrayList;
import java.util.List;

/** Accumulates subtitle entries extracted for a single media item. */
@UnstableApi
public final class SubtitleTranscript {

  private final List<SubtitleContent.Entry> entries = new ArrayList<>();
  private SubtitleContent.State state = SubtitleContent.State.LOADING;

  /** Adds an extracted entry. */
  public synchronized void add(SubtitleContent.Entry entry) {
    entries.add(entry);
    state = SubtitleContent.State.PARTIAL;
  }

  /** Replaces the current entries with a complete result. */
  public synchronized void complete(List<SubtitleContent.Entry> result) {
    entries.clear();
    entries.addAll(result);
    state = SubtitleContent.State.COMPLETE;
  }

  /** Returns a snapshot of the current content. */
  public synchronized SubtitleContent snapshot() {
    return new SubtitleContent(state, new ArrayList<>(entries));
  }

  /** Clears all accumulated entries. */
  public synchronized void clear() {
    entries.clear();
    state = SubtitleContent.State.LOADING;
  }
}
