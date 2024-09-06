package org.example.hl7.server;

import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.ConnectionListener;

public class Hl7ConnectionListener implements ConnectionListener {

    public void connectionReceived(Connection theC) {
        System.out.println("New connection received: " + theC.getRemoteAddress().toString());
    }

    public void connectionDiscarded(Connection theC) {
        System.out.println("Lost connection from: " + theC.getRemoteAddress().toString());
    }

}
