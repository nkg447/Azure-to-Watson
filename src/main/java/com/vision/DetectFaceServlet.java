package com.vision;

import com.Util;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.DetectedFaces;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.Face;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.FaceLocation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DetectFaceServlet")
public class DetectFaceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String body = Util.getRequestBody(request);
            Object obj = new JSONParser().parse(body);
            JSONObject jsonObject = (JSONObject) obj;
            String url = (String) jsonObject.get("url");

            DetectedFaces detectedFaces = FaceDetect.faceDetect(url);

            String jsonResponse = getJSONResponse(detectedFaces);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().println(jsonResponse);

        } catch (ParseException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    /*
     * creates a JSON response of the detected faces
     */
    private String getJSONResponse(DetectedFaces detectedFaces) {
        JSONArray response = new JSONArray();

        for (Face face : detectedFaces.getImages().get(0).getFaces()) {
            JSONObject jsonFace = new JSONObject();

            jsonFace.put("faceId", Integer.toHexString(face.hashCode()).toString());

            JSONObject faceRectangle = new JSONObject();
            FaceLocation faceLocation = face.getFaceLocation();
            faceRectangle.put("top", faceLocation.getTop());
            faceRectangle.put("left", faceLocation.getLeft());
            faceRectangle.put("width", faceLocation.getWidth());
            faceRectangle.put("height", faceLocation.getHeight());
            jsonFace.put("faceRectangle", faceRectangle);

            JSONObject faceAttributes = new JSONObject();
            faceAttributes.put("gender", face.getGender().getGender());
            faceAttributes.put("age", (face.getAge().getMax() + face.getAge().getMin()) / 2);
            jsonFace.put("faceAttributes", faceAttributes);

            response.add(jsonFace);
        }
        return String.valueOf(response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Only POST request are accepted.");
    }
}
