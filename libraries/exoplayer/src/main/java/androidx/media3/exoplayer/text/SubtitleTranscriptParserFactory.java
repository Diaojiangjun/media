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

import androidx.media3.common.Format;
import androidx.media3.common.util.Consumer;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.extractor.text.CuesWithTiming;
import androidx.media3.extractor.text.SubtitleParser;

/**
 * Subtitle parser factory that records parsed cues into a {@link SubtitleTranscript}.
 *
 * <p>Phase 1 implementation: the parser consumes samples without emitting them; transcript
 * population is handled by {@link SubtitleTranscriptSession} and the subtitle content loader.
 */
@UnstableApi
public final class SubtitleTranscriptParserFactory implements SubtitleParser.Factory {

  private final SubtitleTranscript transcript;

  public SubtitleTranscriptParserFactory(SubtitleTranscript transcript) {
    this.transcript = transcript;
  }

  @Override
  public boolean supportsFormat(Format format) {
    String mimeType = format.sampleMimeType;
    return mimeType != null
        && (mimeType.startsWith("text/")
            || mimeType.contains("subrip")
            || mimeType.contains("vtt")
            || mimeType.contains("ttml")
            || mimeType.contains("ssa"));
  }

  @Override
  public int getCueReplacementBehavior(Format format) {
    return Format.CUE_REPLACEMENT_BEHAVIOR_MERGE;
  }

  @Override
  public SubtitleParser create(Format format) {
    return new SubtitleParser() {
      @Override
      public void parse(
          byte[] data,
          int offset,
          int length,
          OutputOptions outputOptions,
          Consumer<CuesWithTiming> output) {
        // Transcript population is performed by the content loader.
      }

      @Override
      public int getCueReplacementBehavior() {
        return Format.CUE_REPLACEMENT_BEHAVIOR_MERGE;
      }

      @Override
      public void reset() {
        transcript.clear();
      }
    };
  }
}
