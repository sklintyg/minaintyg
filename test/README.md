
### För att slippa ändra version på minaintyg-testtools och köra npm install vid varje ändring så behöver följande kommandon köras:

 ```sh
 cd minaintygTestTools/
 npm link
 cd ..
 npm link minaintyg-testtools
```

### Då ändringar har gjorts i dessa moduler så bör man ändra versionsnummer för paketet innan incheckning