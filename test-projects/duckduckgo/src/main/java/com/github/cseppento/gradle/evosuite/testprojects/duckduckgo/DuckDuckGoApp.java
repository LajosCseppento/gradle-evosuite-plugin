package com.github.cseppento.gradle.evosuite.testprojects.duckduckgo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Simple console app which requires user interaction to use a {@link DuckDuckGoClient}.
 */
public class DuckDuckGoApp implements Runnable {
    private final InputStream in;
    private final PrintStream out;
    private final DuckDuckGoClient client;

    // visible for testing
    DuckDuckGoApp(InputStream in, PrintStream out, DuckDuckGoClient client) {
        // BUG: missing null checks
        this.in = in;
        this.out = out;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            out.println("I will search Duck Duck Go for you");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while (true) {
                out.print("Search: ");
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                line = line.trim();
                if (!line.isEmpty()) {
                    DuckDuckGoAnswer answer = client.search(line);
                    out.println(answer);

                    // BUG: what if answer field is null
                    if (!line.toLowerCase().contains("equals sign")
                            && answer.getHeading().toLowerCase().contains("equals sign")) {
                        // e.g., search for '='
                        out.println("JACKPOT!");
                    }
                }
            }
            out.println("Thanks for using me!");
        } catch (Exception ex) {
            throw new DuckDuckGoAppException("Failure!", ex);
        }
    }

    @SuppressWarnings("squid:S106") // System.[in|out] is only used here
    public static void main(String[] args) {
        new DuckDuckGoApp(System.in, System.out, new DuckDuckGoClient()).run();
    }
}
