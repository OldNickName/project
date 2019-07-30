package com.scand.currencies;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipInputStream;

class Loader {
    private static final Logger logger = Logger.getLogger(Loader.class.getName());
    private static final String urlString = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref.zip";

    List readFromURL() throws IOException {
        URL url = new URL(urlString);
        url.openConnection();
        InputStream inputStream = url.openStream();
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        zipInputStream.getNextEntry();
        LineIterator lineIterator = IOUtils.lineIterator(zipInputStream, "utf-8");
        List<String> currencyList = Arrays.asList(lineIterator.nextLine().split(","));
        List<String> spotList = Arrays.asList(lineIterator.nextLine().split(","));
        List<ExchangeRate> exchangeRateList = new ArrayList<>();
        if(currencyList.size() == spotList.size()){
            for (int i = 1; i < currencyList.size() - 1; i++) {
                exchangeRateList.add(new ExchangeRate(currencyList.get(i), new BigDecimal(spotList.get(i).trim())));
            }
        }else{
            logger.log(Level.ERROR, "Error:" , new IOException());
        }
        lineIterator.close();
        return exchangeRateList;
    }
}
