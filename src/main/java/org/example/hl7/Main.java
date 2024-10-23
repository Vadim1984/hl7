package org.example.hl7;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import org.example.hl7.client.Hl7Client;
import org.example.hl7.client.Hl7HttpClient;
import org.example.hl7.messages.PatientResultMessageConverter;

public class Main {
    public static void main(String[] args) throws Exception {

        // ----  CONNECTION PROPERTIES ------
        String serverHost = "localhost";
        int port = 1011;
        boolean useTls = false;

        // ----  START SERVER ------
        //HL7Service hl7Service = HL7Server.createAndStart(port, useTls);


        // ----  CREATE AND SEND MESSAGES ------
        /*
          String ackCommunication = "MSH|^~\\&|tetris infinity-2.1|Best Diagnostics|Receiver Application|Receiver Facility|20170314141221||ACK^O21^ACK|3B2EC5DE-34D3-4B82-B526-59F4B4871948|P|2.4|||NE|NE||8859/1\r"
                + "MSA|CA|3B2EC5DE-34D3-4B82-B526-59F4B4871948";
        */

        String ackApplication = "MSH|^~\\&|tetris infinity 2.1|Best Diagnostics|Receiver Application|Receiver Facility|20170308154410||ORL^O22|5BEB9A7F-7DBB-4CC0-9A57-E2682DC55FA3|P|2.4|||AL|ER\r"
                + "MSA|AR\r"
                + "ERR|||42201|E|||Order ID rejected. The action has been disabled.";
        //sendThroughWebSocket(ackApplication);
        //sendThroughHttp(ackApplication);

/*        PatientResultMessageConverter patientResultMessageConverter = new PatientResultMessageConverter();
        String patientResultMessage = patientResultMessageConverter.createMessage();
        String responseString3 = hl7Client.sendMessage(patientResultMessage);
        System.out.println("Received patient message response:\n" + responseString3);*/



        // ----  STOP SERVER ------
        //hl7Service.stopAndWait();

        PatientResultMessageConverter patientResultMessageConverter = new PatientResultMessageConverter();
        Message message = patientResultMessageConverter.createMessage();
        System.out.println(message.encode().replace("\r", "\r\n"));
        //sendThroughWebSocket(message);
        String testMessage =
                  "MSH|^\\&~|IM|IM|PSM|PSM|20241022114552||OML^O21^2OML_O21|248206106|P|2.4|1||AL|ER||8859/1\r"
                + "PID|1|6004|Pat_1||Doe^John||19801018|F PV1|||ORI1^Origin 1|||||||SERV1^Service 1|\r"
                + "SAC|||6004|\r"
                + "ORC|NW|6004|||||^^^^^R|||||DOCT1^Doctor 1|^^^0^^^^^^Location 1|\r "
                + "OBR|1|1000||CEA|||20170105100000||||A||||US-013242355^^^^^^P\r"
                + "OBR|2|1001||CYFRA|||20170105100000||||A||||US-013242355^^^^^^P";

        //sendThroughWebSocket(testMessage);
    }

    private static void sendThroughWebSocket(String message) {
        Hl7Client hl7Client = new Hl7Client("10.2.12.94", 20861, false);
        String responseString2 = hl7Client.sendMessage(message);
        System.out.println("Received response1:\n" + responseString2);
        //hl7Client.closeConnection();
    }

    private static void sendThroughWebSocket(Message message) throws HL7Exception {
        Hl7Client hl7Client = new Hl7Client("localhost", 1011, false);
        Message response = hl7Client.sendMessage(message);
        System.out.println("Received response1:\n" + response.encode());
        //hl7Client.closeConnection();
    }

    private static void sendThroughHttp(String message) {
        Hl7HttpClient hl7HttpClient = new Hl7HttpClient("localhost", 8088, "/cobas/test");
        hl7HttpClient.send(message);
    }
}
