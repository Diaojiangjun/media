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

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

/** Writes a fontconfig file describing the font directories used by libass. */
@UnstableApi
public final class LibassFontConfig {

  private static final String SYSTEM_FONTS_DIRECTORY = "/system/fonts";

  private LibassFontConfig() {}

  /**
   * Writes a fontconfig file.
   *
   * @param output File to write.
   * @param cacheDirectory Directory used by fontconfig to store its cache, or {@code null}.
   * @param directories Additional font directories.
   * @throws IOException If the file cannot be written.
   */
  public static void write(
      File output, @Nullable File cacheDirectory, List<File> directories) throws IOException {
    File parent = output.getParentFile();
    if (parent != null && !parent.exists() && !parent.mkdirs() && !parent.exists()) {
      throw new IOException("Unable to create " + parent);
    }
    try (Writer writer =
        new OutputStreamWriter(new FileOutputStream(output), StandardCharsets.UTF_8)) {
      writer.write("<?xml version=\"1.0\"?>\n");
      writer.write("<!DOCTYPE fontconfig SYSTEM \"fonts.dtd\">\n");
      writer.write("<fontconfig>\n");
      writeDirectory(writer, new File(SYSTEM_FONTS_DIRECTORY));
      if (cacheDirectory != null) {
        writeCachedir(writer, cacheDirectory);
      }
      for (File directory : directories) {
        writeDirectory(writer, directory);
      }
      writer.write("</fontconfig>\n");
    }
  }

  private static void writeDirectory(Writer writer, @Nullable File directory) throws IOException {
    if (directory == null) {
      return;
    }
    writer.write("  <dir>");
    writer.write(escape(directory.getAbsolutePath()));
    writer.write("</dir>\n");
  }

  private static void writeCachedir(Writer writer, File cacheDirectory) throws IOException {
    if (!cacheDirectory.exists() && !cacheDirectory.mkdirs() && !cacheDirectory.exists()) {
      return;
    }
    writer.write("  <cachedir>");
    writer.write(escape(cacheDirectory.getAbsolutePath()));
    writer.write("</cachedir>\n");
  }

  private static String escape(String value) {
    return value
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&apos;");
  }
}
