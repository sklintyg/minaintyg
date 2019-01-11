/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JspPageAttributesTest {

    private JspPageAttributes jspPageAttributes = new JspPageAttributes();

    @Test
    public void testGetUseMinifiedJavaScript() {
        System.setProperty("useMinifiedJavaScript", "true");
        String res = jspPageAttributes.getUseMinifiedJavaScript();

        assertEquals("true", res);
    }

    @Test
    public void testGetUseMinifiedJavaScriptFalse() {
        System.setProperty("useMinifiedJavaScript", "false");
        String res = jspPageAttributes.getUseMinifiedJavaScript();

        assertEquals("false", res);
    }

    @Test
    public void testGetUseMinifiedJavaScriptNotSet() {
        String res = jspPageAttributes.getUseMinifiedJavaScript();

        assertEquals("true", res);
    }

}
