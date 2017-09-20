package com.paymentez.example.sdk;

import com.paymentez.example.model.Customer;
import okhttp3.*;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import java.io.IOException;
import java.util.Base64;

/**
 * Created by mmucito on 18/09/17.
 */
public class Paymentez {

    public static String PAYMENTEZ_DEV_URL = "https://ccapi-stg.paymentez.com";
    public static String PAYMENTEZ_PROD_URL = "https://ccapi.paymentez.com";

    public static OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static String getUniqToken(String auth_timestamp, String app_secret_key) {
        String uniq_token_string = app_secret_key + auth_timestamp;
        return getHash(uniq_token_string);
    }

    public static String getAuthToken(String app_code, String app_secret_key) {
        String auth_timestamp = "" + (System.currentTimeMillis());
        String string_auth_token = app_code + ";" + auth_timestamp + ";" + getUniqToken(auth_timestamp, app_secret_key);
        String auth_token = new String(Base64.getEncoder().encode(string_auth_token.getBytes()));
        return auth_token;
    }

    public static String getHash(String message) {
        String sha256hex = new String(Hex.encodeHex(DigestUtils.sha256(message)));
        return sha256hex;
    }

    public static String paymentezDebitJson(Customer customer, String session_id, String token, double amount, String dev_reference, String description) {
        return "{" +
                    "\"session_id\": \"" + session_id + "\"," +
                    "\"user\": {" +
                        "\"id\": \"" + customer.getId() + "\"," +
                        "\"email\": \"" + customer.getEmail() + "\"," +
                        "\"ip_address\": \"" + customer.getIpAddress() + "\"" +
                    "}," +
                    "\"product\": {" +
                        "\"code\": \"123\"," +
                        "\"amount\": " + amount + "," +
                        "\"description\": \"" + description + "\"," +
                        "\"dev_reference\": \"" + dev_reference + "\"," +
                        "\"vat\": 0.00" +
                    "}," +
                    "\"card\": {" +
                        "\"token\": \"" + token + "\"" +
                    "}" +
                "}";
    }

    public static String doPostRequest(String url, String json){
        String jsonResponse = "{}";
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .header("Auth-Token", Paymentez.getAuthToken(System.getenv("APP_CODE"), System.getenv("APP_SECRET_KEY")))
                .url(url)
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            jsonResponse = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }

    public static String doGetRequest(String url){
        String jsonResponse = "{}";
        Request request = new Request.Builder()
                .header("Auth-Token", Paymentez.getAuthToken(System.getenv("APP_CODE"), System.getenv("APP_SECRET_KEY")))
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            jsonResponse = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }
}
