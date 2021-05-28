package com.project.commoditiesCurrentPrice.utils;

public class Constants {

    public static int PAGE_COUNT = 1;
    public final static int REQUEST_TIME_OUT = 30;
    public static final long DB_EXPIRY_TIME = 40000;

    public static final String DatabaseTime = "DatabaseTime";
    public static final String BASE_URL = "https://api.data.gov.in/";

    //Query Map Params
    public final static String API_KEY = "579b464db66ec23bdd000001cdd3946e44ce4aad7209ff7b23ac571b";
    public final static String API_RESPONSE_FORMAT = "json";
    public final static String NO_OF_RECORDS_PER_REQUEST = "10";

    //Query Map Attribute
    public interface QueryMap {
        String ATTRIBUTE_API_KEY = "api-key";
        String ATTRIBUTE_FORMAT = "format";
        String ATTRIBUTE_LIMIT = "limit";
        String ATTRIBUTE_OFFSET = "offset";
    }
}
