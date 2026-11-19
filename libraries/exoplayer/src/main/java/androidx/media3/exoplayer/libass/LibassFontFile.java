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
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

/** Reads the family name of a font file. */
@UnstableApi
public final class LibassFontFile {

  private static final int NAME_ID_FAMILY = 1;
  private static final int NAME_ID_TYPOGRAPHIC_FAMILY = 16;
  private static final int PLATFORM_UNICODE = 0;
  private static final int PLATFORM_MACINTOSH = 1;
  private static final int PLATFORM_WINDOWS = 3;

  private LibassFontFile() {}

  /**
   * Returns the family name stored in the font file.
   *
   * @param file The font file.
   * @return The family name, or {@code null} if the file does not contain one.
   * @throws IOException If the file cannot be read.
   */
  @Nullable
  public static String getFamilyName(File file) throws IOException {
    try (RandomAccessFile input = new RandomAccessFile(file, "r")) {
      int sfntVersion = input.readInt();
      if (sfntVersion != 0x00010000
          && sfntVersion != 0x4F54544F
          && sfntVersion != 0x74727565) {
        return null;
      }
      input.readUnsignedShort();
      input.readUnsignedShort();
      input.readUnsignedShort();
      input.readUnsignedShort();
      int numTables = input.readUnsignedShort();
      input.readUnsignedShort();
      input.readUnsignedShort();
      input.readUnsignedShort();
      long nameTableOffset = -1;
      long nameTableLength = 0;
      for (int i = 0; i < numTables; i++) {
        int tag = input.readInt();
        input.readInt();
        long offset = input.readInt() & 0xFFFFFFFFL;
        long length = input.readInt() & 0xFFFFFFFFL;
        if (tag == 0x6E616D65) {
          nameTableOffset = offset;
          nameTableLength = length;
          break;
        }
      }
      if (nameTableOffset < 0) {
        return null;
      }
      return readNameTable(input, nameTableOffset, nameTableLength);
    }
  }

  @Nullable
  private static String readNameTable(RandomAccessFile input, long offset, long length)
      throws IOException {
    input.seek(offset);
    input.readUnsignedShort();
    int count = input.readUnsignedShort();
    int stringOffset = input.readUnsignedShort();
    String bestWindows = null;
    String bestUnicode = null;
    String bestMac = null;
    for (int i = 0; i < count; i++) {
      int platformId = input.readUnsignedShort();
      int encodingId = input.readUnsignedShort();
      input.readUnsignedShort();
      int nameId = input.readUnsignedShort();
      int nameLength = input.readUnsignedShort();
      int nameOffset = input.readUnsignedShort();
      if (nameId != NAME_ID_FAMILY && nameId != NAME_ID_TYPOGRAPHIC_FAMILY) {
        continue;
      }
      if (nameLength <= 0 || stringOffset + (long) nameOffset + nameLength > length) {
        continue;
      }
      long position = input.getFilePointer();
      input.seek(offset + stringOffset + nameOffset);
      byte[] data = new byte[nameLength];
      input.readFully(data);
      input.seek(position);
      if (platformId == PLATFORM_WINDOWS && (encodingId == 0 || encodingId == 1)) {
        bestWindows = new String(data, StandardCharsets.UTF_16BE);
      } else if (platformId == PLATFORM_UNICODE) {
        bestUnicode = new String(data, StandardCharsets.UTF_16BE);
      } else if (platformId == PLATFORM_MACINTOSH && encodingId == 0) {
        bestMac = new String(data, StandardCharsets.ISO_8859_1);
      }
    }
    if (bestWindows != null) {
      return bestWindows;
    }
    if (bestUnicode != null) {
      return bestUnicode;
    }
    return bestMac;
  }
}
