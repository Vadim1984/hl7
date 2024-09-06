package org.example.hl7.exception;

public class HL7RuntimeException extends RuntimeException {

    public HL7RuntimeException(Throwable cause) {
        super(cause);
    }

    public HL7RuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
