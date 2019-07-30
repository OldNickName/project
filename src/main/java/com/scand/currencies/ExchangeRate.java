package com.scand.currencies;

import java.math.BigDecimal;

class ExchangeRate {
    final String currency;
    final BigDecimal spot;

    ExchangeRate(String currency, BigDecimal spot) {
        this.currency = currency;
        this.spot = spot;
    }
}
