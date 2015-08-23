package com.morristaedt.mirror.requests;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by HannahMitt on 8/23/15.
 */
public class YahooStockResponse {

    private YahooQueryResponse query;

    private class YahooQueryResponse {
        public YahooResultsResponse results;
    }

    private class YahooResultsResponse {
        public YahooQuoteResponse quote;
    }

    public class YahooQuoteResponse {
        public String symbol;
        public String PercentChange;
        public float LastTradePriceOnly;

        public BigDecimal getPercentageChange() {
            BigDecimal decimalPercentage = new BigDecimal(PercentChange.trim().replace("%", "")).divide(BigDecimal.valueOf(100), RoundingMode.CEILING);
            return decimalPercentage;
        }
    }


    public YahooQuoteResponse getQuoteResponse() {
        if (query != null && query.results != null) {
            return query.results.quote;
        }
        return null;
    }
}
