package com.vision;

import com.Config;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImage;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifierResult;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import java.io.InputStream;

public class ImageClassifier {

    public static ClassifiedImages classify(String url) {
        VisualRecognition service = new VisualRecognition("2018-03-19");
        service.setEndPoint("https://gateway.watsonplatform.net/visual-recognition/api");

//        create a Config.java file containing your API_KEY
        IamOptions options = new IamOptions.Builder().apiKey(Config.Vision.API_KEY).build();
        service.setIamCredentials(options);

        ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                .url(url)
                .build();

        ClassifiedImages result = service.classify(classifyOptions).execute();
        return result;
    }

    public static ClassifiedImages classify(InputStream image) {
        VisualRecognition service = new VisualRecognition("2018-03-19");
        service.setEndPoint("https://gateway.watsonplatform.net/visual-recognition/api");

//        create a Config.java file containing your API_KEY
        IamOptions options = new IamOptions.Builder().apiKey(Config.Vision.API_KEY).build();
        service.setIamCredentials(options);

        ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                .imagesFile(image)
                .build();

        ClassifiedImages result = service.classify(classifyOptions).execute();
        return result;
    }

    public static void main(String[] args) {
        ClassifiedImages classifiedImages = ImageClassifier.classify("https://upload.wikimedia.org/wikipedia/commons/1/12/Broadway_and_Times_Square_by_night.jpg");
        ClassifiedImage image = classifiedImages.getImages().get(0);

        for(ClassifierResult result : image.getClassifiers()){
            System.out.println(result.getClasses());
        }
    }

}
