# wait-for-oracle

A small Docker utility to check the availability of an Oracle database, tested with Oracle 11g.

It uses maven the OJDBC driver is included under the `driver` folder, to build just run `mvn pacakge` and grab the file that's not prefixed with `original-`.

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

```dockerfile

```
