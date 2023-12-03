package be.howest.ti.hangman.util.exceptions;

public class HangmanException extends RuntimeException {

        public HangmanException(String message) {
            super(message);
        }

        public HangmanException(String message, Throwable cause) {
            super(message, cause);
        }
}
