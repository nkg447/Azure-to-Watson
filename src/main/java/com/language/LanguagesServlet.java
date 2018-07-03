package com.language;

import com.ibm.watson.developer_cloud.language_translator.v2.model.IdentifiableLanguage;
import com.ibm.watson.developer_cloud.language_translator.v2.model.IdentifiableLanguages;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LanguagesServlet")
public class LanguagesServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Only GET request are accepted.");
    }

    /*
     * creates a JSON response of the source language and the translation results
     */
    private String getJsonResponse(IdentifiableLanguages identifiableLanguages) {
        JSONObject response = new JSONObject();

        JSONObject dictionary = new JSONObject();

        JSONObject language;
        for (IdentifiableLanguage i : identifiableLanguages.getLanguages()) {
            language = new JSONObject();
            language.put("name", i.getName());
            language.put("nativeName", i.getName());

            dictionary.put(i.getLanguage(), language);
        }
        response.put("dictionary", dictionary);

        return String.valueOf(response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            IdentifiableLanguages identifiableLanguages = Languages.languages();

            String jsonResponse = getJsonResponse(identifiableLanguages);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().println(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}
