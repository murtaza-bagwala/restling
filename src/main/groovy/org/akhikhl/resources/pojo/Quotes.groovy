package org.akhikhl.resources.pojo

public class Quotes {
    List<Quote> quotes = new ArrayList<Quote>();
    Integer total_count = 0
    String category

    public void add(Quote quote) {
        quotes.add(quote)
    }

    public void incrementCount() {
        total_count++
    }
}

