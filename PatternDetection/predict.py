import sys
import math
import yfinance as yf
import numpy as np
from sklearn.preprocessing import MinMaxScaler
from tensorflow import keras
from keras import layers
import pandas as pd
from datetime import datetime, timedelta
from sklearn.metrics import mean_absolute_error
from sklearn.metrics import mean_absolute_percentage_error
from sklearn.metrics import mean_squared_error


def write_file(values):
    f = open("/Users/alexaoanaeliza/Desktop/ExchangeNow/PatternDetection/stockData.txt", "w")
    for value in values.items():
        f.write(str(value[0].to_pydatetime().date()) + " " + str(value[1]) + "\n")
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


def fit_and_save_model(model, train_data_x, train_data_y, batch_size=10, epochs=1, optimizer='adam', loss='mae'):
    model.fit(train_data_x, train_data_y, batch_size=batch_size, epochs=epochs)
    model.save_weights('Model' + str(batch_size) + '_' + str(epochs) + '_' + str(optimizer) + '_' + str(loss) + '.h5')
    return model


def predict(model, test_data_x):
    return model.predict(test_data_x)


def write_predictions(dates, values):
    f = open("/Users/alexaoanaeliza/Desktop/ExchangeNow/PatternDetection/stockPredictions.txt", "w")
    for i in range(len(dates)):
        f.write(str(datetime.utcfromtimestamp(dates[i].astype('O')/1e9).date()) + " " + str(values[i][0]) + "\n")
    f.close()


def main(stock, startDate, endDate):
    field_data = download_field_data(stock=stock, field='Close', start=startDate)

    startDate1 = datetime(2023, 4, 24)
    last = field_data.values[-1]
    future_data = pd.Series([last], index=[startDate1])

    while (startDate1 <= endDate):
        if startDate1.weekday() < 5:
            series = pd.Series([last], index=[startDate1])
            future_data = pd.concat([future_data, series])
        startDate1 = startDate1 + timedelta(days=1)

    length = len(future_data)
    field_data = pd.concat([field_data, future_data])
    scaler, normalized_data = normalize_data(field_data)
    train_data_x, train_data_y, test_data_x, test_data_y = split_data(normalized_data, len(field_data) - length, field_data)
    model = define_model_two_LSTM(train_data_x)
    model = fit_and_save_model(model, train_data_x, train_data_y)
    predictions = predict(model, test_data_x)
    predictions = denormalize_data(predictions, scaler)
    write_predictions(future_data.index.values, predictions)

# args1 = sys.argv[1]
# args2 = sys.argv[2]
main('AAPL', datetime(2015, 4, 24), datetime(2025, 4, 24))