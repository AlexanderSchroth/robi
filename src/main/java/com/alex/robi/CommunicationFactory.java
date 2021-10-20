package com.alex.robi;

import com.alex.robi.communication.AlphaCommunication;
import com.alex.robi.communication.AlphaReciving;
import com.alex.robi.communication.AlphaSending;
import com.alex.robi.communication.Communication;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class CommunicationFactory {

    public static Communication create(String alphaId) throws IOException {
        StreamConnection streamConnection = (StreamConnection) Connector.open("btspp://" + alphaId + ":6");
        return new AlphaCommunication(
            new AlphaSending(streamConnection.openOutputStream()),
            new AlphaReciving(streamConnection.openInputStream()));
    }
}
