package com.speech;

import com.Util;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechRecognitionAlternative;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechRecognitionResult;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechRecognitionResults;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(name = "RecognizeServlet")
public class RecognizeServlet extends HttpServlet {

    /*
     * recognize the speech from the file received as binary data.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String language = request.getParameter("language");
        InputStream audioInputStream = request.getInputStream();

        SpeechRecognitionResults results = Recognize.recognize(language, audioInputStream);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = getJSONResponse(results);

        response.getWriter().println(jsonResponse);

    }

    /*
     * creates a JSON response of the source language and the translation results
     */
    private String getJSONResponse(SpeechRecognitionResults results) {
        JSONObject response = new JSONObject();

        JSONArray nbestArr = new JSONArray();

        JSONObject nbest = new JSONObject();

        int size = 0;
        double confidence = 0;

        String transcript = "";

        SpeechRecognitionAlternative recognition;
        for (SpeechRecognitionResult result : results.getResults()) {
            if (result.isFinalResults()) {
                size++;
                recognition = result.getAlternatives().get(0);
                transcript += recognition.getTranscript();
                confidence += recognition.getConfidence();
            }
        }

        //avg confidence
        confidence = confidence / size;

        nbest.put("Lexical", transcript);
        nbest.put("Display", Util.toSentence(transcript));
        nbest.put("Confidence", confidence);

        nbestArr.add(nbest);

        response.put("RecognitionStatus", "Success");
        response.put("NBest", nbestArr);
        response.put("Offset", 0);
        response.put("Duration", 0);

        return response.toString();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Only POST request are accepted.");
    }
}
