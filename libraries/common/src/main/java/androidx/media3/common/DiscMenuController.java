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

import androidx.media3.common.util.UnstableApi;

/** Controls Blu-ray/DVD disc menu interaction for the current playback. */
@UnstableApi
public interface DiscMenuController {

  /** Result of a menu open request. */
  enum OpenResult {
    /** The menu was opened. */
    OPENED,
    /** No menu is available for the current playback. */
    UNAVAILABLE,
    /** The menu runtime required to display the menu is not installed. */
    RUNTIME_MISSING,
    /** The menu did not open in time. */
    TIMED_OUT
  }

  /** Receives the result of an {@link #open(String, OpenCallback)} request. */
  interface OpenCallback {

    void onOpenResult(OpenResult result);
  }

  /** Returns whether playback is currently driven by disc navigation. */
  boolean isNavigationPlayback();

  /** Returns whether a menu overlay is currently active. */
  boolean isActive();

  /** Returns whether the menu supports pointer input. */
  boolean supportsPointer();

  /**
   * Requests opening the given menu.
   *
   * @param action Menu action such as {@code menu}, {@code title-menu} or {@code popup}.
   * @param callback Callback invoked with the result.
   * @return Whether the request was accepted.
   */
  boolean open(String action, OpenCallback callback);

  /**
   * Sends a navigation action to the active menu.
   *
   * @param action Navigation action such as {@code prev}, {@code next} or {@code enter}.
   * @return Whether the action was accepted.
   */
  boolean sendAction(String action);
}
