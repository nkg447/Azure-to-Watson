package com;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/*
 * Utility functions
 * */
public class Util {
    /*
     * Returns a JSONObject from the data.
     */
    public static JSONObject jsonify(String data) throws ParseException {
        Object obj = new JSONParser().parse(data);
        JSONObject jo = (JSONObject) ((JSONArray) obj).get(0);
        return jo;
    }

    /*
     * Returns a String containing the body of the request object.
     */
    public static String getRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();

        String body = "";
        String temp;
        while ((temp = br.readLine()) != null) {
            body += temp;
        }
        return body;
    }

    public static Element xmlify(HttpServletRequest request) throws IOException, JDOMException {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(request.getReader());
        Element speak = document.getRootElement();
        return speak;
    }

    /*
     * Convert a simple line to a Sentence.
     * eg. "it is a cat"  -->  "It is a cat."
     */
    public static String toSentence(String str) {
        str = str.trim();
        if (!str.endsWith(".")) {
            str = str + ".";
        }
        String firstChar = String.valueOf(str.charAt(0));

        return firstChar.toUpperCase() + str.substring(1);
    }
}
