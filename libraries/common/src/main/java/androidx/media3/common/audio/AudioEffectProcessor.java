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
package androidx.media3.common.audio;

import androidx.media3.common.util.UnstableApi;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Pass-through audio processor that carries the effect configuration.
 *
 * <p>Phase 1 implementation: buffers are forwarded unchanged. Equalizer and channel mixing are
 * applied by the platform equalizer or MPV audio filters.
 */
@UnstableApi
public final class AudioEffectProcessor implements AudioProcessor {

  private static final ByteBuffer EMPTY =
      ByteBuffer.allocateDirect(0).order(ByteOrder.nativeOrder());

  private AudioProcessor.AudioFormat inputFormat = AudioProcessor.AudioFormat.NOT_SET;
  private AudioProcessor.AudioFormat outputFormat = AudioProcessor.AudioFormat.NOT_SET;
  private AudioEffectParameters parameters = AudioEffectParameters.DEFAULT;
  private ByteBuffer output = EMPTY;
  private boolean active;
  private boolean ended;

  /** Applies new effect parameters. */
  public void setParameters(AudioEffectParameters parameters, boolean softwareEqualizer) {
    this.parameters = parameters == null ? AudioEffectParameters.DEFAULT : parameters;
    this.active = this.parameters.hasEffect();
  }

  /** Clears all effect parameters. */
  public void resetParameters() {
    parameters = AudioEffectParameters.DEFAULT;
    active = false;
  }

  @Override
  public AudioProcessor.AudioFormat configure(AudioProcessor.AudioFormat inputAudioFormat) {
    inputFormat = inputAudioFormat;
    outputFormat = inputAudioFormat;
    return outputFormat;
  }

  @Override
  public boolean isActive() {
    return active;
  }

  @Override
  public void queueInput(ByteBuffer inputBuffer) {
    int remaining = inputBuffer.remaining();
    if (remaining == 0) {
      output = EMPTY;
      return;
    }
    ByteBuffer buffer = ByteBuffer.allocateDirect(remaining).order(ByteOrder.nativeOrder());
    buffer.put(inputBuffer.duplicate());
    buffer.flip();
    output = buffer;
    ended = false;
  }

  @Override
  public void queueEndOfStream() {
    ended = true;
  }

  @Override
  public ByteBuffer getOutput() {
    ByteBuffer buffer = output;
    output = EMPTY;
    return buffer;
  }

  @Override
  public boolean isEnded() {
    return ended && !output.hasRemaining();
  }

  @Override
  public void flush() {
    output = EMPTY;
    ended = false;
  }

  @Override
  public void reset() {
    flush();
    active = false;
    parameters = AudioEffectParameters.DEFAULT;
    inputFormat = AudioProcessor.AudioFormat.NOT_SET;
    outputFormat = AudioProcessor.AudioFormat.NOT_SET;
  }
}
