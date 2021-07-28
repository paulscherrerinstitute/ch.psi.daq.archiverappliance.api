# Usage

This module provides the API/extracted functionality to access the archiver appliance data

The archiver appliance documentation can be found here: http://slacmshankar.github.io/epicsarchiver_docs/userguide.html

For production testing a VPN can be established to SwissFEL as follows:
```bash
sshuttle -r sflca 172.26.120.117/24 172.26.40.28/24
sshuttle -r hipa-daq-01 172.19.10.40/24
```

Provide specific configuration:
```bash
-Dspring.config.location=/tmp/application.properties
```


TWLHA example: Forcing Rebuild of the internal channel cache:
```
curl -H Content-Type:application/json -X POST http://twlha-data-api.psi.ch:9090/channels -d '{ "reload" : true }'
```

# Development

To build an upload the project use:
```bash
gradle -x test clean build bootJar uploadBootArchives
```