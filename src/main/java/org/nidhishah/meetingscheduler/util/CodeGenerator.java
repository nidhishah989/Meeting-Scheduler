package org.nidhishah.meetingscheduler.util;
import java.util.Random;

public class CodeGenerator {

    public static String generateSixDigitCode() {
        Random random = new Random();
        int code = 100_000 + random.nextInt(900_000); // Generates a 6-digit code
        return String.valueOf(code);
    }
}
