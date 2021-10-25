package alpha1.communication;

import static alpha1.communication.Parameter.of;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import alpha1.communication.Command;
import alpha1.communication.Message;
import alpha1.communication.Parameter;
import alpha1.communication.Payload;
import org.junit.jupiter.api.Test;

public class PayloadTest {

    @Test
    void toMessage() {
        Message messageFromPayload = Payload.payload(Command.BTHandshake, Parameter.of(1)).toMessage();

        Message expectedMessage = new Message.Builder()
            .withCommandHeader1(of(251))
            .withCommandHeader2(of(191))
            .withLength(of(6))
            .withCommand(of(1))
            .withParameters(of(1))
            .withCheck(of(8))
            .withEndCharacter(of(237))
            .build();

        assertThat(messageFromPayload, equalTo(expectedMessage));
    }

    @Test
    void testToString() {
        Payload payload = Payload.payload(Command.BTHandshake, Parameter.of(1));

        assertThat(payload.toString(), equalTo("{\r\n  \"command\" : \"BTHandshake\",\r\n  \"parameters\" : \"[ \\\"0x1, (int)1\\\" ]\"\r\n}"));
    }

}
