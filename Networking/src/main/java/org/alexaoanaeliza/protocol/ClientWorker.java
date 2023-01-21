package org.alexaoanaeliza.protocol;

import org.alexaoanaeliza.DebitCard;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.enums.Country;
import org.alexaoanaeliza.enums.DebitCardType;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.ServerException;
import org.alexaoanaeliza.exception.ServiceException;
import org.alexaoanaeliza.protocol.request.*;
import org.alexaoanaeliza.protocol.response.*;
import org.alexaoanaeliza.service.ServiceInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;

public class ClientWorker implements Runnable {
    private final ServiceInterface server;
    private final Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientWorker(ServiceInterface server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException ioException) {
            System.exit(1);
        }
    }

    public void run() {
        while(connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            }

            catch (IOException | ClassNotFoundException exception) {
                System.exit(1);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                System.exit(1);
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException ioException) {
            System.exit(1);
        }
    }

    private Response handleRequest(Request request) {
        if (request instanceof LoginUserRequest loginUserRequest) {
            List<String> userInfo = loginUserRequest.getUsersLogin();
            String email = userInfo.get(0);
            String cnp = userInfo.get(1);
            try {
                User connectedUser = server.loginUser(email, cnp);
                return new LoginUserResponse(connectedUser);
            } catch (ServerException | ServiceException | DatabaseException exception) {
                connected = false;
                return new ErrorResponse(exception.getMessage());
            }
        }

        if (request instanceof AddUserRequest addUserRequest) {
            String firstname = addUserRequest.getFirstName();
            String lastName = addUserRequest.getLastName();
            String email = addUserRequest.getEmail();
            String password = addUserRequest.getPassword();
            String phoneNumber = addUserRequest.getPhoneNumber();
            String personalNumber = addUserRequest.getPersonalNumber();
            LocalDate birthday = addUserRequest.getBirthday();
            Country country = addUserRequest.getCountry();
            String county = addUserRequest.getCounty();
            String city = addUserRequest.getCity();
            String street = addUserRequest.getStreet();
            String number = addUserRequest.getNumber();
            String apartment = addUserRequest.getApartment();

            try {
                if (server.getUserByEmail(email) == null) {
                    server.addUser(firstname, lastName, email, password, phoneNumber, personalNumber, birthday,
                            country, county, city, street, number, apartment);
                    return new AddUserResponse();
                }
                else
                    return new ErrorResponse("There is an account opened for this email address");
            } catch (ServerException | ServiceException | DatabaseException exception) {
                return new ErrorResponse(exception.getMessage());
            }
        }

        if (request instanceof GetUserByEmailRequest getUserByEmailRequest) {
            String email = getUserByEmailRequest.getEmail();

            try {
                server.getUserByEmail(email);
                return new GetUserByEmailResponse(null);
            } catch (DatabaseException databaseException) {
                return new ErrorResponse(databaseException.getMessage());
            }
        }

        if (request instanceof GetDebitCardByDataRequest getDebitCardByDataRequest) {
            String cardNumber = getDebitCardByDataRequest.getCardNumber();
            String cvv = getDebitCardByDataRequest.getCvv();
            LocalDate expireDate = getDebitCardByDataRequest.getExpireDate();
            DebitCardType debitCardType = getDebitCardByDataRequest.getDebitCardType();

            try {
                DebitCard debitCard = server.getDebitCardByData(cardNumber, cvv, expireDate, debitCardType);
                return new GetDebitCardByDataResponse(debitCard);
            } catch (DatabaseException databaseException) {
                return new ErrorResponse(databaseException.getMessage());
            }
        }

        if (request instanceof AddDebitCardRequest addDebitCardRequest) {
            String cardNumber = addDebitCardRequest.getCardNumber();
            String cvv = addDebitCardRequest.getCvv();
            LocalDate expireDate = addDebitCardRequest.getExpireDate();
            DebitCardType debitCardType = addDebitCardRequest.getDebitCardType();
            User owner = addDebitCardRequest.getOwner();

            try {
                DebitCard debitCard = server.addDebitCard(cardNumber, cvv, expireDate, debitCardType, owner);
                return new AddDebitCardResponse(debitCard);
            } catch (DatabaseException databaseException) {
                return new ErrorResponse(databaseException.getMessage());
            }
        }

        if (request instanceof GetDebitCardByIdRequest getDebitCardByIdRequest) {
            Long id = getDebitCardByIdRequest.getId();

            try {
                DebitCard debitCard = server.getDebitCardById(id);
                return new GetDebitCardByIdResponse(debitCard);
            } catch (DatabaseException databaseException) {
                return new ErrorResponse(databaseException.getMessage());
            }
        }

        if (request instanceof DepositAmountRequest depositAmountRequest) {
            DebitCard debitCard = depositAmountRequest.getDebitCard();
            Double amount = depositAmountRequest.getSum();

            try {
                server.depositAmount(amount, debitCard);
                return new DepositAmountResponse();
            } catch (DatabaseException databaseException) {
                return new ErrorResponse(databaseException.getMessage());
            }
        }

        return null;
    }

    private void sendResponse(Response response) throws IOException{
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }
}