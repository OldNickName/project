package com.scand.currencies;

import java.io.IOException;
import java.sql.SQLException;

@SuppressWarnings("unchecked")
class CurrencyExchangeLoader {
    public static void main(String[] args) throws SQLException, IOException {
        CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
        Loader loader = new Loader();
        currencyExchangeService.load(loader.readFromURL());
    }
}
