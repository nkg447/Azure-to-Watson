package com.language;

import com.Configuration;
import com.ibm.watson.developer_cloud.language_translator.v2.LanguageTranslator;
import com.ibm.watson.developer_cloud.language_translator.v2.model.IdentifiedLanguage;
import com.ibm.watson.developer_cloud.language_translator.v2.model.IdentifiedLanguages;
import com.ibm.watson.developer_cloud.language_translator.v2.model.IdentifyOptions;

public class Detector {

    static String detect(String text){
        LanguageTranslator service = new LanguageTranslator();

//        create a Configuration.java file containing your username and password
        service.setUsernameAndPassword(Configuration.USERNAME, Configuration.PASSWORD);

        IdentifyOptions identifyOptions=new IdentifyOptions.Builder()
                .text(text)
                .build();

        IdentifiedLanguages identifiedLanguages = service.identify(identifyOptions).execute();

        String language = identifiedLanguages.getLanguages().get(0).getLanguage();
        System.out.println("Detected Language - "+language);
        return language;
    }

}
