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
package androidx.media3.common.util;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.Player;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.TrackSelectionOverride;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.common.Tracks;
import java.util.ArrayList;
import java.util.List;

/** Helpers for selecting and cycling tracks through {@link Player} parameters. */
@UnstableApi
public final class TrackSelectionUtil {

  private TrackSelectionUtil() {}

  /** Cycles through all tracks of the given type. */
  public static boolean cycleTrack(
      Player player, @C.TrackType int trackType, boolean forward, boolean includeDisabled) {
    List<Selection> selections = collect(player, trackType, null);
    return cycle(player, trackType, selections, forward);
  }

  /** Cycles through the given track ids of the given type. */
  public static boolean cycleTrack(
      Player player,
      @C.TrackType int trackType,
      List<String> trackIds,
      boolean forward,
      boolean includeDisabled) {
    List<Selection> selections = collect(player, trackType, trackIds);
    return cycle(player, trackType, selections, forward);
  }

  /** Selects a track by its one-based ordinal. */
  public static boolean selectTrack(Player player, @C.TrackType int trackType, String trackId) {
    int ordinal;
    try {
      ordinal = Integer.parseInt(trackId.trim());
    } catch (NumberFormatException ignored) {
      return false;
    }
    List<Selection> selections = collect(player, trackType, null);
    if (ordinal < 1 || ordinal > selections.size()) {
      return false;
    }
    Selection selection = selections.get(ordinal - 1);
    return apply(player, trackType, selection.override);
  }

  /** Selects a track by its zero-based index. */
  public static boolean selectTrack(Player player, @C.TrackType int trackType, int index) {
    List<Selection> selections = collect(player, trackType, null);
    if (index < 0 || index >= selections.size()) {
      return false;
    }
    return apply(player, trackType, selections.get(index).override);
  }

  /** Enables or disables a track type. */
  public static boolean setTrackTypeDisabled(
      Player player, @C.TrackType int trackType, boolean disabled) {
    TrackSelectionParameters parameters =
        player.getTrackSelectionParameters().buildUpon().setTrackTypeDisabled(trackType, disabled).build();
    player.setTrackSelectionParameters(parameters);
    return true;
  }

  /** Clears overrides for a track type and re-enables it. */
  public static boolean resetTrackSelection(Player player, @C.TrackType int trackType) {
    TrackSelectionParameters parameters =
        player.getTrackSelectionParameters().buildUpon().clearOverridesOfType(trackType).build();
    player.setTrackSelectionParameters(parameters);
    return true;
  }

  private static boolean cycle(
      Player player, int trackType, List<Selection> selections, boolean forward) {
    if (selections.isEmpty()) {
      return false;
    }
    int selected = -1;
    for (int index = 0; index < selections.size(); index++) {
      if (selections.get(index).selected) {
        selected = index;
        break;
      }
    }
    int next;
    if (selected < 0) {
      next = forward ? 0 : selections.size() - 1;
    } else {
      next = (selected + (forward ? 1 : -1) + selections.size()) % selections.size();
    }
    return apply(player, trackType, selections.get(next).override);
  }

  private static boolean apply(
      Player player, int trackType, TrackSelectionOverride override) {
    TrackSelectionParameters parameters =
        player.getTrackSelectionParameters().buildUpon().setOverrideForType(override).build();
    player.setTrackSelectionParameters(parameters);
    return true;
  }

  private static List<Selection> collect(
      Player player, int trackType, @Nullable List<String> trackIds) {
    List<Selection> selections = new ArrayList<>();
    for (Tracks.Group group : player.getCurrentTracks().getGroups()) {
      if (group.getType() != trackType) {
        continue;
      }
      TrackGroup trackGroup = group.getMediaTrackGroup();
      for (int index = 0; index < group.length; index++) {
        if (trackIds != null && !trackIds.contains(Integer.toString(selections.size() + 1))) {
          continue;
        }
        TrackSelectionOverride override = new TrackSelectionOverride(trackGroup, index);
        selections.add(new Selection(override, group.isTrackSelected(index)));
      }
    }
    return selections;
  }

  private static final class Selection {
    final TrackSelectionOverride override;
    final boolean selected;

    Selection(TrackSelectionOverride override, boolean selected) {
      this.override = override;
      this.selected = selected;
    }
  }
}
