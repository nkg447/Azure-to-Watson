package com.language;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "TranslateServlet")
public class TranslateServlet extends HttpServlet {

    /*
    * Returns a String containing the body of the request object.
    */
    static String getRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();

        String body = "";
        String temp;
        while ((temp = br.readLine()) != null) {
            body += temp;
        }
        return body;
    }

    /*
    * Returns a JSONObject from the data.
    */
    static JSONObject jsonify(String data) throws ParseException {
        Object obj = new JSONParser().parse(data);
        JSONObject jo = (JSONObject) ((JSONArray) obj).get(0);
        return jo;
    }

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
            String body = getRequestBody(request);
            JSONObject jsonObject = jsonify(body);

            String text = (String) jsonObject.get("Text");
            String sourceLanguage = Detector.detect(text);
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
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("Only POST request are accepted.");
    }
}
