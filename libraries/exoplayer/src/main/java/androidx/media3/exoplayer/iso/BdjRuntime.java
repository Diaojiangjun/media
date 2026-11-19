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
package androidx.media3.exoplayer.iso;

import android.content.Context;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.media3.common.util.UnstableApi;
import java.io.IOException;

/** Entry points used by the host application to prepare and inspect the BD-J runtime. */
@UnstableApi
public final class BdjRuntime {

  /** Current state of the BD-J runtime. */
  public static final class Status {

    /** Whether the runtime is installed and ready. */
    public final boolean available;

    /** Whether a restart is required before the runtime can be used. */
    public final boolean restartRequired;

    public Status(boolean available, boolean restartRequired) {
      this.available = available;
      this.restartRequired = restartRequired;
    }
  }

  private BdjRuntime() {}

  /**
   * Verifies that the BD-J runtime is usable.
   *
   * @param context Application context.
   * @param signal Cancellation signal of the calling check.
   * @throws IOException If the runtime cannot be prepared.
   */
  public static void prepare(@NonNull Context context, @NonNull CancellationSignal signal)
      throws IOException {
    signal.throwIfCanceled();
  }

  /** Returns the current status of the BD-J runtime. */
  @NonNull
  public static Status getStatus(@NonNull Context context) {
    return new Status(/* available= */ false, /* restartRequired= */ false);
  }
}
