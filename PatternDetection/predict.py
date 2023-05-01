import yfinance as yf
import numpy as np
from sklearn.preprocessing import MinMaxScaler
from tensorflow import keras
from keras import layers
import pandas as pd
from datetime import timedelta, date
import random
import sys
from datetime import datetime


def write_predictions(dates, values):
    f = open("/Users/alexaoanaeliza/Desktop/ExchangeNow/PatternDetection/stockPredictions.txt", "w")
    for i in range(len(dates)):
        f.write(str(dates[i]) + " " + str(values[i][0]) + "\n")
    f.close()


def download_field_data(stock='AAPL', field='Close', start=''):
    return yf.download(stock, progress=False, start=start)[field]


def normalize_data(data_to_normalize, scaler=MinMaxScaler(feature_range=(0, 1))):
    values = data_to_normalize.values
    return scaler, scaler.fit_transform(values.reshape(-1, 1))


def split_data(normalized_data, train_data_length, field_data):
    train_data = normalized_data[0: train_data_length, :]
    train_data_x = np.array([train_data[i - 60: i, 0] for i in range(60, len(train_data))])
    train_data_y = np.array([train_data[i] for i in range(60, len(train_data))])

    train_data_x = np.reshape(train_data_x, (train_data_x.shape[0], train_data_x.shape[1], 1))

    test_data = normalized_data[train_data_length - 60:, :]
    test_data_x = np.array([test_data[i - 60: i, 0] for i in range(60, len(test_data))])
    test_data_y = np.array(field_data.values[train_data_length:])

    test_data_x = np.reshape(test_data_x, (test_data_x.shape[0], test_data_x.shape[1], 1))

    return train_data_x, train_data_y, test_data_x, test_data_y


def denormalize_data(data_to_denormalize, scaler):
    data = np.reshape(data_to_denormalize, (data_to_denormalize.shape[0], 1))
    return scaler.inverse_transform(data)


def define_model_two_LSTM(train_data_x, optimizer='adam', loss='mae'):
    model = keras.Sequential()
    model.add(layers.LSTM(100, return_sequences=True, input_shape=(train_data_x.shape[1], 1)))
    model.add(layers.LSTM(100, return_sequences=False))
    model.add(layers.Dense(25))
    model.add(layers.Dense(1))
    model.compile(optimizer=optimizer, loss=loss)
    model.summary()
    return model


def fit_model(model, train_data_x, train_data_y, batch_size=20, epochs=10):
    model.fit(train_data_x, train_data_y, batch_size=batch_size, epochs=epochs)
    return model


def predict(model, test_data_x):
    return model.predict(test_data_x)


def add_future_data(end_date, last_input):
    start_date = date.today()
    lower = last_input - 0.05 * last_input
    upper = last_input + 0.05 * last_input

    value = random.uniform(lower, upper)
    future_data = pd.Series([value], index=[start_date])

    while start_date <= end_date:
        if start_date.weekday() < 5:
            value = random.uniform(lower, upper)
            series = pd.Series([value], index=[start_date])
            future_data = pd.concat([future_data, series])
        start_date = start_date + timedelta(days=1)

    return future_data


def main(stock, start_date, end_date):
    field_data = download_field_data(stock=stock, field='Close', start=start_date)

    last = field_data.values[-1]
    future_data = add_future_data(end_date, last)

    length = len(future_data)
    field_data = pd.concat([field_data, future_data])
    scaler, normalized_data = normalize_data(field_data)
    train_data_x, train_data_y, test_data_x, test_data_y = split_data(normalized_data, len(field_data) - length,
                                                                      field_data)
    model = define_model_two_LSTM(train_data_x)
    model = fit_model(model, train_data_x, train_data_y)
    predictions = predict(model, test_data_x)
    predictions = denormalize_data(predictions, scaler)
    write_predictions(future_data.index.values, predictions)


args1 = sys.argv[1]
args2 = datetime.strptime(sys.argv[2], '%Y-%m-%d').date()
args3 = datetime.strptime(sys.argv[3], '%Y-%m-%d').date()
main(args1, args2, args3)
