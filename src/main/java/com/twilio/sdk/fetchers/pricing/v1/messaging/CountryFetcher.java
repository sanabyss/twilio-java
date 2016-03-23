package com.twilio.sdk.fetchers.pricing.v1.messaging;

import com.twilio.sdk.clients.TwilioRestClient;
import com.twilio.sdk.exceptions.ApiConnectionException;
import com.twilio.sdk.exceptions.ApiException;
import com.twilio.sdk.fetchers.Fetcher;
import com.twilio.sdk.http.HttpMethod;
import com.twilio.sdk.http.Request;
import com.twilio.sdk.http.Response;
import com.twilio.sdk.resources.RestException;
import com.twilio.sdk.resources.pricing.v1.messaging.Country;

public class CountryFetcher extends Fetcher<Country> {
    private final String isoCountry;

    /**
     * Construct a new CountryFetcher.
     * 
     * @param isoCountry The iso_country
     */
    public CountryFetcher(final String isoCountry) {
        this.isoCountry = isoCountry;
    }

    /**
     * Make the request to the Twilio API to perform the fetch.
     * 
     * @param client TwilioRestClient with which to make the request
     * @return Fetched Country
     */
    @Override
    @SuppressWarnings("checkstyle:linelength")
    public Country execute(final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            TwilioRestClient.Domains.PRICING,
            "/v1/Messaging/Countries/" + this.isoCountry + "",
            client.getAccountSid()
        );
        
        Response response = client.request(request);
        
        if (response == null) {
            throw new ApiConnectionException("Country fetch failed: Unable to connect to server");
        } else if (response.getStatusCode() != TwilioRestClient.HTTP_STATUS_CODE_OK) {
            RestException restException = RestException.fromJson(response.getStream(), client.getObjectMapper());
            if (restException == null) {
                throw new ApiException("Server Error, no content");
            }
        
            throw new ApiException(
                restException.getMessage(),
                restException.getCode(),
                restException.getMoreInfo(),
                restException.getStatus(),
                null
            );
        }
        
        return Country.fromJson(response.getStream(), client.getObjectMapper());
    }
}