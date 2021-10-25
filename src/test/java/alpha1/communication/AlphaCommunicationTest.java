package alpha1.communication;

import static alpha1.communication.Payload.payload;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import alpha1.communication.AlphaCommunication;
import alpha1.communication.Command;
import alpha1.communication.Payload;
import alpha1.communication.Receiving;
import alpha1.communication.ResponseWaiter;
import alpha1.communication.Sending;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AlphaCommunicationTest {

    @Test
    void receivesMessages() {

        ResponseWaiter responseWaiter = new ResponseWaiter(Command.BTHandshake);
        responseWaiter.add(Payload.payload(Command.BTHandshake).toMessage());

        Receiving receiving = mock(Receiving.class);
        when(receiving.waitFor(Command.BTHandshake)).thenReturn(responseWaiter);

        AlphaCommunication alphaCommunication = new AlphaCommunication(mock(Sending.class), receiving);
        List<Payload> rawReceived = alphaCommunication.send(payload(Command.BTHandshake), messages -> messages);

        assertThat(rawReceived.size(), is(1));
        assertThat(rawReceived, equalTo(asList(payload(Command.BTHandshake))));
    }

    @Test
    void sendsMessage() throws IOException {
        ResponseWaiter responseWaiter = new ResponseWaiter(Command.BTHandshake);
        responseWaiter.add(Payload.payload(Command.BTHandshake).toMessage());

        Receiving receiving = mock(Receiving.class);
        when(receiving.waitFor(Command.BTHandshake)).thenReturn(responseWaiter);

        Sending sending = mock(Sending.class);

        AlphaCommunication alphaCommunication = new AlphaCommunication(sending, receiving);
        alphaCommunication.send(payload(Command.BTHandshake), messages -> null);

        verify(sending).send(Mockito.eq(new int[] { 251, 191, 6, 1, 0, 7, 237 }));
    }
}
