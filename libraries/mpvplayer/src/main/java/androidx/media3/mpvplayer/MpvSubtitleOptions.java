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
package androidx.media3.mpvplayer;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

/** Subtitle presentation options understood by {@link MpvPlayer}. */
public final class MpvSubtitleOptions {

  public static final int SECONDARY_TRACK_OFF = -2;
  public static final int SECONDARY_TRACK_AUTO = -1;

  private static final int STYLE_ORIGINAL = 0;
  private static final int STYLE_SYSTEM = 1;
  private static final int STYLE_CUSTOM = 2;

  private final double position;
  private final boolean positionSet;
  private final double scale;
  private final boolean scaleSet;
  private final int style;
  private final int foregroundColor;
  private final int backgroundColor;
  private final int edgeType;
  private final int edgeColor;
  private final double edgeSize;
  private final double shadowOffset;
  private final boolean secondarySubtitleConfigured;
  private final int secondaryTrack;
  private final double secondaryPosition;
  private final boolean secondaryOverrideAssStyles;
  private final String fontsDirectory;
  private final String fontFamily;
  private final double verticalOffset;
  private final boolean verticalOffsetSet;
  private final double secondaryVerticalOffset;
  private final double bitmapBrightness;

  private MpvSubtitleOptions(Builder builder) {
    position = builder.position;
    positionSet = builder.positionSet;
    scale = builder.scale;
    scaleSet = builder.scaleSet;
    style = builder.style;
    foregroundColor = builder.foregroundColor;
    backgroundColor = builder.backgroundColor;
    edgeType = builder.edgeType;
    edgeColor = builder.edgeColor;
    edgeSize = builder.edgeSize;
    shadowOffset = builder.shadowOffset;
    secondarySubtitleConfigured = builder.secondarySubtitleConfigured;
    secondaryTrack = builder.secondaryTrack;
    secondaryPosition = builder.secondaryPosition;
    secondaryOverrideAssStyles = builder.secondaryOverrideAssStyles;
    fontsDirectory = builder.fontsDirectory;
    fontFamily = builder.fontFamily;
    verticalOffset = builder.verticalOffset;
    verticalOffsetSet = builder.verticalOffsetSet;
    secondaryVerticalOffset = builder.secondaryVerticalOffset;
    bitmapBrightness = builder.bitmapBrightness;
  }

  public double getPosition() {
    return position;
  }

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
  public boolean isPositionSet() {
    return positionSet;
  }

  public double getScale() {
    return scale;
  }

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
  public boolean isScaleSet() {
    return scaleSet;
  }

  public boolean usesSystemCaptionStyle() {
    return style == STYLE_SYSTEM;
  }

  public boolean hasCustomStyle() {
    return style == STYLE_CUSTOM;
  }

  public int getForegroundColor() {
    return foregroundColor;
  }

  public int getBackgroundColor() {
    return backgroundColor;
  }

  public int getEdgeType() {
    return edgeType;
  }

  public int getEdgeColor() {
    return edgeColor;
  }

  public double getEdgeSize() {
    return edgeSize;
  }

  public double getShadowOffset() {
    return shadowOffset;
  }

  public boolean isSecondarySubtitleConfigured() {
    return secondarySubtitleConfigured;
  }

  public int getSecondaryTrack() {
    return secondaryTrack;
  }

  public double getSecondaryPosition() {
    return secondaryPosition;
  }

  public boolean shouldOverrideSecondaryAssStyles() {
    return secondaryOverrideAssStyles;
  }

  /** Returns the subtitle fonts directory, or {@code null} when not configured. */
  @Nullable
  public String getFontsDirectory() {
    return fontsDirectory;
  }

  /** Returns the subtitle font family, or {@code null} when not configured. */
  @Nullable
  public String getFontFamily() {
    return fontFamily;
  }

  /** Returns the vertical subtitle offset in pixels. */
  public double getVerticalOffset() {
    return verticalOffset;
  }

  public boolean isVerticalOffsetSet() {
    return verticalOffsetSet;
  }

  /** Returns the vertical offset of the secondary subtitle in pixels. */
  public double getSecondaryVerticalOffset() {
    return secondaryVerticalOffset;
  }

  /** Returns the brightness applied to bitmap subtitles. */
  public double getBitmapBrightness() {
    return bitmapBrightness;
  }

  /** Builder for {@link MpvSubtitleOptions}. */
  public static final class Builder {

