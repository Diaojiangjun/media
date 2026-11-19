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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

/** Immutable configuration of the libass subtitle renderer. */
@UnstableApi
public final class LibassConfiguration {

  /** Path of the fontconfig file, or {@code null} when libass should use its defaults. */
  @Nullable public final String fontConfig;

  /** Directory containing additional fonts, or {@code null} when none is configured. */
  @Nullable public final String fontsDirectory;

  /** Default font family, or {@code null} to use the subtitle's own font. */
  @Nullable public final String defaultFontFamily;

  /** Maximum number of pixels of a rendered subtitle bitmap. */
  public final int maximumRenderPixels;

  /** Maximum number of glyphs cached by the renderer. */
  public final int maximumGlyphCount;

  /** Maximum size of the bitmap cache in megabytes. */
  public final int maximumBitmapCacheSizeMb;

  private LibassConfiguration(Builder builder) {
    this.fontConfig = builder.fontConfig;
    this.fontsDirectory = builder.fontsDirectory;
    this.defaultFontFamily = builder.defaultFontFamily;
    this.maximumRenderPixels = builder.maximumRenderPixels;
    this.maximumGlyphCount = builder.maximumGlyphCount;
    this.maximumBitmapCacheSizeMb = builder.maximumBitmapCacheSizeMb;
  }

  /** Builder for {@link LibassConfiguration}. */
  public static final class Builder {

    @Nullable private String fontConfig;
    @Nullable private String fontsDirectory;
    @Nullable private String defaultFontFamily;
    private int maximumRenderPixels = 1920 * 1080;
    private int maximumGlyphCount = 4096;
    private int maximumBitmapCacheSizeMb = 48;
    private boolean buildCalled;

    @CanIgnoreReturnValue
    public Builder setFontConfig(@Nullable String fontConfig) {
      checkState(!buildCalled);
      this.fontConfig = fontConfig;
      return this;
    }

    @CanIgnoreReturnValue
    public Builder setFontsDirectory(@Nullable String fontsDirectory) {
      checkState(!buildCalled);
      this.fontsDirectory = fontsDirectory;
      return this;
    }

    @CanIgnoreReturnValue
    public Builder setDefaultFontFamily(@Nullable String defaultFontFamily) {
      checkState(!buildCalled);
      this.defaultFontFamily = defaultFontFamily;
      return this;
    }

    @CanIgnoreReturnValue
    public Builder setMaximumRenderPixels(int maximumRenderPixels) {
      checkState(!buildCalled);
      checkArgument(maximumRenderPixels > 0);
      this.maximumRenderPixels = maximumRenderPixels;
      return this;
    }

    @CanIgnoreReturnValue
    public Builder setMaximumGlyphCount(int maximumGlyphCount) {
      checkState(!buildCalled);
      checkArgument(maximumGlyphCount > 0);
      this.maximumGlyphCount = maximumGlyphCount;
      return this;
    }

    @CanIgnoreReturnValue
    public Builder setMaximumBitmapCacheSizeMb(int maximumBitmapCacheSizeMb) {
      checkState(!buildCalled);
      checkArgument(maximumBitmapCacheSizeMb > 0);
      this.maximumBitmapCacheSizeMb = maximumBitmapCacheSizeMb;
      return this;
    }

    public LibassConfiguration build() {
      checkState(!buildCalled);
      buildCalled = true;
      return new LibassConfiguration(this);
    }
  }
}
