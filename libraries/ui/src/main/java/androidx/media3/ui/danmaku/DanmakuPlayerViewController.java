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
package androidx.media3.ui.danmaku;

import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.ui.PlayerView;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;

/** Binds danmaku state to a {@link PlayerView} and its {@link DanmakuController}. */
@UnstableApi
public final class DanmakuPlayerViewController {

  @Nullable private PlayerView playerView;
  @Nullable private OkHttpClient client;
  @Nullable private DanmakuConfig config;
  @Nullable private Uri dataSource;
  private boolean enabled = true;

  /** Binds the controller to a player view and applies pending state. */
  public void bind(PlayerView playerView) {
    this.playerView = playerView;
    if (client != null) {
      playerView.setDanmakuOkHttpClient(client);
    }
    playerView.setDanmakuEnabled(enabled);
    if (config != null) {
      playerView.setDanmakuConfig(config);
    }
    if (dataSource != null) {
      playerView.setDanmakuSource(dataSource);
    }
  }

  /** Sets the danmaku rendering configuration. */
  public void setConfig(DanmakuConfig config) {
    this.config = config;
    if (playerView != null) {
      playerView.setDanmakuConfig(config);
    }
  }

  /** Sets the HTTP client used for danmaku sources. */
  public void setOkHttpClient(@Nullable OkHttpClient client) {
    this.client = client;
    if (playerView != null) {
      playerView.setDanmakuOkHttpClient(client);
    }
  }

  /** Enables or disables danmaku rendering. */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
    if (playerView != null) {
      playerView.setDanmakuEnabled(enabled);
    }
  }

  /** Sets the danmaku source URI, or {@code null} to clear items. */
  public void setDataSource(@Nullable Uri uri) {
    dataSource = uri;
    if (playerView != null) {
      playerView.setDanmakuSource(uri);
    }
  }

  /** Sends a danmaku item at the current playback position. */
  public void post(@Nullable Danmaku item) {
    if (playerView != null && item != null) {
      playerView.getDanmakuController().sendNow(item);
    }
  }

  /** Returns the currently loaded items. */
  public List<Danmaku> getItems() {
    return playerView == null
        ? new ArrayList<>()
        : playerView.getDanmakuController().getItems();
  }

  /** Sends a text danmaku at the current playback position. */
  public void sendNow(String text) {
    if (playerView != null) {
      playerView.sendDanmaku(text);
    }
  }

  /** Detaches from the current player view. */
  public void close() {
    playerView = null;
  }
}
