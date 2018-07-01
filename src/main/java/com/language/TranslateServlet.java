package com.language;

import com.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "TranslateServlet")
public class TranslateServlet extends HttpServlet {

    /*
     * creates a JSON response of the source language and the translation results
     */
    static String getJSONResponse(String sourceLanguage, List<Translation> translationResults) {
        JSONObject responseObj = new JSONObject();

        JSONObject detectedLanguage = new JSONObject();
        detectedLanguage.put("language", sourceLanguage);
        detectedLanguage.put("score", 1.0);
        responseObj.put("detectedLanguage", detectedLanguage);

        JSONArray translations = new JSONArray();

        for (Translation t : translationResults) {
            translations.add(t.toJSONObject());
        }

        responseObj.put("translations", translations);

        JSONArray response = new JSONArray();
        response.add(responseObj);

        System.out.println("Response - \n" + response);
        return String.valueOf(response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String body = Util.getRequestBody(request);
            JSONObject jsonObject = Util.jsonify(body);

            String text = (String) jsonObject.get("Text");
            String sourceLanguage = Detector.detectLanguages(text).get(0).getLanguage();

            String targetLanguages[] = request.getParameterValues("to");
            List<Translation> translationResults = new LinkedList<>();

            for (String toLang : targetLanguages) {
                translationResults.add(new Translation(Translator.translate(text, sourceLanguage, toLang), toLang));
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String jsonResponse = getJSONResponse(sourceLanguage, translationResults);
            response.getWriter().println(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Only POST request are accepted.");
    }
}
