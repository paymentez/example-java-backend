/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.paymentez.example;

import com.paymentez.example.sdk.Paymentez;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@SpringBootApplication
public class Main {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }

    @RequestMapping("/")
    String index() {
        return "Great, your backend is set up. Now you can configure the Paymentez example apps to point here.";
    }

    @RequestMapping(value = "/get-cards", method = RequestMethod.GET, produces = "application/json")
    String getCards(@RequestParam(value = "uid") String uid) {

        String apiResponse = "{}";
        OkHttpClient httpclient = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Paymentez.PAYMENTEZ_DEV_URL + "/v2/transaction/list").newBuilder();
        urlBuilder.addQueryParameter("uid", uid);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .header("Auth-Token", Paymentez.getAuthToken(System.getenv("APP_CODE"), System.getenv("APP_SECRET_KEY")))
                .url(url).get().build();

        try (Response response = httpclient.newCall(request).execute()) {
            apiResponse = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return apiResponse;
    }


}
