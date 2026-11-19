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
import androidx.media3.common.text.CueGroup;
import androidx.media3.common.util.UnstableApi;

/** Secondary text output used to render a second subtitle track. */
@UnstableApi
public final class SecondaryTextOutput implements TextOutput {

  @Nullable private volatile CueGroup cueGroup;

  @Override
  public void onCues(CueGroup cueGroup) {
    this.cueGroup = cueGroup;
  }

  /** Returns the most recent cue group, or {@code null} when no cues were emitted. */
  @Nullable
  public CueGroup getCueGroup() {
    return cueGroup;
  }

  /** Clears the current cues. */
  public void clear() {
    cueGroup = null;
  }
}
