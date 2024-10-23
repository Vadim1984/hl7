package org.example.hl7.messages;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.group.OUL_R21_CONTAINER;
import ca.uhn.hl7v2.model.v24.group.OUL_R21_OBSERVATION;
import ca.uhn.hl7v2.model.v24.group.OUL_R21_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v24.group.OUL_R21_PATIENT;
import ca.uhn.hl7v2.model.v24.message.OUL_R21;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.NTE;
import ca.uhn.hl7v2.model.v24.segment.OBR;
import ca.uhn.hl7v2.model.v24.segment.OBX;
import ca.uhn.hl7v2.model.v24.segment.ORC;
import ca.uhn.hl7v2.model.v24.segment.SAC;
import ca.uhn.hl7v2.model.v24.segment.TCD;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.example.hl7.util.Hl7MessageUtility.populateMshSegment;
import static org.example.hl7.util.Hl7MessageUtility.populateObx;
import static org.example.hl7.util.Hl7MessageUtility.populateOrc;
import static org.example.hl7.util.Hl7MessageUtility.populatePatientInfoNotesAndComments;
import static org.example.hl7.util.Hl7MessageUtility.populatePidSegment;
import static org.example.hl7.util.Hl7MessageUtility.populateSac;
import static org.example.hl7.util.Hl7MessageUtility.populateObr;

public class PatientResultMessageConverter {
    private static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public Message createMessage() {

/*        Terser t = new Terser(theMessage);
        ApplicationRouter.AppRoutingData msgData =
                new AppRoutingDataImpl(t.get("/MSH-9-1"), t.get("/MSH-9-2"), t.get("/MSH-11-1"), t.get("/MSH-12"));*/
        try {
            String tube = "123456789";
            String specimen = "US-013242355";
            String dateNow = LocalDateTime.now().format(YYYY_MM_DD_HH_MM_SS_FORMAT);
            OUL_R21 patientResultMessage = new OUL_R21();
            patientResultMessage.initQuickstart("OUL", "R21", "P");

            // Populate the MSH Segment
            MSH mshSegment = patientResultMessage.getMSH();
            String uuidString = String.valueOf(UUID.randomUUID());
            populateMshSegment(mshSegment, uuidString,
                    "REMCS Sapio", "Liquid - ctDNA Normalization.",
                    "cobas infinity 3.0.0", "Roche Diagnostics",
                    dateNow, "ER", "ER");

            // get Patient info
            OUL_R21_PATIENT patientInfo = patientResultMessage.getPATIENT();
            // Populate the PID Segment
            populatePidSegment(patientInfo.getPID(), "John", "Doe", "123456");
            populatePatientInfoNotesAndComments(patientInfo.getNTE(), "1", "L", "Patient Comment");

            // get Order observation
            OUL_R21_ORDER_OBSERVATION orderObservation = patientResultMessage.getORDER_OBSERVATION();
            OUL_R21_CONTAINER oulR21Container = orderObservation.getCONTAINER();
            // SAC
            SAC sac = oulR21Container.getSAC();
            populateSac(sac, tube, "DNAE", "A", "1");
            // ORC
            ORC orc = orderObservation.getORC();
            populateOrc(orc, tube, "R", dateNow, "0", "Boston");
            // OBR
            OBR obr = orderObservation.getOBR();
            populateObr(obr, "1", tube, "ALL", "R", dateNow, specimen,"P");
            // ==== OBX START ====
            OUL_R21_OBSERVATION observation = orderObservation.getOBSERVATION(0);
            OBX obx0 = observation.getOBX();
            populateObx(obx0, "1", "NM", "e12000", "Cobas e flow test",
                    "35", "", "0", "F",
                    "20181113121327", "..DM-DM-0-0", "SYSTEM",
                    "..DM-DM-0-0");
            TCD tcd0 = observation.getTCD();
            tcd0.getUniversalServiceIdentifier().parse("e12000");
            NTE nte = observation.getNTE();
            populatePatientInfoNotesAndComments(nte, "1", "L", "Main Test comment");
            // ==== OBX END ====

            // Print
            System.out.println("======================================================");
            //System.out.println(patientResultMessage.printStructure());
            System.out.println("======================================================");

            return patientResultMessage;
        } catch (DataTypeException ex) {
            throw new IllegalArgumentException(ex);
        } catch (HL7Exception | IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
