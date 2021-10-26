package com.robi.alpha1p.api;

import com.robi.alpha1p.api.communication.AlphaCommunication;
import com.robi.alpha1p.api.communication.AlphaReciving;
import com.robi.alpha1p.api.communication.AlphaSending;
import com.robi.alpha1p.api.communication.Communication;
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
