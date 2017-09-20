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

import com.paymentez.example.model.Customer;
import com.paymentez.example.sdk.Paymentez;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

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

    /**
     * This code simulates "loading the customer for your current session".
     * Your own logic will likely look very different.
     *
     * @return customer for your current session
     */
    Customer getAuthenticatedCustomer(String uid, HttpServletRequest request){
        Customer customer = new Customer(uid,
                "dev@paymentez.com",
                request.getRemoteAddr());
        return customer;
    }

    /**
     * This endpoint receives an uid and gives you all their cards assigned to that user.
     *
     * @param uid Customer identifier. This is the identifier you use inside your application; you will receive it in notifications.
     *
     * @return a json with all the customer cards
     */
    @RequestMapping(value = "/get-cards", method = RequestMethod.GET, produces = "application/json")
    String getCards(@RequestParam(value = "uid") String uid) {

        String jsonResponse = Paymentez.doGetRequest(Paymentez.PAYMENTEZ_DEV_URL + "/v2/transaction/list?uid="+uid);

        return jsonResponse;
    }


    /**
     * This endpoint is used by Android/ios example app to create a charge.
     *
     * @param uid Customer identifier. This is the identifier you use inside your application; you will receive it in notifications.
     *
     * @return a json with all the customer cards
     */
    @RequestMapping(value = "/create-charge", method = RequestMethod.POST, produces = "application/json")
    String createCharge(@RequestParam(value = "uid") String uid,
                        @RequestParam(value = "session_id") String session_id,
                        @RequestParam(value = "token") String token,
                        @RequestParam(value = "amount") double amount,
                        @RequestParam(value = "dev_reference") String dev_reference,
                        @RequestParam(value = "description") String description,
                        HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(uid, request);

        String jsonPaymentezDebit = Paymentez.paymentezDebitJson(customer, session_id, token, amount, dev_reference, description);

        String jsonResponse = Paymentez.doPostRequest(Paymentez.PAYMENTEZ_DEV_URL + "/v2/transaction/debit", jsonPaymentezDebit);

        return jsonResponse;
    }

}
