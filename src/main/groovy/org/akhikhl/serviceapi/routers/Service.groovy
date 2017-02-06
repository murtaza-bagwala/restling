package org.akhikhl.serviceapi.routers

import groovy.json.JsonSlurper
import org.akhikhl.resources.pojo.Quote
import org.akhikhl.resources.pojo.Quotes

import java.util.regex.Matcher
import java.util.regex.Pattern

class Service {
    static Service service
    Quotes twitterQuotes

    private Service () {
        print "New Object Created"
    }

    public static Service getService() {
        if (this.service == null) {
            this.service  = new Service()
        }
        return this.service
    }

    Quotes getData(String category, Integer pageNo) {
        Quotes quotes = new Quotes();
        if (category.equals("famous") || category.equals("movies")) {
            for (int i = 0; i < 10; i++) {
                quotes.add(prepareRequest(category))
                quotes.incrementCount()
            }
        } else {
            if (pageNo == 1) {
                twitterQuotes = new Quotes()
                prepareTwitterRequest(category).each { status ->
                    Quote quote = new Quote(removeUrl(status.text))
                    if (!twitterQuotes.quotes.any{twitterQuote -> quote.quote == twitterQuote.quote}) {
                        quote.author = status.user.name
                        twitterQuotes.add(quote)
                        twitterQuotes.incrementCount()
                    }
                }
            }
            quotes.setTotal_count(twitterQuotes.total_count)
            if (twitterQuotes.total_count < pageNo * 10) {
                if (twitterQuotes.total_count >= pageNo * 10 - 10)
                    quotes.setQuotes(twitterQuotes.getQuotes().subList(pageNo * 10 - 10, twitterQuotes.total_count))
            }
            else {
                quotes.setQuotes(twitterQuotes.getQuotes().subList(pageNo * 10 - 10, pageNo * 10))
            }
        }
        quotes.setCategory(category)
        return quotes
    }

    private Quote prepareRequest(String category) {
        def url = new URL("https://andruxnet-random-famous-quotes.p.mashape.com/?cat=" + category)
        def connection = (HttpURLConnection) url.openConnection()
        connection.setRequestMethod("POST")
        connection.setRequestProperty("X-Mashape-Key", "cqUWSfKEA5mshQ2qXdS2FQICJDV9p1BqhONjsnGY2uHwajI0iT")
        connection.setRequestProperty("Accept",
                "application/json")
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        Quote quote = new JsonSlurper().parse(connection.getInputStream())
        print quote

        return quote;
    }

    private Collection prepareTwitterRequest(String category) {
        def url = new URL("https://api.twitter.com/1.1/search/tweets.json?q=%23quotes%23quote%23" + category)
        def connection = (HttpURLConnection) url.openConnection()
        connection.setRequestMethod("GET")
        connection.setRequestProperty("Authorization", "Bearer " +
                "AAAAAAAAAAAAAAAAAAAAAP5ZywAAAAAAVFaY7ro3RufF9%2BRRg1pYHGvAo%2Bk" +
                "%3DJL5DbUNfq3Zl6xpbF1Bzj8BxK1JZCXrZ7rg15j8sheCEKsngcd")
        connection.setRequestProperty "Accept", "application/json"
        Object object = new JsonSlurper().parse(connection.getInputStream())
        return object.statuses.collect()
    }

    private static String removeUrl(String commentstr)  {
        String urlPattern = $/((https|http):?((\/)|(\\))+[\w\d:#@%/;$()~_?\\+-=.&]*)/$
        String retweetPattern = $/(RT[\s@\w\d_]*:)/$
        Matcher matcher = commentstr =~ retweetPattern
        int count = 0;
        while (matcher.find()) {
            commentstr = commentstr.replaceAll(matcher.group(count), "").trim();
            count++;
        }
        matcher = commentstr =~ urlPattern
        count = 0;
        while (matcher.find()) {
            commentstr = commentstr.replaceAll(matcher.group(count), "").trim();
            count++;
        }
        return commentstr;
    }

}
