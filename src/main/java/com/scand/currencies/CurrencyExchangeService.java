package com.scand.currencies;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.List;

class CurrencyExchangeService {

    private static final Logger logger = Logger.getLogger(CurrencyExchangeService.class.getName());

    @SuppressWarnings("RedundantIfStatement")
    private boolean searchRecordInDatabase(ExchangeRate record) throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT Currency FROM spots WHERE Currency=(?)");
        try {
            preparedStatement.setString(1, record.currency);
            ResultSet resultset = preparedStatement.executeQuery();
            if (resultset.next()) {
                return true;
            } else {
                return false;
            }
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception e) {
                logger.log(Level.ERROR, "Exception: ", e);
            }
            try {
                connection.close();
            } catch (Exception e) {
                logger.log(Level.ERROR, "Exception: ", e);
            }
        }
    }

    void load(List<ExchangeRate> records) throws SQLException {
        for (ExchangeRate record : records) {
            if (searchRecordInDatabase(record)) updateRecord(record);
            else insertRecord(record);
        }
    }

    private void updateRecord(ExchangeRate record) throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE spots SET Spot=(?) WHERE Currency=(?)");
        try {
            preparedStatement.setBigDecimal(1, record.spot);
            preparedStatement.setString(2, record.currency);
            preparedStatement.execute();
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception e) {
                logger.log(Level.ERROR, "Exception: ", e);
            }
            try {
                connection.close();
            } catch (Exception e) {
                logger.log(Level.ERROR, "Exception: ", e);
            }
        }
    }

    private void insertRecord(ExchangeRate record) throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO spots (Currency, Spot) values(?, ?)");
        try {
            preparedStatement.setString(1, record.currency);
            preparedStatement.setBigDecimal(2, record.spot);
            preparedStatement.execute();
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception e) {
                logger.log(Level.ERROR, "Exception: ", e);
            }
            try {
                connection.close();
            } catch (Exception e) {
                logger.log(Level.ERROR, "Exception: ", e);
            }
        }
    }

    private BigDecimal searchSpotInDatabase(String currency) throws SQLException {
        BigDecimal spot;
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT Spot FROM spots WHERE Currency='" + (" " + currency) + "'");
        ResultSet resultSet = preparedStatement.executeQuery();
        try {
            resultSet.next();
            spot = new BigDecimal(resultSet.getString(1));
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                logger.log(Level.ERROR, "Exception: ", e);
            }
            try {
                preparedStatement.close();
            } catch (Exception e) {
                logger.log(Level.ERROR, "Exception: ", e);
            }
            try {
                connection.close();
            } catch (Exception e) {
                logger.log(Level.ERROR, "Exception: ", e);
            }
        }
        return spot;
    }

    BigDecimal convert(String fromCurrency, String toCurrency, BigDecimal startAmount) throws SQLException {
        BigDecimal fromSpot = searchSpotInDatabase(fromCurrency);
        BigDecimal toSpot = searchSpotInDatabase(toCurrency);
        return (startAmount.multiply(toSpot)).divide(fromSpot, RoundingMode.HALF_UP);
    }
}