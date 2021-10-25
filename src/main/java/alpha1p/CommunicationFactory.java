package alpha1p;

import alpha1p.communication.AlphaCommunication;
import alpha1p.communication.AlphaReciving;
import alpha1p.communication.AlphaSending;
import alpha1p.communication.Communication;
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
