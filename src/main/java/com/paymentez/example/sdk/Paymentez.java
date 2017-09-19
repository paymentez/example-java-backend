package com.paymentez.example.sdk;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Map;

/**
 * Created by mmucito on 18/09/17.
 */
public class Paymentez {
    public static String PAYMENTEZ_DEV_URL = "https://ccapi-stg.paymentez.com";
    public static String PAYMENTEZ_PROD_URL = "https://ccapi.paymentez.com";


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


    public static String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }
}
