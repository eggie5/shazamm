import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class AudioDataBuffer {

    public static void main(String[] args) throws Exception {

        File sourceFile = new File("voice.wav");
        File targetFile = new File("target");

        /* Get the type of the source file. We need this information
           later to write the audio data to a file of the same type.
        */
        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(sourceFile);

        /* Read the audio data into a memory buffer.
         */
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(
                new FileInputStream("voice.wav")));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int frameLength = (int) audioInputStream.getFrameLength();
        int frameSize = audioInputStream.getFormat().getFrameSize();
        byte[] bytes = new byte[frameLength * frameSize];

        int result = 0;
        try {
            result = audioInputStream.read(bytes);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        int numChannels = audioInputStream.getFormat().getChannels();
        System.out.println("Channels found: "+numChannels);
        int frameLength1 = (int) audioInputStream.getFrameLength();
        System.out.println("Frame Length: "+frameLength1);
        int[][] toReturn = new int[numChannels][frameLength1];

        int sampleIndex = 0;
        for (int t = 0; t < bytes.length;) {
            for (int channel = 0; channel < numChannels; channel++) {
                int low = bytes[t];
                t++;
                int high = bytes[t];
                t++;
                int sample = getSixteenBitSample(high, low);
                toReturn[channel][sampleIndex] = sample;
            }
            sampleIndex++;
        }

        PrintWriter writer = new PrintWriter(targetFile);
        for (int i = 0; i < toReturn[0].length; i++) {
            System.out.println(toReturn[0][i]);
            writer.println(String.valueOf(toReturn[0][i]));
        }
        writer.close();

        System.out.println("done");
    }

    private static int getSixteenBitSample(int high, int low) {
        return (high << 8) + (low & 0x0ff);

    }

}
