package org.alexaoanaeliza;

import org.alexaoanaeliza.exception.FileException;
import org.alexaoanaeliza.exception.ServerException;
import org.alexaoanaeliza.server.Service;
import org.alexaoanaeliza.serverUtils.AbstractServer;
import org.alexaoanaeliza.serverUtils.ConcurrentServer;

import java.io.IOException;
import java.util.Properties;

public class StartServer {
    public static void main(String[] args) {
        Properties properties = new Properties();

        try {
            properties.load(StartServer.class.getResourceAsStream("/config.properties"));
        } catch (IOException ioException) {
            throw new FileException(ioException.getMessage());
        }

        Service service = Service.getInstance();

        try {
            int serverPort = 55555;
            try {
                serverPort = Integer.parseInt(properties.getProperty("port"));
            } catch (NumberFormatException ignored) {
            }
            AbstractServer server = new ConcurrentServer(serverPort, service);
            try {
                server.start();
            } catch (ServerException ignored) {
            } finally {
                try {
                    server.stop();
                } catch (ServerException ignored) {
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            close();
        }
    }

    static void close() {

    }
}