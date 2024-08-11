# JsonUtils Tool - made by *King Bean*
## Shorten Json by whitelist and blacklist JsonPath
### use CURL below to call API:
```json
curl --location 'localhost:8080/jsonUtils/shortenJson' \ --header 'Content-Type: application/json' \ --data '{ "whiteList": [ "$.info.username", "$.info.importantInfo" ], "blackList": [ "$.info.importantInfo.decryptKey" ], "jsonObject": { "info": { "name": "Do Duc Vuong", "username": "vuongdo", "importantInfo": { "pass": "123456", "decryptKey": "secret" } } } }'
```