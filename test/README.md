
### För att slippa ändra version på minaintyg-testtools och köra npm install vid varje ändring så behöver följande kommandon köras:

 ```sh
 cd minaintygTestTools/
 npm link
 cd ..
 npm link minaintyg-testtools
```

### Då ändringar har gjorts i dessa moduler så bör man ändra versionsnummer för paketet innan incheckning

### Restassured

Restassured-tester kan köras från roten av /minaintyg

    # Alla testklasser i ett paket
    ./gradlew restAssured --tests se.inera.intyg.minaintyg.web.controller.integrationtest.moduleapi.*
    
    # Alla metoder i en testklass
    ./gradlew restAssured --tests *ApiControllerIT
    
    # Enskild metod i testklass
    ./gradlew restAssured --tests *ApiControllerIT.testArchive
