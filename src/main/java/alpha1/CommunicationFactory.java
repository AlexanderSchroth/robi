package alpha1;

import alpha1.communication.AlphaCommunication;
import alpha1.communication.AlphaReciving;
import alpha1.communication.AlphaSending;
import alpha1.communication.Communication;
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
