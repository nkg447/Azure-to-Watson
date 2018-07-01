package com.language;

import com.Config;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslateOptions;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslationResult;
import com.ibm.watson.developer_cloud.language_translator.v2.LanguageTranslator;

public class Translator {

    /*
    * Translating text using the Watson API
     */
    static TranslationResult translate(String text, String sourceLanguage, String toLang) {
        LanguageTranslator service = new LanguageTranslator();

//        create a Config.java file containing your username and password
        service.setUsernameAndPassword(Config.Language.USERNAME, Config.Language.PASSWORD);

        TranslateOptions translateOptions = new TranslateOptions.Builder()
                .addText(text)
                .source(sourceLanguage)
                .target(toLang)
                .build();
        TranslationResult translationResult = service.translate(translateOptions).execute();

        System.out.println(translationResult);

        return translationResult;
    }
}
