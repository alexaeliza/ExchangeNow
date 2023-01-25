package org.alexaoanaeliza.protocol;

import org.alexaoanaeliza.DebitCard;
import org.alexaoanaeliza.Stock;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.enums.Country;
import org.alexaoanaeliza.enums.DebitCardType;
import org.alexaoanaeliza.exception.ServerException;
import org.alexaoanaeliza.protocol.request.*;
import org.alexaoanaeliza.protocol.response.*;
import org.alexaoanaeliza.service.ServiceInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
        try {
            return responses.take();
        } catch (InterruptedException interruptedException) {
            throw new ServerException(interruptedException.getMessage());
        }
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

    @Override
    public User getUserByEmail(String email) {
        initializeConnection();
        sendRequest(new GetUserByEmailRequest(email));
        Response response = readResponse();
        if (response instanceof ErrorResponse errorResponse)
            throw new ServerException(errorResponse.getMessage());
        if (response instanceof GetUserByEmailResponse getUserByEmailResponse)
            return getUserByEmailResponse.getUser();
        return null;
    }

    @Override
    public void depositAmount(Double amount, DebitCard debitCard) {
        initializeConnection();
        sendRequest(new DepositAmountRequest(amount, debitCard));
        Response response = readResponse();
        if (response instanceof ErrorResponse errorResponse)
            throw new ServerException(errorResponse.getMessage());
    }

    @Override
    public void withdrawAmount(Double amount, DebitCard debitCard) {
        initializeConnection();
        sendRequest(new WithdrawAmountRequest(amount, debitCard));
        Response response = readResponse();
        if (response instanceof ErrorResponse errorResponse)
            throw new ServerException(errorResponse.getMessage());
    }

    @Override
    public DebitCard getDebitCardById(Long id) {
        initializeConnection();
        sendRequest(new GetDebitCardByIdRequest(id));
        Response response = readResponse();
        if (response instanceof ErrorResponse errorResponse)
            throw new ServerException(errorResponse.getMessage());
        if (response instanceof GetDebitCardByIdResponse getDebitCardByIdResponse)
            return getDebitCardByIdResponse.getDebitCard();
        return null;
    }

    @Override
    public DebitCard getDebitCardByData(String cardNumber, String cvv, LocalDate expireDate, DebitCardType debitCardType) {
        initializeConnection();
        sendRequest(new GetDebitCardByDataRequest(cardNumber, cvv, expireDate, debitCardType));
        Response response = readResponse();
        if (response instanceof ErrorResponse errorResponse)
            throw new ServerException(errorResponse.getMessage());
        if (response instanceof GetDebitCardByDataResponse getDebitCardByDataResponse)
            return getDebitCardByDataResponse.getDebitCard();
        return null;
    }

    @Override
    public DebitCard addDebitCard(String cardNumber, String cvv, LocalDate expireDate, DebitCardType debitCardType, User owner) {
        initializeConnection();
        sendRequest(new AddDebitCardRequest(debitCardType, cardNumber, cvv, expireDate, owner));
        Response response = readResponse();
        if (response instanceof ErrorResponse errorResponse)
            throw new ServerException(errorResponse.getMessage());
        if (response instanceof AddDebitCardResponse addDebitCardResponse)
            return addDebitCardResponse.getDebitCard();
        return null;
    }

    @Override
    public Set<Stock> getStocks() {
        initializeConnection();
        sendRequest(new GetStocksRequest());
        Response response = readResponse();
        if (response instanceof ErrorResponse errorResponse)
            throw new ServerException(errorResponse.getMessage());
        if (response instanceof GetStocksResponse getStocksResponse)
            return getStocksResponse.getStocks();
        return null;
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