package com.housbrandapps.lunotickerwidget;

import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;


class CurrencyUtil {

    private final static HashMap<String, CharSequence[]> CURRENCY_PAIRS = new LinkedHashMap<>();

    static {
        CURRENCY_PAIRS.put(VirtualCurrencies.BTC, new String[]{
                Currencies.IDR,
                Currencies.SGD,
                Currencies.MYR,
                Currencies.NGN,
                Currencies.ZAR
        });
    }

    static String getCurrencySymbol(String currencyCode) {
        SortedMap<Currency, Locale> currencyLocaleMap = new TreeMap<>(new Comparator<Currency>() {
            public int compare(Currency c1, Currency c2) {
                return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
            }
        });
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                Currency currency = Currency.getInstance(locale);
                currencyLocaleMap.put(currency, locale);
            } catch (IllegalArgumentException ignored) {
            }
        }
        Currency currency = Currency.getInstance(currencyCode);
        return currency.getSymbol(currencyLocaleMap.get(currency));
    }

    class Currencies {
        static final String IDR = "IDR";
        static final String SGD = "SGD";
        static final String MYR = "MYR";
        static final String NGN = "NGN";
        static final String ZAR = "ZAR";
    }

    class VirtualCurrencies {
        static final String BTC = "BTC";
        static final String ETH = "ETH";
    }
}
