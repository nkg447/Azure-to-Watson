package com;

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

}
