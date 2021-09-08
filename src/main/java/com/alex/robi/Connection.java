package com.alex.robi;

import java.io.IOException;

public interface Connection {

    void send(int[] message) throws IOException;

}
