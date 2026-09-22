package com.sealsend.sealsend.controller;

import com.sealsend.sealsend.model.Peer;
import com.sealsend.sealsend.service.PeerService;
import com.sealsend.sealsend.socket.FileSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Postman testing:
 *   POST http://localhost:8080/send/{peerId}
 *   Body -> form-data:
 *      key: file   (type: File)   -> pick a file from your computer
 *
 * Get peerId first from GET http://localhost:8080/peers (add one with POST first).
 */
@RestController
@RequestMapping("/send")
public class TransferController {

    private final PeerService peerService;

    public TransferController(PeerService peerService) {
        this.peerService = peerService;
    }

    @PostMapping("/{peerId}")
    public Map<String, String> sendFile(@PathVariable String peerId,
                                         @RequestParam("file") MultipartFile file) throws IOException {

        Peer peer = peerService.find(peerId)
                .orElseThrow(() -> new IllegalArgumentException("No such peer: " + peerId));

        Path tempFile = Files.createTempFile("sealsend-", "-" + file.getOriginalFilename());
        file.transferTo(tempFile.toFile());

        try {
            FileSender.send(peer.getIp(), peer.getPort(), tempFile);
        } finally {
            Files.deleteIfExists(tempFile);
        }

        return Map.of(
                "status", "sent",
                "to", peer.getName(),
                "file", file.getOriginalFilename()
        );
    }
}