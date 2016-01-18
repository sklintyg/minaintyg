/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.safari.SafariDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver

driver = { new FirefoxDriver() } // use firefox by default
//driver = { new ChromeDriver() }
//driver = { new HtmlUnitDriver() }
//baseUrl = "http://localhost:8088"
waiting {
    timeout = 5 // default wait is two seconds
}
environments {
    chrome {
        driver = { new ChromeDriver() }
    }
    safari {
        driver = { new SafariDriver() }
    }
    firefox {
        driver = { new FirefoxDriver() }
    }
    headless {
        driver = { new HtmlUnitDriver() }
    }
    'win-ie' {
        driver = {
            new RemoteWebDriver(new URL("http://windows.ci-server.local"), DesiredCapabilities.internetExplorer())
        }
    }
}
