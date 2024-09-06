package org.example.hl7;

import ca.uhn.hl7v2.app.HL7Service;
import org.example.hl7.server.HL7Server;

import java.util.Scanner;

public class Hl7StartServer {
    public static void main(String[] args) throws Exception {
        int port = 1011;
        boolean useTls = false;

        HL7Service hl7Service = HL7Server.createAndStart(port, useTls);

        Scanner scan = new Scanner(System.in);
        String userInput;

        while (true) {
            System.out.println("Do you want to stop server? [y/n]");
            userInput = scan.nextLine();

            if ("y".equalsIgnoreCase(userInput)) {
                // Stop the receiving server and client
                hl7Service.stopAndWait();
                break;
            }
        }
    }
}
