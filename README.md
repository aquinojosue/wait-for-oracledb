# wait-for-oracle

A small Docker utility to check the availability or existence of tables and records in a dependent Oracle database, tested with [Oracle 11g](https://hub.docker.com/r/sath89/oracle-xe-11g/).

It uses maven, the OJDBC driver is included under the `driver` folder, to build just run `mvn package`.

To use in a project copy both, the jar file (the one that's not prefixed with `original-`) at the `target` folder and the bash script in the `bin` directory to a location in your project, add as entrypoint to your docker file among the lines of the supplied example.

```sh
usage: wait-for-oracle host user password command [-q query] [-d delay] [-e --exceptions]
 -c,--command <arg>   The command to run after the verification success.
 -d,--delay <arg>     The delay between tries.
 -e,--exceptions      Show the exception messages with the retry message.
 -h,--host <arg>      The JDBC connection string. iex:
                      jdbc:oracle:thin:@localhost:1521:xe
 -p,--pass <arg>      The database password.
 -q,--query <arg>     The query to verify the database integrity.
 -u,--user <arg>      The username for the databases.
```
Add in the docker file for example:
```dockerfile
    volumes:
      - ./docker/wait-for-oracle.jar:/wait-for-oracle.jar
      - ./docker/wait-for-oracle:/wait-for-oracle
    entrypoint: ["./wait-for-oracle", "-h", "jdbc:oracle:thin:@oracle:1521:xe", "-u", "system", "-p", "system", "-c", "\"java -Djava.security.egd=file:/dev/./urandom -jar /app.jar\""]
```
## TODO
* Still working in the README
* Handle args better in the script
* Default connection and credentials
* Maybe it doesn't actually need the bash script?
