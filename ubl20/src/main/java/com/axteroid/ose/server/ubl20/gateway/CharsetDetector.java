package com.axteroid.ose.server.ubl20.gateway;

/**
 * User: RAC
 * Date: 24/05/12
 */

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class CharsetDetector {

    public Charset detectCharset(File f, String[] charsets) {
        Charset charset = null;
        for (String charsetName : charsets) {
            charset = detectCharset(f, Charset.forName(charsetName));
            if (charset != null) {
                break;
            }
        }
        return charset;
    }

    private Charset detectCharset(File f, Charset charset) {
        try {
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(f));

            CharsetDecoder decoder = charset.newDecoder();
            decoder.reset();

            byte[] buffer = new byte[1024];
            boolean identified = false;
            while ((input.read(buffer) != -1)) {
                identified = identify(buffer, decoder);

            }

            input.close();
            if (identified) {
                return charset;
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    private boolean identify(byte[] bytes, CharsetDecoder decoder) {
        try {
            decoder.decode(ByteBuffer.wrap(bytes));
        } catch (CharacterCodingException e) {
            throw new IllegalArgumentException("Error");
        }
        return true;
    }

    public static void main(String[] args) {
        File f = new File("D:\\ebiz_server\\txt\\01-201205023_05_33_46.txt");

        String[] charsetsToBeTested = {"UTF-8", "ISO-8859-1", "windows-1252"};

        CharsetDetector cd = new CharsetDetector();
        Charset charset = cd.detectCharset(f, charsetsToBeTested);

        if (charset != null) {
            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(f), charset);
                int c = 0;
                while ((c = reader.read()) != -1) {
                    System.out.print((char) c);
                }
                reader.close();
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        } else {
            throw new IllegalArgumentException("Charset desconocido");
        }
    }
}
