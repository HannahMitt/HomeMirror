package com.morristaedt.mirror.requests;

import android.text.TextUtils;

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
            if (!TextUtils.isEmpty(PercentChange)) {
                BigDecimal decimalPercentage = new BigDecimal(PercentChange.trim().replace("%", "")).divide(BigDecimal.valueOf(100), RoundingMode.CEILING);
                return decimalPercentage;
            } else {
                return BigDecimal.valueOf(0);
            }
        }
    }


    public YahooQuoteResponse getQuoteResponse() {
        if (query != null && query.results != null) {
            return query.results.quote;
        }
        return null;
    }
}
