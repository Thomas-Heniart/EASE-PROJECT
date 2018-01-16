package com.Ease.Utils.Clearbit;

import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.ParameterMap;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import java.nio.charset.Charset;

public class EnrichmentAPI {

    private String api_key;

    public EnrichmentAPI(String api_key) {
        this.api_key = api_key;
        if (!this.api_key.endsWith(":"))
            this.api_key += ":";
    }

    public JSONObject emailLookup(String email) {
        return this.buildRequest("people", email);
    }

    public JSONObject domainLookup(String domain) {
        return this.buildRequest("companies", domain);
    }

    private JSONObject buildRequest(String lookup_type, String value) {
        BasicHttpClient basicHttpClient = new BasicHttpClient("https://person.clearbit.com");
        ParameterMap parameterMap = basicHttpClient.newParams();
        switch (lookup_type) {
            case "people":
                parameterMap.add("email", value);
                break;

            case "companies":
                parameterMap.add("domain", value);
                break;

            default:
                break;
        }
        basicHttpClient.addHeader("Authorization", "Basic " + Base64.encodeBase64String(this.api_key.getBytes(Charset.forName("UTF8"))));
        return new JSONObject(basicHttpClient.get("/v2/" + lookup_type + "/find", parameterMap).getBodyAsString());
    }

}