    private double position = 100.0;
    private boolean positionSet;
    private double scale = 1.0;
    private boolean scaleSet;
    private int style = STYLE_ORIGINAL;
    private int foregroundColor;
    private int backgroundColor;
    private int edgeType;
    private int edgeColor;
    private double edgeSize;
    private double shadowOffset;
    private boolean secondarySubtitleConfigured;
    private int secondaryTrack = SECONDARY_TRACK_OFF;
    private double secondaryPosition;
    private boolean secondaryOverrideAssStyles;
    @Nullable private String fontsDirectory;
    @Nullable private String fontFamily;
    private double verticalOffset;
    private boolean verticalOffsetSet;
    private double secondaryVerticalOffset;
    private double bitmapBrightness = 1.0;
    private boolean buildCalled;

    @CanIgnoreReturnValue
    public Builder setPosition(double position) {
      checkState(!buildCalled);
      this.position = position;
      positionSet = true;
      return this;
    }

    @CanIgnoreReturnValue
    public Builder setScale(double scale) {
      checkState(!buildCalled);
      this.scale = scale;
      scaleSet = true;
      return this;
    }

    @CanIgnoreReturnValue
    public Builder setSystemCaptionStyle() {
      checkState(!buildCalled);
      style = STYLE_SYSTEM;
      return this;
    }

    @CanIgnoreReturnValue
    public Builder setCustomStyle(
        int foregroundColor,
        int backgroundColor,
        int edgeType,
        int edgeColor,
        double edgeSize,
        double shadowOffset) {
      checkState(!buildCalled);
      style = STYLE_CUSTOM;
      this.foregroundColor = foregroundColor;
      this.backgroundColor = backgroundColor;
      this.edgeType = edgeType;
      this.edgeColor = edgeColor;
      this.edgeSize = edgeSize;
      this.shadowOffset = shadowOffset;
      return this;
    }

    @CanIgnoreReturnValue
    public Builder setSecondarySubtitle(int trackId, double position, boolean overrideAssStyles) {
      checkState(!buildCalled);
      checkArgument(trackId >= SECONDARY_TRACK_OFF);
      secondarySubtitleConfigured = true;
      secondaryTrack = trackId;
      secondaryPosition = position;
      secondaryOverrideAssStyles = overrideAssStyles;
      return this;
    }

    /** Sets the secondary subtitle position as a percentage of the video height. */
    @CanIgnoreReturnValue
    public Builder setSecondarySubtitlePosition(double position) {
      checkState(!buildCalled);
      secondarySubtitleConfigured = true;
      secondaryPosition = position;
      return this;
    }

    /** Sets the vertical offset of the secondary subtitle. */
    @CanIgnoreReturnValue
    public Builder setSecondaryVerticalOffset(double offset) {
      checkState(!buildCalled);
      secondaryVerticalOffset = offset;
      return this;
    }

    /** Sets whether secondary ASS styles are overridden by user styling. */
    @CanIgnoreReturnValue
    public Builder setSecondaryAssStyleOverride(boolean overrideAssStyles) {
      checkState(!buildCalled);
      secondarySubtitleConfigured = true;
      secondaryOverrideAssStyles = overrideAssStyles;
      return this;
    }

    /** Sets the directory containing subtitle fonts. */
    @CanIgnoreReturnValue
    public Builder setFontsDirectory(@Nullable String fontsDirectory) {
      checkState(!buildCalled);
      this.fontsDirectory = fontsDirectory;
      return this;
    }

    /** Sets the subtitle font family. */
    @CanIgnoreReturnValue
    public Builder setFontFamily(@Nullable String fontFamily) {
      checkState(!buildCalled);
      this.fontFamily = fontFamily;
      return this;
    }

    /** Sets the vertical offset of subtitles in pixels. */
    @CanIgnoreReturnValue
    public Builder setVerticalOffset(double offset) {
      checkState(!buildCalled);
      verticalOffset = offset;
      verticalOffsetSet = true;
      return this;
    }

    /** Sets the brightness applied to bitmap subtitles. */
    @CanIgnoreReturnValue
    public Builder setBitmapSubtitleBrightness(double brightness) {
      checkState(!buildCalled);
      bitmapBrightness = brightness;
      return this;
    }

    /** Sets the style used when embedded subtitle styling is overridden. */
    @CanIgnoreReturnValue
    public Builder setDefaultStyle(
        int foregroundColor,
        int backgroundColor,
        int edgeType,
        int edgeColor,
        double edgeSize,
        double shadowOffset) {
      checkState(!buildCalled);
      style = STYLE_CUSTOM;
      this.foregroundColor = foregroundColor;
      this.backgroundColor = backgroundColor;
      this.edgeType = edgeType;
      this.edgeColor = edgeColor;
      this.edgeSize = edgeSize;
      this.shadowOffset = shadowOffset;
      return this;
    }

    public MpvSubtitleOptions build() {
      checkState(!buildCalled);
      buildCalled = true;
      return new MpvSubtitleOptions(this);
    }
  }
}
