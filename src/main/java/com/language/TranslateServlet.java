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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "TranslateServlet")
public class TranslateServlet extends HttpServlet {

    static String getRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();

        String body = "";
        String temp;
        while ((temp = br.readLine()) != null) {
            body += temp;
        }
        return body;
    }

    static JSONObject jsonify(String data) throws ParseException {
        Object obj = new JSONParser().parse(data);
        JSONObject jo = (JSONObject) ((JSONArray) obj).get(0);
        return jo;
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String body = getRequestBody(request);
            JSONObject jsonObject = jsonify(body);

            String text = (String) jsonObject.get("Text");
            String sourceLanguage = Detector.detect(text);
            String targetLanguages[]=request.getParameterValues("to");
            List<String> translationResults=new LinkedList<>();

            for(String toLang : targetLanguages){
                translationResults.add(Translator.translate(text,sourceLanguage,toLang));
            }

            response.getWriter().println(translationResults);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("Only POST request are accepted.");
    }
}
