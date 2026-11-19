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

import android.net.Uri;
import androidx.media3.common.Format;
import androidx.media3.common.MediaItem;
import androidx.media3.common.text.SubtitleContent;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Default text subtitle loader supporting SRT/VTT style files. */
@UnstableApi
public final class DefaultSubtitleContentLoader implements SubtitleContentLoader {

  private static final int MAX_BYTES = 8 * 1024 * 1024;
  private static final Pattern TIME_LINE =
      Pattern.compile(
          "(\\d{1,2}:)?(\\d{2}):(\\d{2})[,.](\\d{1,3})\\s*-->\\s*(\\d{1,2}:)?(\\d{2}):(\\d{2})[,.](\\d{1,3})");

  private final DataSource.Factory factory;

  public DefaultSubtitleContentLoader(DataSource.Factory factory) {
    this.factory = factory;
  }

  @Override
  public SubtitleContent load(MediaItem item, Uri uri, Format format) throws IOException {
    String text = read(uri);
    if (text.isEmpty()) {
      return SubtitleContent.LOADING;
    }
    List<SubtitleContent.Entry> entries = parse(text);
    return new SubtitleContent(
        entries.isEmpty() ? SubtitleContent.State.PARTIAL : SubtitleContent.State.COMPLETE,
        entries);
  }

  private String read(Uri uri) throws IOException {
    DataSource dataSource = factory.createDataSource();
    DataSpec dataSpec = new DataSpec(uri);
    try {
      dataSource.open(dataSpec);
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      byte[] buffer = new byte[16 * 1024];
      int total = 0;
      int read;
      while ((read = dataSource.read(buffer, 0, buffer.length)) != -1) {
        total += read;
        if (total > MAX_BYTES) {
          break;
        }
        output.write(buffer, 0, read);
      }
      return output.toString(StandardCharsets.UTF_8.name());
    } finally {
      dataSource.close();
    }
  }

  private static List<SubtitleContent.Entry> parse(String text) {
    List<SubtitleContent.Entry> entries = new ArrayList<>();
    String normalized = text.replace("\r\n", "\n").replace('\r', '\n');
    if (normalized.startsWith("\uFEFF")) {
      normalized = normalized.substring(1);
    }
    String[] blocks = normalized.split("\\n\\s*\\n");
    for (String block : blocks) {
      String[] lines = block.split("\n");
      int timeIndex = -1;
      Matcher matcher = null;
      for (int i = 0; i < lines.length; i++) {
        matcher = TIME_LINE.matcher(lines[i]);
        if (matcher.find()) {
          timeIndex = i;
          break;
        }
      }
      if (matcher == null || timeIndex < 0) {
        continue;
      }
      long startUs = toUs(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4));
      long endUs = toUs(matcher.group(5), matcher.group(6), matcher.group(7), matcher.group(8));
      StringBuilder builder = new StringBuilder();
      for (int i = timeIndex + 1; i < lines.length; i++) {
        if (builder.length() > 0) {
          builder.append('\n');
        }
        builder.append(lines[i].replaceAll("<[^>]+>", ""));
      }
      entries.add(new SubtitleContent.Entry(startUs, endUs, builder.toString(), false));
    }
    entries.sort((first, second) -> Long.compare(first.startTimeUs, second.startTimeUs));
    return entries;
  }

  private static long toUs(String hours, String minutes, String seconds, String millis) {
    long h = hours == null ? 0 : Long.parseLong(hours.substring(0, hours.length() - 1));
    long m = Long.parseLong(minutes);
    long s = Long.parseLong(seconds);
    long ms = Long.parseLong(millis);
    if (millis.length() == 1) {
      ms *= 100;
    } else if (millis.length() == 2) {
      ms *= 10;
    }
    return ((h * 3600 + m * 60 + s) * 1000 + ms) * 1000;
  }
}
