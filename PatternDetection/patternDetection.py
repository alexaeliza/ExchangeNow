import sys
import math
import yfinance as yf
import numpy as np
from sklearn.preprocessing import MinMaxScaler
# from tensorflow import keras
from keras import layers
from sklearn.metrics import mean_absolute_error
from sklearn.metrics import mean_absolute_percentage_error
from sklearn.metrics import mean_squared_error


def writeFile(values):
    f = open("/Users/alexaoanaeliza/Desktop/ExchangeNow/PatternDetection/stockData.txt", "w")
    for value in values.items():
        f.write(str(value[0].to_pydatetime().date()) + " " + str(value[1]) + "\n")
    f.close()


# def download_all_data(stock='AAPL', start='2016-01-01', end='2022-10-01'):
#     values = yf.download(stock, start=start, end=end, progress=False)
#     writeFile(values)
#     return values


def download_field_data(stock='AAPL', start='2022-10-10', end='2022-10-12', field='Close'):
    values = yf.download(stock, start=start, end=end, progress=False)[field]
    writeFile(values)
    return values

#
# def plot_data(field_data):
#     plt.figure(figsize=(15, 8))
#     plt.title('Stock Prices History')
#     plt.plot(field_data)
#     plt.xlabel('Date')
#     plt.ylabel('Prices ($)')
#
#
# def normalize_data(data_to_normalize, scaler=MinMaxScaler(feature_range=(0, 1))):
#     values = data_to_normalize.values
#     return scaler, scaler.fit_transform(values.reshape(-1, 1))
#
#
# def split_data(normalized_data, train_data_length, field_data):
#     train_data = normalized_data[0: train_data_length, :]
#     train_data_x = np.array([train_data[i - 60: i, 0] for i in range(60, len(train_data))])
#     train_data_y = np.array([train_data[i] for i in range(60, len(train_data))])
#
#     train_data_x = np.reshape(train_data_x, (train_data_x.shape[0], train_data_x.shape[1], 1))
#
#     test_data = normalized_data[train_data_length - 60:, :]
#     test_data_x = np.array([test_data[i - 60: i, 0] for i in range(60, len(test_data))])
#     test_data_y = np.array(field_data.values[train_data_length:])
#
#     test_data_x = np.reshape(test_data_x, (test_data_x.shape[0], test_data_x.shape[1], 1))
#
#     return train_data_x, train_data_y, test_data_x, test_data_y
#
#
# def denormalize_data(data_to_denormalize, scaler):
#     data = np.reshape(data_to_denormalize, (data_to_denormalize.shape[0], 1))
#     return scaler.inverse_transform(data)
#
#
# def define_model_one_LSTM(train_data_x, optimizer='adam', loss='mae'):
#     model = keras.Sequential()
#     model.add(layers.LSTM(100, return_sequences=True, input_shape=(train_data_x.shape[1], 1)))
#     model.add(layers.Flatten())
#     model.add(layers.Dense(25))
#     model.add(layers.Dense(1))
#     model.compile(optimizer=optimizer, loss=loss)
#     model.summary()
#     return model
#
#
# def define_model_two_LSTM(train_data_x, optimizer='adam', loss='mae'):
#     model = keras.Sequential()
#     model.add(layers.LSTM(100, return_sequences=True, input_shape=(train_data_x.shape[1], 1)))
#     model.add(layers.LSTM(100, return_sequences=False))
#     model.add(layers.Dense(25))
#     model.add(layers.Dense(1))
#     model.compile(optimizer=optimizer, loss=loss)
#     model.summary()
#     return model
#
#
# def define_model_three_LSTM(train_data_x, optimizer='adam', loss='mae'):
#     model = keras.Sequential()
#     model.add(layers.LSTM(400, return_sequences=True, input_shape=(train_data_x.shape[1], 1)))
#     model.add(layers.LSTM(200, return_sequences=True))
#     model.add(layers.LSTM(100, return_sequences=False))
#     model.add(layers.Dense(25))
#     model.add(layers.Dense(1))
#     model.compile(optimizer=optimizer, loss=loss)
#     model.summary()
#     return model
#
#
# def load_model(model, batch_size=10, epochs=20, optimizer='adam', loss='mae'):
#     model.load_weights('Model' + str(batch_size) + '_' + str(epochs) + '_' + str(optimizer) + '_' + str(loss) + '.h5')
#     return model
#
#
# def fit_and_save_model(model, train_data_x, train_data_y, batch_size=10, epochs=20, optimizer='adam', loss='mae'):
#     model.fit(train_data_x, train_data_y, batch_size=batch_size, epochs=epochs)
#     model.save_weights('Model' + str(batch_size) + '_' + str(epochs) + '_' + str(optimizer) + '_' + str(loss) + '.h5')
#     return model
#
#
# def predict(model, test_data_x):
#     return model.predict(test_data_x)
#
#
# def compute_metrics(data, predictions, train_data_length):
#     accuracy = compute_accuracy(data, predictions, train_data_length)
#     mae = compute_mae(data, predictions, train_data_length)
#     mape = compute_mape(data, predictions, train_data_length)
#     rmse = compute_rmse(data, predictions, train_data_length)
#     return accuracy, mae, mape, rmse
#
#
# def compute_accuracy(data, predictions, train_data_length, ratio=0.05):
#     validation = data[train_data_length :]
#     total = len(validation)
#     correct = 0
#     for i in range(total):
#         if data[i + train_data_length] - ratio * data[i + train_data_length]  <= predictions[i] <= data[i + train_data_length]  + ratio * data[i + train_data_length] :
#             correct = correct + 1
#     return (correct / total) * 100
#
#
# def compute_mae(data, predictions, train_data_length):
#     validation = data[train_data_length :]
#     return mean_absolute_error(validation, predictions)
#
#
# def compute_mape(data, predictions, train_data_length):
#     validation = data[train_data_length :]
#     return mean_absolute_percentage_error(validation, predictions)
#
#
# def compute_rmse(data, predictions, train_data_length):
#     validation = data[train_data_length :]
#     return mean_squared_error(validation, predictions)
#
#
# def plotResult(stock_data, predictions, training_data_len):
#     data = stock_data.filter(['Close'])
#     validation = data[training_data_len :]
#     validation['Predictions'] = predictions
#     plt.figure(figsize=(16, 8))
#     plt.title('Model')
#     plt.xlabel('Date')
#     plt.ylabel('Close Price USD ($)')
#     plt.plot(validation[['Close', 'Predictions']])
#     plt.legend(['Val', 'Predictions'], loc='lower right')
#     plt.show()


# def main():
    # data = download_all_data()
    # field_data = download_field_data()
    # plot_data(field_data)
    # scaler, normalized_data = normalize_data(field_data)
    # train_data_x, train_data_y, test_data_x, test_data_y = split_data(normalized_data, math.ceil(0.8 * len(field_data)), field_data)
    # model = define_model_three_LSTM(train_data_x)
    # model = fit_and_save_model(model, train_data_x, train_data_y)
    # model = load_model(model)
    # predictions = predict(model, test_data_x)
    # predictions = denormalize_data(predictions, scaler)
    # plotResult(data, predictions, math.ceil(0.8 * len(field_data)))
    # accuracy, mae, mape, rmse = compute_metrics(field_data, predictions, math.ceil(0.8 * len(field_data)))

    # print('Accuracy: ' + str(accuracy) + '%')
    # print('Mean average error: ' + str(mae))
    # print('Mean average percentage error: ' + str(mape))
    # print('Mean squared error: ' + str(rmse))

    # print('This is Python Code')
    # print('Executing Python')
    # print('From Java')

args = sys.argv[1]
download_field_data(args)