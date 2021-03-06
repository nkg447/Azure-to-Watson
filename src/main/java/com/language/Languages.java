package com.language;

import com.Config;
import com.ibm.watson.developer_cloud.language_translator.v2.LanguageTranslator;
import com.ibm.watson.developer_cloud.language_translator.v2.model.IdentifiableLanguages;
import com.ibm.watson.developer_cloud.language_translator.v2.model.ListIdentifiableLanguagesOptions;

public class Languages {

    /*
    * Return languages that are identifiable by Watson API
    */
    static IdentifiableLanguages languages() {
        LanguageTranslator service = new LanguageTranslator();

//        create a Config.java file containing your username and password
        service.setUsernameAndPassword(Config.Language.USERNAME, Config.Language.PASSWORD);

        ListIdentifiableLanguagesOptions listOptions = new ListIdentifiableLanguagesOptions.Builder()
                .build();

        IdentifiableLanguages identifiableLanguages = service.listIdentifiableLanguages(listOptions).execute();

        return identifiableLanguages;

    }

}
