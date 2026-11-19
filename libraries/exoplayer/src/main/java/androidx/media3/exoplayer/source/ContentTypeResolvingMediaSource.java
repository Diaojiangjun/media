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
package androidx.media3.exoplayer.source;

import androidx.annotation.NonNull;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.drm.DrmSessionManagerProvider;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;

/** Media source that resolves the container/content type before delegating extraction. */
@UnstableApi
public final class ContentTypeResolvingMediaSource extends WrappingMediaSource {

  public ContentTypeResolvingMediaSource(MediaSource mediaSource) {
    super(mediaSource);
  }

  /** Factory that wraps sources created by {@code delegate}. */
  public static final class Factory implements MediaSource.Factory {

    private final MediaSource.Factory delegate;
    private final androidx.media3.datasource.DataSource.Factory dataSourceFactory;

    public Factory(
        MediaSource.Factory delegate,
        androidx.media3.datasource.DataSource.Factory dataSourceFactory) {
      this.delegate = delegate;
      this.dataSourceFactory = dataSourceFactory;
    }

    @NonNull
    @Override
    public Factory setDrmSessionManagerProvider(
        @NonNull DrmSessionManagerProvider drmSessionManagerProvider) {
      delegate.setDrmSessionManagerProvider(drmSessionManagerProvider);
      return this;
    }

    @NonNull
    @Override
    public Factory setLoadErrorHandlingPolicy(
        @NonNull LoadErrorHandlingPolicy loadErrorHandlingPolicy) {
      delegate.setLoadErrorHandlingPolicy(loadErrorHandlingPolicy);
      return this;
    }

    @NonNull
    @Override
    public int[] getSupportedTypes() {
      return delegate.getSupportedTypes();
    }

    @NonNull
    @Override
    public MediaSource createMediaSource(@NonNull MediaItem mediaItem) {
      return new ContentTypeResolvingMediaSource(delegate.createMediaSource(mediaItem));
    }

    /** Returns the data source factory used for content type probing. */
    public androidx.media3.datasource.DataSource.Factory getDataSourceFactory() {
      return dataSourceFactory;
    }
  }
}
