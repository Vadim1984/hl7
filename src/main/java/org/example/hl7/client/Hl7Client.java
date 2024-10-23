package org.example.hl7.client;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import lombok.AllArgsConstructor;
import org.example.hl7.exception.HL7RuntimeException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class Hl7Client {
    private String serverHost;
    private int port;
    private boolean useTls;

    public String sendMessage(final String stringMessage) {
        try (HapiContext context = new DefaultHapiContext()) {
            Parser pipeParser = context.getPipeParser();
            Message message = pipeParser.parse(stringMessage);
            Message response = sendMessage(context, message);

            return pipeParser.encode(response);
        } catch (HL7Exception | IOException ex) {
            throw new HL7RuntimeException("Error sending HL7 message.", ex);
        }
    }

    public Message sendMessage(final Message message) {
        try (HapiContext context = new DefaultHapiContext()) {
            return sendMessage(context, message);
        } catch (IOException ex) {
            //log.error("Error sending HL7 message.", ex);
            throw new HL7RuntimeException("Error sending HL7 message.", ex);
        }
    }

    protected Message sendMessage(final HapiContext context, final Message message) {
        try (Connection connection = context.newClient(serverHost, port, useTls)) {
            Initiator initiator = connection.getInitiator();
            //initiator.setTimeout(1000000, TimeUnit.MILLISECONDS);
            return initiator.sendAndReceive(message);
        } catch (HL7Exception | LLPException | IOException ex) {
            //log.error("Error sending HL7 message.", ex);
            throw new HL7RuntimeException("Error sending HL7 message.", ex);
        }
    }
}
