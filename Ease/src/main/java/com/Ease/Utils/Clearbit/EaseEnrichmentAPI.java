package com.Ease.Utils.Clearbit;

import com.Ease.Context.Variables;
import org.json.JSONObject;

public class EaseEnrichmentAPI extends EnrichmentAPI {
    public EaseEnrichmentAPI() {
        super(Variables.CLEARBIT_API_KEY);
    }

    @Override
    public JSONObject emailLookup(String email) {
        JSONObject lookup = super.emailLookup(email);
        JSONObject res = new JSONObject();
        res.put("success", !lookup.has("error"));
        JSONObject name = lookup.getJSONObject("name");
        res.put("first_name", name.optString("givenName"));
        res.put("last_name", name.optString("familyName"));
        res.put("company_name", lookup.has("employment") ? lookup.getJSONObject("employment").optString("name") : "");
        return res;
    }
}
