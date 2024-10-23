package org.example.hl7.client;

import ca.uhn.hl7v2.hoh.api.DecodeException;
import ca.uhn.hl7v2.hoh.api.EncodeException;
import ca.uhn.hl7v2.hoh.api.IAuthorizationClientCallback;
import ca.uhn.hl7v2.hoh.api.IReceivable;
import ca.uhn.hl7v2.hoh.api.ISendable;
import ca.uhn.hl7v2.hoh.api.MessageMetadataKeys;
import ca.uhn.hl7v2.hoh.auth.SingleCredentialClientCallback;
import ca.uhn.hl7v2.hoh.raw.api.RawSendable;
import ca.uhn.hl7v2.hoh.raw.client.HohRawClientSimple;

import java.io.IOException;
import java.util.Optional;

public class Hl7HttpClient {
    private final String host;
    private final int port;
    private final String uri;

    public Hl7HttpClient(String host, int port, String uri) {
        this.host = host;
        this.port = port;
        this.uri = uri;
    }

    public void send(String message) {
        HohRawClientSimple client = new HohRawClientSimple(host, port, uri);
        // Optionally, if credentials should be sent, they can be provided using a credential callback
        IAuthorizationClientCallback authCalback = new SingleCredentialClientCallback("ausername", "somepassword");
        //client.setAuthorizationCallback(authCalback);

        // The ISendable defines the object that provides the actual message to send
        ISendable<String> sendable = new RawSendable(message);

        try {
            // sendAndReceive actually sends the message
            IReceivable<String> receivable = client.sendAndReceive(sendable);

            // receivavle.getRawMessage() provides the response
            System.out.println("Response was:\n" + receivable.getMessage());

            // IReceivable also stores metadata about the message
            String remoteHostIp = Optional.ofNullable(receivable.getMetadata())
                    .map(map -> map.get(MessageMetadataKeys.REMOTE_HOST_ADDRESS))
                    .map(Object::toString)
                    .orElse("");
            System.out.println("From:\n" + remoteHostIp);

        } catch (DecodeException | EncodeException | IOException e) {
            // Thrown if the response can't be read
            e.printStackTrace();
        }
    }
}
