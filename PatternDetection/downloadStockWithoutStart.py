import yfinance as yf
import sys


def write_file(values):
    f = open("/Users/alexaoanaeliza/Desktop/ExchangeNow/PatternDetection/stockData.txt", "w")
    for value in values.items():
        f.write(str(value[0].to_pydatetime().date()) + " " + str(value[1]) + "\n")
    f.close()


def download_field_data(stock='AAPL'):
    values = yf.download(stock, progress=False)['Close']
    write_file(values)
    return values


args = sys.argv[1]
download_field_data(args)

