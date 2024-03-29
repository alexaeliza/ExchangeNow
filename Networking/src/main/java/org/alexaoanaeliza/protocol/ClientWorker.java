package org.alexaoanaeliza.protocol;

import org.alexaoanaeliza.DebitCard;
import org.alexaoanaeliza.Stock;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.enums.Country;
import org.alexaoanaeliza.enums.DebitCardType;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.ServiceException;
import org.alexaoanaeliza.exception.ServerException;
import org.alexaoanaeliza.protocol.request.*;
import org.alexaoanaeliza.protocol.response.*;
import org.alexaoanaeliza.service.ServiceInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

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
            String email = loginUserRequest.getEmail();
            String password = loginUserRequest.getPassword();
            try {
                User connectedUser = server.loginUser(email, password);
                return new LoginUserResponse(connectedUser);
            } catch (ServerException | DatabaseException | ServiceException exception) {
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
            } catch (ServerException | DatabaseException | ServiceException exception) {
                return new ErrorResponse(exception.getMessage());
            }
        }

        if (request instanceof GetUserByIdRequest getUserByIdRequest) {
            Long id = getUserByIdRequest.getUserId();

            try {
                return new GetUserByIdResponse(server.getUserById(id));
            } catch (ServiceException serviceException) {
                return new ErrorResponse(serviceException.getMessage());
            }
        }

        if (request instanceof GetUserByEmailRequest getUserByEmailRequest) {
            String email = getUserByEmailRequest.getEmail();

            try {
                server.getUserByEmail(email);
                return new GetUserByEmailResponse(null);
            } catch (ServiceException serviceException) {
                return new ErrorResponse(serviceException.getMessage());
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
            } catch (ServiceException serviceException) {
                return new ErrorResponse(serviceException.getMessage());
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
            } catch (ServiceException serviceException) {
                return new ErrorResponse(serviceException.getMessage());
            }
        }

        if (request instanceof GetDebitCardByIdRequest getDebitCardByIdRequest) {
            Long id = getDebitCardByIdRequest.getId();

            try {
                DebitCard debitCard = server.getDebitCardById(id);
                return new GetDebitCardByIdResponse(debitCard);
            } catch (ServiceException serviceException) {
                return new ErrorResponse(serviceException.getMessage());
            }
        }

        if (request instanceof DepositAmountRequest depositAmountRequest) {
            DebitCard debitCard = depositAmountRequest.getDebitCard();
            Double amount = depositAmountRequest.getSum();

            try {
                server.depositAmount(amount, debitCard);
                return new DepositAmountResponse();
            } catch (ServiceException serviceException) {
                return new ErrorResponse(serviceException.getMessage());
            }
        }

        if (request instanceof WithdrawAmountRequest withdrawAmountRequest) {
            DebitCard debitCard = withdrawAmountRequest.getDebitCard();
            Double amount = withdrawAmountRequest.getSum();

            try {
                server.withdrawAmount(amount, debitCard);
                return new WithdrawAmountResponse();
            } catch (ServiceException serviceException) {
                return new ErrorResponse(serviceException.getMessage());
            }
        }

        if (request instanceof GetStocksRequest)
            try {
                return new GetStocksResponse(server.getStocks());
            } catch (ServiceException serviceException) {
                return new ErrorResponse(serviceException.getMessage());
            }


        if (request instanceof GetTodaySoldByUserRequest getTodaySoldByUserRequest) {
            User user = getTodaySoldByUserRequest.getUser();

            try {
                return new GetTodaySoldByUserResponse(server.getTodaySoldByUser(user));
            } catch (ServiceException serviceException) {
                return new ErrorResponse(serviceException.getMessage());
            }
        }

        if (request instanceof GetReturnValueByUserRequest getReturnValueByUserRequest) {
            User user = getReturnValueByUserRequest.getUser();

            try {
                return new GetReturnValueByUserResponse(server.getTodaySoldByUser(user));
            } catch (ServiceException serviceException) {
                return new ErrorResponse(serviceException.getMessage());
            }
        }

        if (request instanceof GetReturnPercentageByUserRequest getReturnPercentageByUserRequest) {
            User user = getReturnPercentageByUserRequest.getUser();

            try {
                return new GetReturnPercentageByUserResponse(server.getTodaySoldByUser(user));
            } catch (ServiceException serviceException) {
                return new ErrorResponse(serviceException.getMessage());
            }
        }
        
        if (request instanceof GetStockDataRequest getStockDataRequest) {
            String stockId = getStockDataRequest.getStockId();
            
            try {
                return new GetStockDataResponse(server.getStockData(stockId));
            } catch (ServiceException serviceException) {
                return new ErrorResponse(serviceException.getMessage());
            }
        }

        if (request instanceof BuyStockRequest buyStockRequest) {
            Long userId = buyStockRequest.getUserId();
            Long stockId = buyStockRequest.getStockId();
            LocalDateTime dateTime = buyStockRequest.getDateTime();
            Double sum = buyStockRequest.getSum();

            try {
                double availableSold = server.getUserById(userId).getAvailableAmount();
                if (availableSold < sum)
                    return new ErrorResponse("Only " + availableSold + "$ are available");
                return new BuyStockResponse(server.buyStock(userId, stockId, dateTime, sum));
            } catch (ServiceException serviceException) {
                return new ErrorResponse(serviceException.getMessage());
            }
        }

        if (request instanceof SellStockRequest sellStockRequest) {
            Long userId = sellStockRequest.getUserId();
            Long stockId = sellStockRequest.getStockId();
            LocalDateTime dateTime = sellStockRequest.getDateTime();
            Double sum = sellStockRequest.getSum();

            try {
                Map<Stock, Double> portfolio = server.getPortfolioByUser(userId);
                Stock stock = server.getStockById(stockId);
                if (!portfolio.containsKey(stock))
                    return new ErrorResponse("Stock " + stock.getName() + " does not appear in your portfolio");
                Double availableStock = portfolio.get(stock);
                if (availableStock < sum)
                    return new ErrorResponse("Only " + availableStock + "$ are available");
                return new SellStockResponse(server.sellStock(userId, stockId, dateTime, sum));
            } catch (ServiceException serviceException) {
                return new ErrorResponse(serviceException.getMessage());
            }
        }

        if (request instanceof GetStockByNameRequest getStockByNameRequest) {
            String stockName = getStockByNameRequest.getStockName();

            try {
                return new GetStockByNameResponse(server.getStockByName(stockName));
            } catch (ServiceException serviceException) {
                return new ErrorResponse(serviceException.getMessage());
            }
        }

        if (request instanceof GetPortfolioByUserRequest getPortfolioByUserRequest) {
            Long userId = getPortfolioByUserRequest.getUserId();

            try {
                return new GetPortfolioByUserResponse(server.getPortfolioByUser(userId));
            } catch (ServiceException serviceException) {
                return new ErrorResponse(serviceException.getMessage());
            }
        }

        if (request instanceof GetStockPredictionsRequest getStockPredictionsRequest) {
            String stockId = getStockPredictionsRequest.getStockId();
            LocalDate startDate = getStockPredictionsRequest.getStartDate();
            LocalDate endDate = getStockPredictionsRequest.getEndDate();

            try {
                return new GetStockPredictionsResponse(server.getStockPredictions(stockId, startDate, endDate));
            } catch (ServiceException serviceException) {
                return new ErrorResponse(serviceException.getMessage());
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