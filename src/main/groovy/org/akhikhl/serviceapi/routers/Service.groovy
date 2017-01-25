package org.akhikhl.serviceapi.routers
import groovy.json.JsonSlurper
import org.akhikhl.resources.pojo.Quote
import org.akhikhl.resources.pojo.Quotes

class Service {
    static Quotes getData(String category) {
        Quotes quotes = new Quotes();
        if (category.equals("famous") || category.equals("movies")) {
            for (int i = 0; i< 10; i++) {
                quotes.add(prepareRequest(category))
            }
        } else {
            prepareTwitterRequest(category).each {status -> quotes.add(new Quote(status.text))}
        }

        return quotes
    }

    private static Quote prepareRequest(String category) {
        def url = new URL("https://andruxnet-random-famous-quotes.p.mashape.com/?cat=" + category)
        def connection = (HttpURLConnection)url.openConnection()
        connection.setRequestMethod("POST")
        connection.setRequestProperty("X-Mashape-Key", "cqUWSfKEA5mshQ2qXdS2FQICJDV9p1BqhONjsnGY2uHwajI0iT")
        connection.setRequestProperty("Accept",
                "application/json")
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        Quote quote = new JsonSlurper().parse(connection.getInputStream())
        print quote

        return quote;
    }

    private static Collection prepareTwitterRequest(String category) {
        def url = new URL("https://api.twitter.com/1.1/search/tweets.json?q=%23quotes%23quote%23" +  category)
        def connection = (HttpURLConnection)url.openConnection()
        connection.setRequestMethod("GET")
        connection.setRequestProperty("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAP5ZywAAAAAAVFaY7ro3RufF9%2BRRg1pYHGvAo%2Bk%3DJL5DbUNfq3Zl6xpbF1Bzj8BxK1JZCXrZ7rg15j8sheCEKsngcd")
        connection.setRequestProperty("Accept", "application/json")
        Object object = new JsonSlurper().parse(connection.getInputStream())
        return object.statuses.collect()
    }
}
