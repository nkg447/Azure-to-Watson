package com.vision;

import com.Util;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassResult;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImage;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;

import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifierResult;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@WebServlet(name = "ImageClassifyServlet")
public class ImageClassifyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String body = Util.getRequestBody(request);
            Object obj = new JSONParser().parse(body);
            JSONObject jsonObject = (JSONObject) obj;
            String url = (String) jsonObject.get("url");


            ClassifiedImage classifiedImage = ImageClassifier.classify(url).getImages().get(0);

            String jsonResponse = getJSONResponse(classifiedImage, url);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().println(jsonResponse);
            System.out.println("ImageClassifyServlet: response sent");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private String getJSONResponse(ClassifiedImage classifiedImage, String url) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new URL(url));

        JSONObject response = new JSONObject();
        response.put("requestId", Integer.toHexString(classifiedImage.hashCode()).toString());

        JSONObject metadata = new JSONObject();
        metadata.put("width", bufferedImage.getWidth());
        metadata.put("height", bufferedImage.getHeight());
        metadata.put("format", url.substring(url.lastIndexOf('.')+1));
        response.put("metadata", metadata);

        JSONObject color = new JSONObject();
        String clr[] = getColor(classifiedImage).split(" ");
        color.put("dominantColorForeground", clr[0]);
        color.put("dominantColorBackground", clr[clr.length - 1]);
        JSONArray dominantColors = new JSONArray();
        for (String s : clr) {
            dominantColors.add(s);
        }
        color.put("dominantColors", dominantColors);
        response.put("color", color);

        JSONObject description =new JSONObject();
        JSONArray tags=new JSONArray();
        for (ClassifierResult result : classifiedImage.getClassifiers()) {
            for (ClassResult classResult : result.getClasses()) {
                if (!classResult.getClassName().endsWith("color")) {
                    tags.add(classResult.getClassName());
                }
            }
        }
        description.put("tags",tags);
        response.put("description",description);

        return String.valueOf(response);
    }

    private String getColor(ClassifiedImage classifiedImage) {
        for (ClassifierResult result : classifiedImage.getClassifiers()) {
            for (ClassResult classResult : result.getClasses()) {
                if (classResult.getClassName().endsWith("color")) {
                    String color = classResult.getClassName();
                    return color.substring(0, color.lastIndexOf("color"));
                }
            }
        }
        return null;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Only POST request are accepted.");
    }
}
