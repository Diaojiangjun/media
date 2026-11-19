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

/** Scale and pan applied to the video surface. */
@UnstableApi
public final class VideoTransform {

  /** No scale or pan. */
  public static final VideoTransform DEFAULT = new VideoTransform(1.0f, 1.0f, 0.0f, 0.0f);

  /** Maximum supported scale factor. */
  public static final float MAX_SCALE = 4.0f;

  public final float scaleX;
  public final float scaleY;
  public final float panX;
  public final float panY;

  public VideoTransform(float scaleX, float scaleY, float panX, float panY) {
    this.scaleX = scaleX;
    this.scaleY = scaleY;
    this.panX = panX;
    this.panY = panY;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof VideoTransform)) {
      return false;
    }
    VideoTransform transform = (VideoTransform) other;
    return Float.compare(scaleX, transform.scaleX) == 0
        && Float.compare(scaleY, transform.scaleY) == 0
        && Float.compare(panX, transform.panX) == 0
        && Float.compare(panY, transform.panY) == 0;
  }

  @Override
  public int hashCode() {
    int result = Float.hashCode(scaleX);
    result = 31 * result + Float.hashCode(scaleY);
    result = 31 * result + Float.hashCode(panX);
    result = 31 * result + Float.hashCode(panY);
    return result;
  }
}
