package com.robi.alpha1p.api.communication;

import java.io.IOException;

public interface Receiving {

    void close() throws IOException;

    ResponseWaiter waitFor(Command c);
}
