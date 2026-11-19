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

/** Timing offsets applied to the primary and secondary subtitle tracks, in milliseconds. */
@UnstableApi
public final class SubtitleOffsets {

  /** No offset applied to either subtitle track. */
  public static final SubtitleOffsets ZERO = new SubtitleOffsets(0, 0);

  /** Offset of the primary subtitle track, in milliseconds. */
  public final long primaryMs;

  /** Offset of the secondary subtitle track, in milliseconds. */
  public final long secondaryMs;

  public SubtitleOffsets(long primaryMs, long secondaryMs) {
    this.primaryMs = primaryMs;
    this.secondaryMs = secondaryMs;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof SubtitleOffsets)) {
      return false;
    }
    SubtitleOffsets offsets = (SubtitleOffsets) other;
    return primaryMs == offsets.primaryMs && secondaryMs == offsets.secondaryMs;
  }

  @Override
  public int hashCode() {
    return 31 * Long.hashCode(primaryMs) + Long.hashCode(secondaryMs);
  }

  @Override
  public String toString() {
    return "SubtitleOffsets[" + primaryMs + ", " + secondaryMs + "]";
  }
}
