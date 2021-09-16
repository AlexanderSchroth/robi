package com.alex.robi.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ResponseWaiter {
    private Command command;
    private BlockingQueue<List<Message>> q;

    private List<Message> messages;

    ResponseWaiter(Command command) {
        this.command = command;
        this.messages = new ArrayList<>();
        this.q = new ArrayBlockingQueue<List<Message>>(1);
    }

    public List<Message> take() throws InterruptedException {
        return q.take();
    }

    public boolean add(Message m) {
        messages.add(m);
        if (command.expectedResponseMessages() == messages.size()) {
            q.add(messages);
            return true;
        } else {
            return false;
        }
    }
}