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
package androidx.media3.exoplayer.trackselection;

import android.content.Context;
import androidx.media3.common.util.UnstableApi;

/** Track selector that also manages an optional secondary text track. */
@UnstableApi
public final class SecondaryTextTrackSelector extends DefaultTrackSelector {

  public SecondaryTextTrackSelector(Context context) {
    super(context);
  }

  /** Factory that creates secondary text aware selectors. */
  public static final class Factory implements TrackSelector.Factory {

    private final TrackSelector.Factory delegate;

    public Factory(TrackSelector.Factory delegate) {
      this.delegate = delegate;
    }

    @Override
    public TrackSelector createTrackSelector(Context context) {
      return delegate.createTrackSelector(context);
    }
  }
}
