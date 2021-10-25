package com.alex.robi.communication;

import java.io.IOException;

public interface Receiving {

    void close() throws IOException;

    ResponseWaiter waitFor(Command c);
}
