package org.alexaoanaeliza.protocol;

import org.alexaoanaeliza.User;
import org.alexaoanaeliza.enums.Country;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.ServerException;
import org.alexaoanaeliza.exception.ServiceException;
import org.alexaoanaeliza.protocol.request.AddUserRequest;
import org.alexaoanaeliza.protocol.request.LoginUserRequest;
import org.alexaoanaeliza.protocol.request.Request;
import org.alexaoanaeliza.protocol.response.AddUserResponse;
import org.alexaoanaeliza.protocol.response.ErrorResponse;
import org.alexaoanaeliza.protocol.response.LoginUserResponse;
import org.alexaoanaeliza.protocol.response.Response;
import org.alexaoanaeliza.service.ServiceInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

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
                Response response = handleRequest((Request)request);
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
        Response response = null;

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
                server.addUser(firstname, lastName, email, password, phoneNumber, personalNumber, birthday,
                        country, county, city, street, number, apartment);
                return new AddUserResponse();
            } catch (ServerException | ServiceException | DatabaseException exception) {
                return new ErrorResponse(exception.getMessage());
            }
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException{
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }
}