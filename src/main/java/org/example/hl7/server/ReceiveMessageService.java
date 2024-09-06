package org.example.hl7.server;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.protocol.ReceivingApplication;

import java.io.IOException;
import java.util.Map;

public class ReceiveMessageService implements ReceivingApplication {

    @Override
    public Message processMessage(Message message, Map<String, Object> metaData) throws HL7Exception {
        try (DefaultHapiContext defaultHapiContext = new DefaultHapiContext()) {
            String encodedMessage = defaultHapiContext.getPipeParser().encode(message);
            //System.out.println("Received message:\n" + encodedMessage + "\n\n");
            // Now generate a simple acknowledgment message and return it
            return message.generateACK();
        } catch (IOException e) {
            throw new HL7Exception(e);
        }
    }

    @Override
    public boolean canProcess(Message message) {
        System.out.println("received message: " + message);
        return true;
    }
}
