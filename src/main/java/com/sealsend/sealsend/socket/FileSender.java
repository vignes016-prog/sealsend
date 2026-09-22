package com.sealsend.sealsend.socket;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;


public class FileSender {

    public static void send(String peerIp, int peerPort, Path fileOnDisk) throws IOException {
        File file = fileOnDisk.toFile();
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
        }

        try (Socket socket = new Socket(peerIp, peerPort)) {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(file.getName());
            out.writeLong(file.length());

            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                byte[] buffer = new byte[4096];
                int read;
                while ((read = bis.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.flush();
            }
        }
    }
}