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
import androidx.media3.common.C;
import androidx.media3.common.Player;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.TrackSelectionOverride;
import androidx.media3.common.Tracks;
import androidx.media3.common.text.SubtitleSelectionState;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.text.SecondaryTextOutput;
import androidx.media3.exoplayer.trackselection.SecondaryTextTrackSelector;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;

/** Tracks the selection of the primary and secondary subtitle tracks. */
@UnstableApi
public final class LibassSubtitleController {

  @SuppressWarnings("unused")
  private final Player player;

  @SuppressWarnings("unused")
  private final LibassPlaybackSession playbackSession;

  @SuppressWarnings("unused")
  private final SecondaryTextTrackSelector.Factory trackSelectorFactory;

  @SuppressWarnings("unused")
  private final SecondaryTextOutput secondaryTextOutput;

  @Nullable private TrackSelectionOverride secondarySelection;
  private boolean autoSelectionEnabled;

  public LibassSubtitleController(
      Player player,
      LibassPlaybackSession playbackSession,
      SecondaryTextTrackSelector.Factory trackSelectorFactory,
      SecondaryTextOutput secondaryTextOutput) {
    this.player = player;
    this.playbackSession = playbackSession;
    this.trackSelectorFactory = trackSelectorFactory;
    this.secondaryTextOutput = secondaryTextOutput;
  }

  /** Returns the current primary and secondary subtitle selection state. */
  public SubtitleSelectionState getSubtitleSelectionState() {
    List<TrackSelectionOverride> candidates = new ArrayList<>();
    @Nullable TrackSelectionOverride primary = null;
    ImmutableList<Tracks.Group> groups = player.getCurrentTracks().getGroups();
    for (Tracks.Group group : groups) {
      if (group.getType() != C.TRACK_TYPE_TEXT) {
        continue;
      }
      TrackGroup trackGroup = group.getMediaTrackGroup();
      for (int i = 0; i < trackGroup.length; i++) {
        TrackSelectionOverride override = new TrackSelectionOverride(trackGroup, i);
        if (group.isTrackSelected(i)) {
          if (primary == null) {
            primary = override;
          }
        } else {
          candidates.add(override);
        }
      }
    }
    @Nullable TrackSelectionOverride requested = secondarySelection;
    boolean promoted = requested != null && requested.equals(primary);
    @Nullable TrackSelectionOverride active = requested != null && !promoted ? requested : null;
    return new SubtitleSelectionState(primary, active, candidates, requested, promoted);
  }

  /** Sets the secondary text track explicitly selected by the user, or {@code null}. */
  public void setSecondaryTextTrackSelectionOverride(@Nullable TrackSelectionOverride selection) {
    secondarySelection = selection;
  }

  /** Enables or disables automatic secondary text track selection. */
  public void setSecondaryTextTrackAutoSelectionEnabled(boolean enabled) {
    autoSelectionEnabled = enabled;
    if (enabled) {
      secondarySelection = null;
    }
  }

  /** Returns whether automatic secondary text track selection is enabled. */
  public boolean isSecondaryTextTrackAutoSelectionEnabled() {
    return autoSelectionEnabled;
  }

  /** Clears the current selection state. */
  public void resetSecondaryTextTrackSelection() {
    secondarySelection = null;
    autoSelectionEnabled = false;
  }

  /** Releases controller resources. */
  public void close() {
    resetSecondaryTextTrackSelection();
    secondaryTextOutput.clear();
  }
}
