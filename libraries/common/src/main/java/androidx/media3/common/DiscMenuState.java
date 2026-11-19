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
package androidx.media3.common;

import androidx.annotation.IntDef;
import androidx.media3.common.util.UnstableApi;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Snapshot of the Blu-ray/DVD disc menu state of the current playback. */
@UnstableApi
public final class DiscMenuState {

  /** No disc menu is available. */
  public static final int AVAILABILITY_UNAVAILABLE = 0;

  /** A disc menu is available for the current media. */
  public static final int AVAILABILITY_AVAILABLE = 1;

  /** Whether a disc menu can be displayed for the current media. */
  @Documented
  @Retention(RetentionPolicy.SOURCE)
  @Target({java.lang.annotation.ElementType.TYPE_USE})
  @IntDef({AVAILABILITY_UNAVAILABLE, AVAILABILITY_AVAILABLE})
  public @interface Availability {}

  /** No menu transition is in progress. */
  public static final int PHASE_IDLE = 0;

  /** A menu overlay is visible. */
  public static final int PHASE_ACTIVE = 1;

  /** A menu is opening. */
  public static final int PHASE_OPENING = 2;

  /** Menu transition phase. */
  @Documented
  @Retention(RetentionPolicy.SOURCE)
  @Target({java.lang.annotation.ElementType.TYPE_USE})
  @IntDef({PHASE_IDLE, PHASE_ACTIVE, PHASE_OPENING})
  public @interface Phase {}

  /** The state used when no disc menu information is available. */
  public static final DiscMenuState EMPTY =
      new DiscMenuState(AVAILABILITY_UNAVAILABLE, PHASE_IDLE, false);

  /** Whether a disc menu is available. */
  public final @Availability int availability;

  /** Current menu transition phase. */
  public final @Phase int phase;

  /** Whether the current playback is driven by disc navigation (not a title stream). */
  public final boolean navigationPlaybackOngoing;

  public DiscMenuState(
      @Availability int availability, @Phase int phase, boolean navigationPlaybackOngoing) {
    this.availability = availability;
    this.phase = phase;
    this.navigationPlaybackOngoing = navigationPlaybackOngoing;
  }

  /** Returns whether a disc menu can be displayed. */
  public boolean isAvailable() {
    return availability == AVAILABILITY_AVAILABLE;
  }

  /** Returns whether a menu overlay is currently shown. */
  public boolean isActive() {
    return phase == PHASE_ACTIVE;
  }
}
