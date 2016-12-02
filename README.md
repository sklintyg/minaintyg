# Mina intyg
Mina Intyg är en webbtjänst som möjliggör för invånare att hantera sina läkarintyg.

## Kom igång
Här hittar du grundläggande instruktioner för hur man kommer igång med projektet. Mer detaljerade instruktioner för att sätta upp sin utvecklingsmiljö och liknande hittar du på projektets [Wiki för utveckling](https://github.com/sklintyg/common/wiki).

### Bygg projektet
Mina intyg byggs med hjälp av Maven enligt följande:
```
$ git clone https://github.com/sklintyg/minaintyg.git

$ cd minaintyg
$ ./gradlew clean build install -PcodeQuality
```

### Starta webbapplikationen
För att starta webbapplikationen så måste [Intygstjänsten](https://github.com/sklintyg/intygstjanst)  vara startad eftersom Mina intyg hämtar intygen direkt från Intygstjänsten. Därefter kan webbapplikationen startas med Jetty enligt följande:
```
$ cd minaintyg
$ ./gradlew appRun
$ open http://localhost:8088/welcome.jsp

```
### Kör Protractor
För att köra Protractor-testerna måste Intygstjänsten och Mina intyg vara igång:
```
$ cd minaintyg/test
$ grunt
```

### Kör FitNesse
För att köra FitNesse-testerna måste man starta FitNesse wiki samt se till att Intygstjänsten och Mina intyg är igång:
```
$ cd minaintyg/specifications
$ ../gradlew fitnesseWiki
$ open http://localhost:9124/MinaIntyg.AutomatiseradeTester
```

## Licens
Copyright (C) 2014 Inera AB (http://www.inera.se)

Mina intyg is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

Mina intyg is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.

Se även [LICENSE.md](https://github.com/sklintyg/minaintyg/blob/master/LICENSE.md). 
