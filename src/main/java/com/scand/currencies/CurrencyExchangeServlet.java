package com.scand.currencies;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Currency;

@SuppressWarnings("unchecked")
public class CurrencyExchangeServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(CurrencyExchangeServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fromCurrency = req.getParameter("fromCurrency");
        String toCurrency = req.getParameter("toCurrency");
        String amount = req.getParameter("amount");
        BigDecimal convertedAmount;
        CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
        Loader loader = new Loader();

        try {
            currencyExchangeService.load(loader.readFromURL());
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Exception in function load: ", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        PrintWriter printWriter = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        JSONObject errorObject = new JSONObject();
        JSONObject successfullyCreatedObject = new JSONObject();

        try {
            boolean fromCurrencyState = Currency.getAvailableCurrencies().contains(Currency.getInstance(fromCurrency));
            boolean toCurrencyState = Currency.getAvailableCurrencies().contains(Currency.getInstance(toCurrency));
            boolean amountState = amount.matches("[0-9]+");
            if (fromCurrencyState && toCurrencyState && amountState) {
                successfullyCreatedObject.put("fromCurrency: ", fromCurrency);
                successfullyCreatedObject.put("toCurrency: ", toCurrency);
                successfullyCreatedObject.put("amount: ", amount);
                convertedAmount = currencyExchangeService.convert(fromCurrency, toCurrency, new BigDecimal(amount));
                successfullyCreatedObject.put("convertedAmount:", convertedAmount);
                printWriter.println(successfullyCreatedObject);
                printWriter.flush();
            } else {
                errorObject.put("status:", "400");
                errorObject.put("code: ", "validationError");
                if (!fromCurrencyState) errorObject.put("fromCurrency: ", "notCurrency");
                if (!toCurrencyState) errorObject.put("toCurrency: ", "notCurrency");
                if (!amountState) errorObject.put("amount: ", "notNumber");
                printWriter.println(errorObject);
                printWriter.flush();
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, "exception converting: ", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "state error");
        }

        //   printWriter.print(jsonObject);
        //  printWriter.flush();
    }
}
