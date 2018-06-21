package com.language;

import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslationResult;
import org.json.simple.JSONObject;

/*
* Translation as a Data Type
* */
public class Translation {
    String text;
    String to;

    Translation(String text, String to) {
        this.text = text;
        this.to = to;
    }

    Translation(TranslationResult translationResult, String to) {
        this.text = translationResult.getTranslations().get(0).getTranslation();
        this.to = to;
    }

    /*
    * creates a json object from the data from Translation object
    */
    JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("text", text);
        obj.put("to", to);
        return obj;
    }
}
