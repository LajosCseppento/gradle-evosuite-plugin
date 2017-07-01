package com.github.cseppento.gradle.evosuite.testprojects.simple;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.OptionalLong;
import java.util.Random;
import java.util.Scanner;

/**
 * Simple console app which requires user interaction.
 */
public class SimpleApp implements Runnable {
    private final InputStream in;
    private final PrintStream out;
    private SimpleObjectSut obj;

    // visible for testingCe
    SimpleApp(InputStream in, PrintStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        try {
            intro();
            play();
        } catch (InterruptedException interruptEx) {
            out.println("Bye, bye my impatient friend!");
            Thread.currentThread().interrupt();
        } catch (IllegalStateException ex) {
            try {
                printlnAndWait("SECRET WIN!", 1000);
                printlnAndWait("Your rank now is...", 1500);
                printlnAndWait("CHEATER", 1000);
                printlnAndWait("Your gift is...", 3000);

                throw ex;
            } catch (InterruptedException interruptEx) {
                out.println("Nice, you got me, Mr Winner!");
                Thread.currentThread().interrupt();
            }
        }
    }

    private void intro() throws InterruptedException {
        printlnAndWait("Hi!", 1500);
        printlnAndWait("Your random number is...", 2000);

        obj = new SimpleObjectSut(new Random().nextLong() % 9000 + 1000);
        printlnAndWait("!!! " + obj.getValue() + " !!!", 1500);

        printlnAndWait("Now you will have to guess the divisors of this number", 2500);
        printlnAndWait("Don't fail me", 1000);
    }

    private void play() throws InterruptedException {
        int mistakes = 0;
        Scanner scanner = new Scanner(in);

        while (mistakes < 3) {
            out.print("Your guess: ");
            OptionalLong guess;
            String remainder;
            try {
                guess = OptionalLong.of(scanner.nextLong());
                remainder = scanner.nextLine();
            } catch (Exception ex) { // e.g.: non-digit character, EOF
                guess = OptionalLong.empty();
                remainder = "";
            }

            printlnAndWait("Parsing...", 1000);

            if (guess.isPresent() && remainder.trim().isEmpty()) {
                try {
                    obj.guessDivisor(guess.getAsLong());
                    printlnAndWait("Ok, go on", 500);
                } catch (IllegalArgumentException ex) {
                    printlnAndWait("MISTAKE: " + ex.getMessage(), 2000);
                    mistakes++;
                }
            } else {
                printlnAndWait("Haha, nice try, I will count it as a mistake", 2000);
                mistakes++;
            }

            obj.check();
        }

        printlnAndWait("Mr. Failure, here is your dump: " + obj, 3000);
        out.println("Bye, bye!");
    }

    private void printlnAndWait(String msg, long ms) throws InterruptedException {
        out.println(msg);
        Thread.sleep(ms);
    }

    @SuppressWarnings("squid:S106") // System.[in|out] is only used here
    public static void main(String[] args) {
        new SimpleApp(System.in, System.out).run();
    }
}
