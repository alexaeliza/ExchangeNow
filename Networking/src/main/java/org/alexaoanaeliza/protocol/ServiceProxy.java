package org.alexaoanaeliza.protocol;

import org.alexaoanaeliza.User;
import org.alexaoanaeliza.enums.Country;
import org.alexaoanaeliza.exception.ServerException;
import org.alexaoanaeliza.protocol.request.AddUserRequest;
import org.alexaoanaeliza.protocol.request.LoginUserRequest;
import org.alexaoanaeliza.protocol.request.Request;
import org.alexaoanaeliza.protocol.response.ErrorResponse;
import org.alexaoanaeliza.protocol.response.LoginUserResponse;
import org.alexaoanaeliza.protocol.response.Response;
import org.alexaoanaeliza.protocol.response.UpdateResponse;
import org.alexaoanaeliza.service.ServiceInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServiceProxy implements ServiceInterface {
    private final String host;
    private final int port;
    private ClientWorker client;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket socket;
    private final BlockingQueue<Response> responses;
    private volatile boolean finished;

    public ServiceProxy(String host, int port) {
        this.host = host;
        this.port = port;
        responses = new LinkedBlockingQueue<>();
    }

    private void closeConnection() {
        finished = true;
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
            client = null;
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
            System.exit(1);
        }
    }

    private void sendRequest(Request request) throws ServerException {
        try {
            outputStream.writeObject(request);
            outputStream.flush();
        } catch (IOException ioException) {
            throw new ServerException(ioException.getMessage());
        }
    }

    private Response readResponse() throws ServerException {
        Response response;
        try {
            response = responses.take();
        } catch (InterruptedException interruptedException) {
            throw new ServerException(interruptedException.getMessage());
        }
        return response;
    }

    private void initializeConnection() throws ServerException {
        try {
            socket = new Socket(host, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
            finished = false;
            startReader();
        } catch (IOException ioException) {
            System.exit(1);
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(UpdateResponse update) {

    }

    @Override
    public User loginUser(String username, String cnp) {
        initializeConnection();
        User connectedUser = null;
        List<String> info = new ArrayList<>();
        info.add(username);
        info.add(cnp);
        sendRequest(new LoginUserRequest(info));
        Response response = readResponse();
        if (response instanceof LoginUserResponse)
            connectedUser = ((LoginUserResponse) response).getUser();
        if (response instanceof ErrorResponse errorResponse) {
            closeConnection();
            throw new ServerException(errorResponse.getMessage());
        }
        return connectedUser;
    }

    @Override
    public void addUser(String firstName, String lastName, String email, String password, String phoneNumber, String personalNumber, LocalDate birthday, Country country, String county, String city, String street, String number, String apartment) {
        initializeConnection();
        sendRequest(new AddUserRequest(firstName, lastName, email, password, phoneNumber, personalNumber, birthday, country, county, city, street, number, apartment));
        Response response = readResponse();
        if (response instanceof ErrorResponse errorResponse)
            throw new ServerException(errorResponse.getMessage());
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = inputStream.readObject();
                    if (response instanceof UpdateResponse) {
                        handleUpdate((UpdateResponse) response);
                    } else {
                        try {
                            responses.put((Response) response);
                        } catch (InterruptedException ignored) {
                        }
                    }
                } catch (IOException | ClassNotFoundException ignored) {
                }
            }
        }
    }
}