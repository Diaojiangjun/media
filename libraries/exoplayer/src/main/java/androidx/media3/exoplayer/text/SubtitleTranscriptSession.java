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

import androidx.annotation.Nullable;
import androidx.media3.common.Format;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.text.SubtitleContent;
import androidx.media3.common.util.UnstableApi;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Holds subtitle transcripts for the current and preloaded media items. */
@UnstableApi
public final class SubtitleTranscriptSession {

  private final Map<String, SubtitleTranscript> transcripts = new ConcurrentHashMap<>();
  @Nullable private SubtitleContentLoader loader;
  @Nullable private Player player;
  @Nullable private MediaItem preloadMediaItem;
  private volatile boolean contentEnabled;

  /** Sets the loader used to extract subtitle content. */
  public void setLoader(SubtitleContentLoader loader) {
    this.loader = loader;
  }

  /** Sets the player this session is attached to. */
  public void setPlayer(@Nullable Player player) {
    this.player = player;
  }

  /** Sets the media item that is currently being preloaded. */
  public void setPreloadMediaItem(@Nullable MediaItem mediaItem) {
    preloadMediaItem = mediaItem;
  }

  /** Enables or disables subtitle content extraction. */
  public void setContentEnabled(boolean enabled) {
    contentEnabled = enabled;
  }

  /** Returns the transcript associated with a media item. */
  public SubtitleTranscript forMediaItem(MediaItem mediaItem) {
    String key = mediaItem.mediaId == null ? mediaItem.toString() : mediaItem.mediaId;
    return transcripts.computeIfAbsent(key, ignored -> new SubtitleTranscript());
  }

  /** Returns extracted content for the selected subtitle track. */
  public SubtitleContent getContent(Format format, long offsetMs) {
    if (!contentEnabled) {
      return SubtitleContent.UNSUPPORTED;
    }
    SubtitleTranscript transcript = findTranscript();
    if (transcript == null) {
      return SubtitleContent.UNSUPPORTED;
    }
    SubtitleContent content = transcript.snapshot();
    if (!content.entries.isEmpty() && offsetMs != 0) {
      long offsetUs = offsetMs * 1000;
      List<SubtitleContent.Entry> shifted = new ArrayList<>(content.entries.size());
      for (SubtitleContent.Entry entry : content.entries) {
        shifted.add(
            new SubtitleContent.Entry(
                entry.startTimeUs + offsetUs,
                entry.endTimeUs + offsetUs,
                entry.text,
                entry.image));
      }
      return new SubtitleContent(content.state, shifted);
    }
    return content;
  }

  /** Returns whether content extraction can be retried for the given format. */
  public boolean canRetryContent(Format format) {
    return loader != null;
  }

  /** Retries content extraction for the given format. */
  public void retryContent(Format format) {
    SubtitleTranscript transcript = findTranscript();
    if (transcript != null) {
      transcript.clear();
    }
  }

  /** Releases all transcripts. */
  public void close() {
    transcripts.clear();
    loader = null;
    player = null;
    preloadMediaItem = null;
  }

  @Nullable
  private SubtitleTranscript findTranscript() {
    MediaItem item = preloadMediaItem;
    if (item == null && player != null) {
      item = player.getCurrentMediaItem();
    }
    if (item == null) {
      return null;
    }
    String key = item.mediaId == null ? item.toString() : item.mediaId;
    return transcripts.get(key);
  }
}
