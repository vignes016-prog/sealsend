
package com.sealsend.sealsend.controller;
import com.sealsend.sealsend.model.Peer;
import com.sealsend.sealsend.service.PeerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/peers")
public class PeerController {

    private final PeerService peerService;

    public PeerController(PeerService peerService) {
        this.peerService = peerService;
    }

    @GetMapping
    public List<Peer> list() {
        return peerService.all();
    }

    @PostMapping
    public Peer add(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        String ip = (String) body.get("ip");
        int port = Integer.parseInt(String.valueOf(body.get("port")));
        return peerService.add(name, ip, port);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable String id) {
        peerService.remove(id);
    }
}
