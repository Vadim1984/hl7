package org.example.hl7.client;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Hl7Client {
    private HapiContext context;
    /**
     * A connection object represents a socket attached to an HL7 server
     */
    private Connection connection;

    public Hl7Client(String serverHost, int port, boolean useTls) {
        try {
            this.context = new DefaultHapiContext();
            this.connection = context.newClient(serverHost, port, useTls);
        } catch (HL7Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    public String sendMessage(final String stringMessage) throws Exception {
        Parser pipeParser = context.getPipeParser();
        Message message = pipeParser.parse(stringMessage);
        // connection creates a client, which will connect to waiting server and send messages to it.
        // The initiator is used to transmit unsolicited messages
        Initiator initiator = connection.getInitiator();
        Message response = initiator.sendAndReceive(message);

        /*
         * MSH|^~\&|||||20070218200627.515-0500||ACK|54|P|2.2 MSA|AA|12345
         */

        /*
         * If you want to send another message to the same destination, it's fine to ask the context again for a client to attach to the same host/port.
         * The context will be smart about it and return the same (already connected) client Connection instance, assuming it hasn't been closed.
         *
         *         connection = context.newClient("localhost", port, useTls);
         *         initiator = connection.getInitiator();
         *         response = initiator.sendAndReceive(message);
         */

        return pipeParser.encode(response);
    }

    /*
     * Close the connection when you are done with it. If you are designing a system which will continuously send out messages, you may want to
     * consider not closing the connection until you have no more messages to send out. This is more efficient, as most (if not all) HL7 receiving
     * applications are capable of receiving lots of messages in a row over the same connection, even with a long delay between messages.
     *
     * See http://hl7api.sourceforge.net/xref/ca/uhn/hl7v2/examples/SendLotsOfMessages.html for an example of this.
     */
    public void closeConnection() {
        connection.close();
    }
}
