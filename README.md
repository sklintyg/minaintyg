# Webcert
Webcert är en webbtjänst för att författa intyg samt ställa frågor och svar kring dem. 

## Utvecklingssetup
Webcert byggs och körs med hjälp av Maven enligt följande:

```
$ git clone https://github.com/sklintyg/webcert.git

$ cd webcert
$ mvn jetty:run
$ open http://localhost:9088/welcome.jsp
```

Detta startar Webcert med stubbar för alla externa tjänster som Webcert använder. För att köra både Mina intyg och Webcert samtidigt behöver [Intygstjänsten](https://github.com/sklintyg/intygstjanst) startas före Mina intyg och Webcert.

### Starta specifik version
Man kan även starta Webcert i ett läge där endast de funktioner som är tillgängliga i en viss version är tillgängliga.

```
$ mvn jetty:run -Pv0.6
```

### Eclipse
TODO.

### IntelliJ IDEA
TODO.


## Licens

Copyright (C) 2014 Inera AB (http://www.inera.se)

Webcert is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

Webcert is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.

Se även [LICENSE.txt](https://github.com/sklintyg/common/blob/master/LICENSE.txt). 
