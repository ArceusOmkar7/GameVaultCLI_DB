// src/com/project/gamevaultcli/utils/CredentialUtil.java
package com.project.gamevaultcli.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class CredentialUtil {

    private static final String DB_CREDENTIALS_FILE = "db_credentials.txt";

    public static Properties getDBProperties() throws IOException {
        Properties props = new Properties();
        File dbFile = new File(DB_CREDENTIALS_FILE);

        if (!dbFile.exists()) {
            createDBCredentialsFile(props, dbFile);
        } else {
            try (FileReader fr = new FileReader(dbFile);
                 BufferedReader br = new BufferedReader(fr)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains("=")) {
                        String[] parts = line.split("=", 2); // Split into key and value
                        if (parts.length == 2) {
                            props.setProperty(parts[0].trim(), parts[1].trim());
                        }
                    }
                }
            }
        }

        return props;
    }

    private static void createDBCredentialsFile(Properties props, File dbFile) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Database credentials file not found. Please enter database details:");
        System.out.print("Enter database URL (e.g., jdbc:mysql://localhost:3306/gamevault): ");
        String dbUrl = scanner.nextLine();
        System.out.print("Enter database username: ");
        String dbUser = scanner.nextLine();
        System.out.print("Enter database password: ");
        String dbPassword = scanner.nextLine();

        props.setProperty("db.url", dbUrl);
        props.setProperty("db.user", dbUser);
        props.setProperty("db.password", dbPassword);

        try (FileWriter fw = new FileWriter(dbFile);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("db.url=" + dbUrl + "\n");
            bw.write("db.user=" + dbUser + "\n");
            bw.write("db.password=" + dbPassword + "\n");
            System.out.println("Database credentials saved to " + DB_CREDENTIALS_FILE);
        }
    }
}