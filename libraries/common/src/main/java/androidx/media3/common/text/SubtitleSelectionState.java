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

import androidx.annotation.Nullable;
import androidx.media3.common.TrackSelectionOverride;
import androidx.media3.common.util.UnstableApi;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Currently selected primary and secondary text tracks. */
@UnstableApi
public final class SubtitleSelectionState {

  /** No subtitle track selected. */
  public static final SubtitleSelectionState EMPTY =
      new SubtitleSelectionState(null, null);

  /** Override describing the primary text track, or {@code null} when none is selected. */
  @Nullable public final TrackSelectionOverride primarySelection;

  /** Override describing the active secondary text track, or {@code null} when none is active. */
  @Nullable public final TrackSelectionOverride activeSecondarySelection;

  /** Overrides of the text tracks that can be used as the secondary track. */
  public final List<TrackSelectionOverride> secondaryCandidates;

  /** The secondary override explicitly requested by the user, or {@code null} for automatic. */
  @Nullable public final TrackSelectionOverride requestedSecondarySelection;

  /** Whether the requested secondary track was promoted to the primary selection. */
  public final boolean secondaryPromotedToPrimary;

  public SubtitleSelectionState(
      @Nullable TrackSelectionOverride primarySelection,
      @Nullable TrackSelectionOverride activeSecondarySelection) {
    this(primarySelection, activeSecondarySelection, Collections.emptyList(), null, false);
  }

  public SubtitleSelectionState(
      @Nullable TrackSelectionOverride primarySelection,
      @Nullable TrackSelectionOverride activeSecondarySelection,
      List<TrackSelectionOverride> secondaryCandidates,
      @Nullable TrackSelectionOverride requestedSecondarySelection,
      boolean secondaryPromotedToPrimary) {
    this.primarySelection = primarySelection;
    this.activeSecondarySelection = activeSecondarySelection;
    this.secondaryCandidates =
        Collections.unmodifiableList(new ArrayList<>(secondaryCandidates));
    this.requestedSecondarySelection = requestedSecondarySelection;
    this.secondaryPromotedToPrimary = secondaryPromotedToPrimary;
  }

  /** Returns whether {@code selection} identifies the current primary text track. */
  public boolean isPrimary(TrackSelectionOverride selection) {
    return primarySelection != null && primarySelection.equals(selection);
  }

  /** Returns whether {@code selection} identifies the active secondary text track. */
  public boolean isActiveSecondary(TrackSelectionOverride selection) {
    return activeSecondarySelection != null && activeSecondarySelection.equals(selection);
  }

  /** Returns whether a secondary text track is active. */
  public boolean hasActiveSecondary() {
    return activeSecondarySelection != null;
  }
}
