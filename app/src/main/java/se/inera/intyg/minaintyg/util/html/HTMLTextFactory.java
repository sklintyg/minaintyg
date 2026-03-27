/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.minaintyg.util.html;

import static se.inera.intyg.minaintyg.util.html.HTMLFactory.tag;

import java.util.HashMap;

public class HTMLTextFactory {

  private static final String IDS_HEADING_2 = "ids-heading-2";
  private static final String IDS_HEADING_3 = "ids-heading-3";
  private static final String IDS_HEADING_4 = "ids-heading-4";
  private static final String IDS_HEADING_5 = "ids-heading-5";

  private HTMLTextFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static String h2(String value) {
    return tag("h2", IDS_HEADING_2, value);
  }

  public static String h3(String value) {
    return tag("h3", IDS_HEADING_3, value);
  }

  public static String h4(String value) {
    return tag("h4", IDS_HEADING_4, value);
  }

  public static String h5(String value) {
    return tag("h5", IDS_HEADING_5, value);
  }

  public static String p(String value) {
    return tag("p", value);
  }

  public static String link(String url, String name) {
    final var attributes = new HashMap<String, String>();
    attributes.put("href", url);
    attributes.put("target", "_blank");

    return tag("a", null, name, attributes);
  }
}
