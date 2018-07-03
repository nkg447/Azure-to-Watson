package com.speech;

import com.Util;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.SynthesizeOptions;
import org.jdom2.Element;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@WebServlet(name = "SynthesizeServlet")
public class SynthesizeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Element speak = Util.xmlify(request);
            String text = speak.getChildText("voice", speak.getNamespace());

            InputStream voice = Synthesize.text_to_speech(text);


            ServletOutputStream stream = null;
            BufferedInputStream buf = null;
            try {

                stream = response.getOutputStream();

                //set response headers
                response.setContentType(SynthesizeOptions.Accept.AUDIO_WAV);
                //response.setContentLength((int) mp3.length());

                buf = new BufferedInputStream(voice);
                int readBytes = 0;

                //read from the input stream; write to the ServletOutputStream
                while ((readBytes = buf.read()) != -1)
                    stream.write(readBytes);

                System.out.println("response sent");
            } catch (IOException ioe) {
                throw new ServletException(ioe.getMessage());
            } finally {
                if (stream != null)
                    stream.close();
                if (buf != null)
                    buf.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Only POST request are accepted.");
    }
}
