package com.mycompany.main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 test suite for Main.java.
 *
 * This class lives in the same package (com.mycompany.main) so it can read
 * and reset Main's package-private static fields (username, password,
 * registeredUsername, etc.) directly between tests, since those fields have
 * no access modifier (i.e., they are package-private).
 */
class MainTest {

    private final InputStream originalIn = System.in;

    @BeforeEach
    void resetState() {
        // Reset all shared static state before every test so tests don't
        // leak into one another.
        Main.username = null;
        Main.password = null;
        Main.cellphone = null;
        Main.firstName = null;
        Main.lastName = null;
        Main.registeredUsername = null;
        Main.registeredPassword = null;
        Main.registeredCellPhone = null;
    }

    @AfterEach
    void restoreSystemIn() {
        System.setIn(originalIn);
    }

    /** Helper: point System.in at a canned sequence of lines and re-create Main.input. */
    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
        Main.input = new java.util.Scanner(System.in);
    }

    // ------------------------------------------------------------------
    // checkUserName
    // ------------------------------------------------------------------
    @Nested
    @DisplayName("checkUserName")
    class CheckUserNameTests {

        @Test
        @DisplayName("valid: underscore present, length <= 5")
        void validUsername() {
            assertTrue(Main.checkUserName("ky_1"));
        }

        @Test
        @DisplayName("valid: exactly 5 characters with underscore")
        void validUsernameExactlyFiveChars() {
            assertTrue(Main.checkUserName("a_bcd"));
        }

        @Test
        @DisplayName("invalid: no underscore")
        void noUnderscore() {
            assertFalse(Main.checkUserName("kyle1"));
        }

        @Test
        @DisplayName("invalid: too long even with underscore")
        void tooLong() {
            assertFalse(Main.checkUserName("kyle_smith"));
        }

        @Test
        @DisplayName("invalid: empty string")
        void emptyString() {
            assertFalse(Main.checkUserName(""));
        }

        @Test
        @DisplayName("invalid: only underscore but too long")
        void onlyUnderscoreTooLong() {
            assertFalse(Main.checkUserName("______"));
        }
    }

    // ------------------------------------------------------------------
    // checkPasswordComplexity
    // ------------------------------------------------------------------
    @Nested
    @DisplayName("checkPasswordComplexity")
    class CheckPasswordComplexityTests {

        @Test
        @DisplayName("valid: meets all complexity rules")
        void validPassword() {
            assertTrue(Main.checkPasswordComplexity("Ch&&sec@ke99!"));
        }

        @Test
        @DisplayName("valid: exactly 8 chars with all requirements")
        void validMinimumLengthPassword() {
            assertTrue(Main.checkPasswordComplexity("Aa1!aaaa"));
        }

        @Test
        @DisplayName("invalid: too short")
        void tooShort() {
            assertFalse(Main.checkPasswordComplexity("Aa1!aaa"));
        }

        @Test
        @DisplayName("invalid: no uppercase letter")
        void noUppercase() {
            assertFalse(Main.checkPasswordComplexity("aa1!aaaa"));
        }

        @Test
        @DisplayName("invalid: no digit")
        void noDigit() {
            assertFalse(Main.checkPasswordComplexity("Aa!aaaaa"));
        }

        @Test
        @DisplayName("invalid: no special character")
        void noSpecialCharacter() {
            assertFalse(Main.checkPasswordComplexity("Aa1aaaaa"));
        }

        @Test
        @DisplayName("invalid: empty string")
        void emptyString() {
            assertFalse(Main.checkPasswordComplexity(""));
        }
    }

    // ------------------------------------------------------------------
    // checkCellPhoneNumber
    // ------------------------------------------------------------------
    @Nested
    @DisplayName("checkCellPhoneNumber")
    class CheckCellPhoneNumberTests {

        @Test
        @DisplayName("valid: +27 followed by 9 digits")
        void validNumber() {
            assertTrue(Main.checkCellPhoneNumber("+27821234567"));
        }

        @Test
        @DisplayName("invalid: missing +27 prefix")
        void missingCountryCode() {
            assertFalse(Main.checkCellPhoneNumber("0821234567"));
        }

        @Test
        @DisplayName("invalid: too few digits after +27")
        void tooFewDigits() {
            assertFalse(Main.checkCellPhoneNumber("+2782123456"));
        }

        @Test
        @DisplayName("invalid: too many digits after +27")
        void tooManyDigits() {
            assertFalse(Main.checkCellPhoneNumber("+278212345678"));
        }

        @Test
        @DisplayName("invalid: contains non-digit characters")
        void containsLetters() {
            assertFalse(Main.checkCellPhoneNumber("+2782123abc7"));
        }

        @Test
        @DisplayName("invalid: empty string")
        void emptyString() {
            assertFalse(Main.checkCellPhoneNumber(""));
        }
    }

    // ------------------------------------------------------------------
    // returnLoginStatus
    // ------------------------------------------------------------------
    @Nested
    @DisplayName("returnLoginStatus")
    class ReturnLoginStatusTests {

        @Test
        @DisplayName("successful login returns welcome message with first/last name")
        void successfulLogin() {
            Main.firstName = "Thabo";
            Main.lastName = "Nkosi";

            String result = Main.returnLoginStatus(true);

            assertEquals("Welcome Thabo Nkosi it is great to see you again.", result);
        }

        @Test
        @DisplayName("failed login returns generic error message")
        void failedLogin() {
            String result = Main.returnLoginStatus(false);

            assertEquals("Username or password incorrect, please try again.", result);
        }
    }

    // ------------------------------------------------------------------
    // loginUser
    // ------------------------------------------------------------------
    @Nested
    @DisplayName("loginUser")
    class LoginUserTests {

        @Test
        @DisplayName("returns true immediately when credentials match on first try")
        void loginSucceedsOnFirstAttempt() {
            Main.registeredUsername = "ky_1";
            Main.registeredPassword = "Aa1!aaaa";

            // No Scanner input should be needed since the first attempt matches.
            provideInput("");

            assertTrue(Main.loginUser("ky_1", "Aa1!aaaa"));
        }

        @Test
        @DisplayName("re-prompts on incorrect credentials until correct ones are entered")
        void loginRetriesUntilSuccess() {
            Main.registeredUsername = "ky_1";
            Main.registeredPassword = "Aa1!aaaa";

            // First attempt (passed as arguments) is wrong; the method will then
            // read a corrected username/password pair from the simulated input.
            provideInput("ky_1\nAa1!aaaa\n");

            assertTrue(Main.loginUser("wrong_user", "wrongPass1!"));
        }
    }

    // ------------------------------------------------------------------
    // registerUser
    // ------------------------------------------------------------------
    @Nested
    @DisplayName("registerUser")
    class RegisterUserTests {

        @Test
        @DisplayName("registers successfully and stores details when all input is valid on the first pass")
        void registersSuccessfullyWithValidInput() {
            String simulatedInput = String.join("\n",
                    "Thabo",            // first name
                    "Nkosi",            // last name
                    "ky_1",             // username (valid)
                    "Aa1!aaaa",         // password (valid)
                    "+27821234567"      // cellphone (valid)
            ) + "\n";
            provideInput(simulatedInput);

            String result = Main.registerUser();

            assertEquals("User registered successfully", result);
            assertEquals("Thabo", Main.firstName);
            assertEquals("Nkosi", Main.lastName);
            assertEquals("ky_1", Main.registeredUsername);
            assertEquals("Aa1!aaaa", Main.registeredPassword);
            assertEquals("+27821234567", Main.registeredCellPhone);
        }

        @Test
        @DisplayName("re-prompts for each field until a valid value is supplied")
        void registersSuccessfullyAfterInvalidAttempts() {
            String simulatedInput = String.join("\n",
                    "Thabo",             // first name
                    "Nkosi",             // last name
                    "kyle_smith",        // username - invalid (too long)
                    "ky_1",              // username - valid
                    "weakpass",          // password - invalid (no upper/digit/special)
                    "Aa1!aaaa",          // password - valid
                    "0821234567",        // cellphone - invalid (no +27)
                    "+27821234567"       // cellphone - valid
            ) + "\n";
            provideInput(simulatedInput);

            String result = Main.registerUser();

            assertEquals("User registered successfully", result);
            assertEquals("ky_1", Main.registeredUsername);
            assertEquals("Aa1!aaaa", Main.registeredPassword);
            assertEquals("+27821234567", Main.registeredCellPhone);
        }
    }
}
