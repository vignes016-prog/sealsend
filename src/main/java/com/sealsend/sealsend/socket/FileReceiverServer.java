
package com.sealsend.sealsend.socket;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class FileReceiverServer {

    @Value("${sealsend.receive-port:9090}")
    private int port;

    @PostConstruct
    public void start() {
        Thread thread = new Thread(this::listen);
        thread.setDaemon(true);
        thread.start();
    }

    private void listen() {
        File saveDir = new File("received_files");
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[FileReceiver] Listening for files on port " + port);
            System.out.println("[FileReceiver] Saving into: " + saveDir.getAbsolutePath());

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    receiveOneFile(socket, saveDir);
                } catch (IOException e) {
                    System.out.println("[FileReceiver] Transfer failed: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("[FileReceiver] Could not start on port " + port + ": " + e.getMessage());
        }
    }

    private void receiveOneFile(Socket socket, File saveDir) throws IOException {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        String fileName = in.readUTF();
        long fileSize = in.readLong();

        System.out.println("[FileReceiver] Receiving " + fileName + " (" + fileSize + " bytes) from "
                + socket.getInetAddress());

        File outFile = new File(saveDir, fileName);
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile))) {
            byte[] buffer = new byte[4096];
            long remaining = fileSize;
            int read;
            while (remaining > 0 && (read = in.read(buffer, 0, (int) Math.min(buffer.length, remaining))) != -1) {
                bos.write(buffer, 0, read);
                remaining -= read;
            }
        }
        System.out.println("[FileReceiver] Saved: " + outFile.getAbsolutePath());
    }
}
