package se.inera.intyg.minaintyg.util;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

public final class HashUtility {

  public static final String EMPTY = "EMPTY";

  private static HashFunction hf = Hashing.sha256();

  private HashUtility() {
  }

  public static String hash(final String payload) {
    if (Strings.isNullOrEmpty(payload)) {
      return EMPTY;
    }
    final byte[] digest = hf.hashString(payload, Charsets.UTF_8).asBytes();
    return BaseEncoding.base16().lowerCase().encode(digest);
  }
}
