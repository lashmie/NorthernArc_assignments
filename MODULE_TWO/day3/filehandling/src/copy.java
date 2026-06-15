//package filehandling;

import java.io.*;
public class copy {
    public static void main(String[] args) {
        String source = "C:\\Guvi_Learning\\MODULE_TWO\\day3\\filehandling\\src\\input.png";
        String destination = "photo_copy.jpg";

        try (
                BufferedInputStream bis =
                        new BufferedInputStream(new FileInputStream(source));
                BufferedOutputStream bos =
                        new BufferedOutputStream(new FileOutputStream(destination))
        ) {
            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            System.out.println("Image copied successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


