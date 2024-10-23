package org.example.hl7.server;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.HL7Service;
import ca.uhn.hl7v2.protocol.ReceivingApplication;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HL7Server {
    public static HL7Service createAndStart(int port, boolean useTls) throws Exception {
        /*
         * create a server to listen for incoming messages.
         * The following section of code establishes a server listening on port 1011 for new connections.
         */
        HapiContext context = new DefaultHapiContext();
        HL7Service server = context.newServer(port, useTls);

        ReceivingApplication messageHandler = new Hl7MessageHandler();
        //single application to handle all messages
        server.registerApplication("*", "*", messageHandler);
        /*
         * The server may have any number of "application" objects registered to handle messages.
         * We are going to create an application to listen to ADT^A01 and ADT^A02 messages.
         *
         *    server.registerApplication("ADT", "A01", handler1);
         *    server.registerApplication("ADT", "A02", handler2);
         */

        /*
         * If you want to be notified any time a new connection comes in or is
         * lost, you might also want to register a connection listener (see the
         * bottom of this class to see what the listener looks like). It's fine
         * to skip this step.
         */
        server.registerConnectionListener(new Hl7ConnectionListener());

        /*
         * If you want to be notified any processing failures when receiving, processing, or responding to messages with the server,
         * you can also register an exception handler.
         */
        server.setExceptionHandler(new Hl7ExceptionHandler());

        // Start the server listening for messages
        server.startAndWait();
        /*
         * Note: if you don't want to wait for the server to initialize itself, it
         * can start in the background: server.start();
         */

        return server;
    }
}
