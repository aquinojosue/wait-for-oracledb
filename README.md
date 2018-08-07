# wait-for-oracle

## TODO
* Still working in the README
* Handle args better in the script
* Default connection and credentials
* Maybe it doesn't actually need the bash script?

A small Docker utility to check the availability of an Oracle database, tested with Oracle 11g.

It uses maven the OJDBC driver is included under the `driver` folder, to build just run `mvn package` and grab the file that's not prefixed with `original-`, copy both, the jar and the script at the `target` and `bin` directories respectively and run as showed in the example.

```sh
usage: wait-for-oracle host user password command [-q query] [-d delay]
 -c,--command <arg>   The command to run after the verification success.
 -d,--delay <arg>     The delay between tries.
 -h,--host <arg>      The JDBC connection string. iex:
                      jdbc:oracle:thin:@localhost:1521:xe
 -p,--pass <arg>      The database password.
 -q,--query <arg>     The query to verify the database integrity.
 -u,--user <arg>      The username for the databases.
```
Add in the docker file:
```dockerfile
entrypoint: ["./wait-for-oracle", "-h", "jdbc:oracle:thin:@oracle:1521:xe", "-u", "system", "-p", "system", "-c", "\"java -Djava.security.egd=file:/dev/./urandom -jar /app.jar\""]
```
