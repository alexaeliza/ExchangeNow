package org.alexaoanaeliza.serverUtils;

import org.alexaoanaeliza.protocol.ClientWorker;
import org.alexaoanaeliza.service.ServiceInterface;

import java.net.Socket;

public class ConcurrentServer extends AbstractConcurrentServer {
    private final ServiceInterface server;

    public ConcurrentServer(Integer port, ServiceInterface server) {
        super(port);
        this.server = server;
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientWorker worker = new ClientWorker(server, client);
        return new Thread(worker);
    }

    @Override
    public void stop(){
    }
}