package org.alexaoanaeliza.serverUtils;

import java.net.Socket;

public abstract class AbstractConcurrentServer extends AbstractServer {
    public AbstractConcurrentServer(Integer port) {
        super(port);
    }

    @Override
    protected void processRequest(Socket client) {
        Thread workerThread = createWorker(client);
        workerThread.start();
    }

    protected abstract Thread createWorker(Socket client);
}