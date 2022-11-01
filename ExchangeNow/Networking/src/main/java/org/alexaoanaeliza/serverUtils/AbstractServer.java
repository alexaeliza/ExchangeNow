package org.alexaoanaeliza.serverUtils;

import org.alexaoanaeliza.exception.ServerException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer {
    private final Integer port;
    private ServerSocket serverSocket = null;

    public AbstractServer(Integer port) {
        this.port = port;
    }

    protected abstract void processRequest(Socket client);

    public void start() throws ServerException {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                processRequest(clientSocket);
            }
        } catch (IOException ioException) {
            throw new ServerException(ioException.getMessage());
        } finally {
            stop();
        }
    }

    public void stop() throws ServerException{
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            throw new ServerException(ioException.getMessage());
        }
    }
}