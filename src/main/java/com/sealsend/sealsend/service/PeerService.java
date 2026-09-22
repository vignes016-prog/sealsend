package com.sealsend.sealsend.service;

import com.sealsend.sealsend.model.Peer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;


@Service
public class PeerService {

    private final List<Peer> peers = new CopyOnWriteArrayList<>();

    public List<Peer> all() {
        return peers;
    }

    public Peer add(String name, String ip, int port) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Please enter a name for your friend.");
        }
        if (ip == null || ip.isBlank()) {
            throw new IllegalArgumentException("Please enter your friend's IP address.");
        }
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Port must be between 1 and 65535.");
        }
        Peer peer = new Peer(name.trim(), ip.trim(), port);
        peers.add(peer);
        return peer;
    }

    public Optional<Peer> find(String id) {
        return peers.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public void remove(String id) {
        peers.removeIf(p -> p.getId().equals(id));
    }
}