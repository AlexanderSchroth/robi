package alpha1.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class ResponseWaiter {
    private Command command;
    private BlockingQueue<List<Message>> q;

    private List<Message> messages;

    ResponseWaiter(Command command) {
        this.command = command;
        this.messages = new ArrayList<>();
        this.q = new ArrayBlockingQueue<List<Message>>(1);
    }

    List<Message> take() throws InterruptedException {
        return q.take();
    }

    void add(Message m) {
        messages.add(m);
        if (complete()) {
            q.add(messages);
        }
    }
    
    boolean complete() {
        return command.expectedResponseMessages() == messages.size();
    }
}