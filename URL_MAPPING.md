# URL MAPPING
<strong>Mapping of all the Microsoft Azure APIs to there respective Watson APIs(v2) and the servlets implementing that API.</strong><br>

    
## Language
    
    Endpoint Host - 
    Azure - "https://api.cognitive.microsofttranslator.com"
    Watson - "https://gateway.watsonplatform.net/language-translator/api/v2"

* Languages<br>
    * Azure path - "/languages"<br>
    * Watson path - "/identifiable_languages"<br>
    * Servlet - com.language.LanguagesServlet<br>


* Language Detection<br>
    * Azure path - "/detect"<br>
    * Watson path - "/identify"<br>
    * Servlet - com.language.DetectServlet<br>

* Text Translation<br>
    * Azure path - "/translate"<br>
    * Watson path - "/translate"<br>
    * Servlet - com.language.TranslateServlet<br>
        
<br><br>
## Speech

* Synthesize Speech ( Text To Speech )<br>
    * Endpoint Host -<br>
        * Azure -<br>
            1. West US - "https://westus.tts.speech.microsoft.com/cognitiveservices/v1"<br>
            2. East Asia - "https://eastasia.tts.speech.microsoft.com/cognitiveservices/v1"<br>
            3. North Europe - "https://northeurope.tts.speech.microsoft.com/cognitiveservices/v1"<br>
        * Watson - "https://gateway.watsonplatform.net/text-to-speech/api/v1"
        <br>
    * Azure path - --not applicable<br>
    * Watson path - "/synthesize"<br>
    * Servlet - com.speech.SynthesizeServlet<br>


* Recognize Speech ( Speech To Text )<br>
    * Endpoint Host -<br>
        * Azure -<br>
            1. West US - "https://westus.stt.speech.microsoft.com/speech/recognition/conversation/cognitiveservices/v1"<br>
            2. East Asia - "https://eastasia.stt.speech.microsoft.com/speech/recognition/conversation/cognitiveservices/v1"<br>
            3. North Europe - "https://northeurope.stt.speech.microsoft.com/speech/recognition/conversation/cognitiveservices/v1"<br>
        * Watson - "https://gateway.watsonplatform.net/speech-to-text/api/v1"
        <br>
    * Azure path - --not applicable<br>
    * Watson path - "/recognize"<br>
    * Servlet - com.speech.RecognizeServlet<br>

## Vision

* Face Detect <br>
    * Endpoint Host -<br>
        * Azure - "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/"<br>
        * Watson - "https://gateway.watsonplatform.net/visual-recognition/api/v3"
        <br>
    * Azure path - "/detect"<br>
    * Watson path - "/detect_faces"<br>
    * Servlet - com.vision.FaceDetectServlet<br>
    
* Classify Image <br>
    * Endpoint Host -<br>
        * Azure - "https://westcentralus.api.cognitive.microsoft.com/vision/v2.0"<br>
        * Watson - "https://gateway.watsonplatform.net/visual-recognition/api/v3"
        <br>
    * Azure path - "/analyze"<br>
    * Watson path - "/classify"<br>
    * Servlet - com.vision.ImageClassifyServlet<br>