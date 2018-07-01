package com.language;

import com.Util;
import com.ibm.watson.developer_cloud.language_translator.v2.model.IdentifiedLanguage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DetectServlet")
public class DetectServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String body = Util.getRequestBody(request);
        JSONObject jsonObject = null;
        try {
            jsonObject = Util.jsonify(body);
            String text = (String) jsonObject.get("Text");
            List<IdentifiedLanguage> identifiedLanguages = Detector.detectLanguages(text);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String jsonResponse = getJSONResponse(identifiedLanguages);
            response.getWriter().println(jsonResponse);
        } catch (ParseException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    /*
     * creates a JSON response of the source language and the translation results
     */
    private String getJSONResponse(List<IdentifiedLanguage> identifiedLanguages) {
        JSONArray response = new JSONArray();
        JSONObject responseObj = new JSONObject();

        responseObj.put("language", identifiedLanguages.get(0).getLanguage());
        responseObj.put("score", 1.0);
        responseObj.put("isTranslationSupported", true);
        responseObj.put("isTransliterationSupported", false);

        JSONArray alternateLang = new JSONArray();

        for (int i = 1; i < identifiedLanguages.size(); i++) {
            if (identifiedLanguages.get(i).getConfidence() > 0.1) {

                JSONObject language = new JSONObject();
                language.put("language", identifiedLanguages.get(i).getLanguage());
                language.put("score", 1.0);
                language.put("isTranslationSupported", true);
                language.put("isTransliterationSupported", false);

                alternateLang.add(language);
            }
        }

        responseObj.put("alternatives", alternateLang);

        response.add(responseObj);

        return String.valueOf(response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Only POST request are accepted.");
    }
}
