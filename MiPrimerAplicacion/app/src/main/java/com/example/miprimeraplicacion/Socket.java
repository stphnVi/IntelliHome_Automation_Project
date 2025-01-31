package com.example.miprimeraplicacion;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
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

//    public static void sendImages(DataOutputStream dataOutputStream, String[] imagePaths) throws IOException {
//        dataOutputStream.writeInt(imagePaths.length); // Send number of images
//
//        for (String imagePath : imagePaths) {
//            File imageFile = new File(imagePath);
//
//            if (!imageFile.exists()) {
//                System.out.println("File not found: " + imagePath);
//                continue;
//            }
//
//            long fileSize = imageFile.length();
//            dataOutputStream.writeLong(fileSize); // Send image size
//
//            try (FileInputStream fileInputStream = new FileInputStream(imageFile)) {
//                byte[] buffer = new byte[4096];
//                int bytesRead;
//
//                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
//                    dataOutputStream.write(buffer, 0, bytesRead);
//                }
//            }
//
//            System.out.println("Sent " + imagePath);
//        }
//
//        System.out.println("All images sent successfully!");
//    }



}