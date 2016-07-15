/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wikipediaminer.wikipedia.miner.indonesian.model;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

/**
 *
 * @author Rakhmatullah Yoga S
 */
public class SentenceModelBuilder {

    /**
     * @param inputFile contains training data
     * @param modelFile Generated model file after training
     * @throws IOException
     */
    public static void trainModel(String inputFile, String modelFile) throws IOException {
        Charset charset = Charset.forName("UTF-8");				
        ObjectStream<String> lineStream =
                new PlainTextByLineStream(new FileInputStream(inputFile), charset);
        ObjectStream<SentenceSample> sampleStream = new SentenceSampleStream(lineStream);

        SentenceModel model;

        try {
            model = SentenceDetectorME.train("id", sampleStream, true, null);
        } finally {
            sampleStream.close();
        }

        OutputStream modelOut = null;
        try {
            modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
            model.serialize(modelOut);
        } finally {
            if (modelOut != null) 
                modelOut.close();      
        }
    }
    
    public static void main(String[] args) {
        try {
            trainModel("1000news-sentences.txt", "id-sent.bin");
        } catch (IOException ex) {
            Logger.getLogger(SentenceModelBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
