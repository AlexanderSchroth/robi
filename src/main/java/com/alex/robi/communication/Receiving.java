package com.alex.robi.communication;

import java.io.IOException;

public interface Receiving {

    void start();

    void stop() throws IOException;

    ResponseWaiter waitFor(Command c);
}
