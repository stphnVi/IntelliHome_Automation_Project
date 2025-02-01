package com.example.miprimeraplicacion;

import android.content.ContentResolver;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Socket {
    public static java.net.Socket socket;
    public static PrintWriter out;
    public static Scanner in;
    public static String message;
    public static boolean sistemaInit = false;
    public static DataOutputStream dataOut;

    public static void iniciarSocket() {
        new Thread(() -> {
            try {
                // Cambiar a la dirección IP de su servidor
                socket = new java.net.Socket("192.168.1.24", 1717);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new Scanner(socket.getInputStream());
                dataOut = new DataOutputStream(socket.getOutputStream());
                new Thread(() -> {
                    while (true) {
                        // Escuchar continuamente los mensajes del servidor
                        if (in.hasNextLine()) {
                            message = in.nextLine();
                            //textViewChat.append("Servidor: " + message + "\n");
                        }
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void sendMessage(String message) {
        new Thread(() -> {
            try {
                if (message != null) {
                    out.println(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

//    public static void sendImages(DataOutputStream dataOutputStream, ArrayList<Uri> imageUris, ContentResolver contentResolver) throws IOException {
//        dataOutputStream.writeInt(imageUris.size()); // Send number of images
//
//        for (Uri imageUri : imageUris) {
//            try (InputStream inputStream = contentResolver.openInputStream(imageUri)) {
//                if (inputStream == null) {
//                    System.out.println("Unable to access image: " + imageUri);
//                    continue;
//                }
//
//                byte[] imageBytes = readBytesFromInputStream(inputStream);
//                int fileSize = imageBytes.length;
//                dataOutputStream.writeLong(fileSize); // Send image size
//
//                dataOutputStream.write(imageBytes);
//
//                System.out.println("Sent image from URI: " + imageUri);
//            } catch (Exception e) {
//                System.out.println("Failed to send image from URI: " + imageUri);
//                e.printStackTrace();
//            }
//        }
//
//        System.out.println("All images sent successfully!");
//    }
//
//    private static byte[] readBytesFromInputStream(InputStream inputStream) throws IOException {
//        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//        byte[] data = new byte[4096];
//        int bytesRead;
//
//        while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
//            buffer.write(data, 0, bytesRead);
//        }
//
//        return buffer.toByteArray();
//    }


}