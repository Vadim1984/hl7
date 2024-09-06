package org.example.hl7;

import ca.uhn.hl7v2.app.HL7Service;
import org.example.hl7.client.Hl7Client;
import org.example.hl7.server.HL7Server;

public class Main {
    public static void main(String[] args) throws Exception {

        // ----  CONNECTION PROPERTIES ------
        String serverHost = "localhost";
        int port = 1011;
        boolean useTls = false;

        // ----  START SERVER ------
        HL7Service hl7Service = HL7Server.createAndStart(port, useTls);


        // ----  CREATE AND SEND MESSAGES ------
        Hl7Client hl7Client = new Hl7Client(serverHost, port, useTls);

/*        String ackCommunication = "MSH|^~\\&|tetris infinity-2.1|Best Diagnostics|Receiver Application|Receiver Facility|20170314141221||ACK^O21^ACK|3B2EC5DE-34D3-4B82-B526-59F4B4871948|P|2.4|||NE|NE||8859/1\r"
                + "MSA|CA|248206111";
        String responseString1 = hl7Client.sendMessage(ackCommunication);
        System.out.println("Received response2:\n" + responseString1);*/

        String ackApplication = "MSH|^~\\&|tetris infinity 2.1|Best Diagnostics|Receiver Application|Receiver Facility|20170308154410||ORL^O22|5BEB9A7F-7DBB-4CC0-9A57-E2682DC55FA3|P|2.4|||AL|ER\r"
                + "MSA|AR\r"
                + "ERR|||42201|E|||Order ID rejected. The action has been disabled.";
        String responseString2 = hl7Client.sendMessage(ackApplication);
        System.out.println("Received response1:\n" + responseString2);


        // ----  CLOSE CLIENT'S CONNECTION ------
        hl7Client.closeConnection();


        // ----  STOP SERVER ------
        hl7Service.stopAndWait();
    }
}
