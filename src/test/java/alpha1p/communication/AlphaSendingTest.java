package alpha1p.communication;

import static org.hamcrest.CoreMatchers.equalToObject;
import static org.hamcrest.MatcherAssert.assertThat;

import alpha1p.communication.AlphaSending;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AlphaSendingTest {

    @Test
    void send() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        AlphaSending alphaSending = new AlphaSending(outputStream);

        alphaSending.send(new int[] { 1, 2 });

        assertThat(outputStream.toByteArray(), equalToObject(new byte[] { 1, 2 }));
    }

    @Test
    void close() throws IOException {
        OutputStream mock = Mockito.mock(OutputStream.class);

        new AlphaSending(mock).close();

        Mockito.verify(mock).close();
    }

}
