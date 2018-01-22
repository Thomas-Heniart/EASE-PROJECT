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
        JSONObject error = lookup.optJSONObject("error");
        if (error != null)
            return lookup;
        JSONObject res = new JSONObject();
        JSONObject name = lookup.getJSONObject("name");
        res.put("first_name", name.getString("givenName"));
        res.put("last_name", name.getString("familyName"));
        res.put("company_name", lookup.getJSONObject("employment").getString("name"));
        return res;
    }
}
