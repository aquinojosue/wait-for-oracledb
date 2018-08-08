package com.kenpb;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitForOracleApplication {

    public static void main(String[] args) throws InterruptedException {
        Options options = new Options();
        Logger logger = LoggerFactory.getLogger(WaitForOracleApplication.class);

        Option host = new Option("h", "host", true,
                "The JDBC connection string. iex:\n jdbc:oracle:thin:@localhost:1521:xe");
        host.setRequired(true);
        options.addOption(host);

        Option userOption = new Option("u", "user", true, "The username for the databases.");
        userOption.setRequired(true);
        options.addOption(userOption);

        Option passOption = new Option("p", "pass", true, "The database password.");
        passOption.setRequired(true);
        options.addOption(passOption);

        Option commandOption = new Option("c", "command", true, "The command to run after the verification success.");
        commandOption.setRequired(true);
        options.addOption(commandOption);

        Option queryOption = new Option("q", "query", true, "The query to verify the database integrity.");
        queryOption.setRequired(false);
        options.addOption(queryOption);

        Option delayOption = new Option("d", "delay", true, "The delay between tries.");
        delayOption.setRequired(false);
        options.addOption(delayOption);

        Option exceptionsOption = new Option("e", "exceptions", false, "Show the exception messages with the retry message.");
        exceptionsOption.setRequired(false);
        options.addOption(exceptionsOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());

            formatter.printHelp("wait-for-oracle host user password command [-q query] [-d delay] [-e --exceptions]", options);

            System.exit(1);
        }

        Integer delay = 5000;
        String commandString = cmd.getOptionValue("command");

        if (cmd.hasOption("delay"))
            delay = Integer.parseInt(cmd.getOptionValue("delay"));

        while (true)
            try {
                DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

                String queryString = "select 'dummy' txt from dual";

                if (cmd.hasOption("query"))
                    queryString = cmd.getOptionValue("query");

                ResultSet res = DriverManager.getConnection(cmd.getOptionValue("host"), cmd.getOptionValue("user"),
                        cmd.getOptionValue("pass")).prepareCall(queryString).executeQuery();
                if (res.next()) {
                    logger.info("Connection to the DB successfully.\n");

                    ProcessBuilder processBuilder = new ProcessBuilder(commandString.split(" "));
                    processBuilder.inheritIO();

                    Process process = processBuilder.start();
                    logger.info("Running command...");
                    process.waitFor();

                    System.exit(0);
                }
            } catch (SQLException e) {
                logger.error("Could not verify the integrity of the database, retrying in {} secs...\n", 
                    delay / 1000);

                if (cmd.hasOption("exceptions"))
                    e.printStackTrace();

                Thread.sleep(delay);
                continue;
            } catch (IOException e) {
                logger.error("Command execution failed...\n", e);

                if (cmd.hasOption("exceptions"))
                    e.printStackTrace();

                System.exit(-1);
            }
    }
}
