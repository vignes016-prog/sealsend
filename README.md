# SealSend

A simple peer-to-peer (P2P) file transfer application built with Java and Spring Boot. Files are sent directly from one computer to another over a raw socket connection — no third-party server ever touches the file data.

**Live demo:** [sealsend-1.onrender.com](https://sealsend-1.onrender.com/)
> Note: the demo shows the web UI and peer-management API live. Actual peer-to-peer file transfer requires two machines with open socket ports, so it's best demonstrated locally (see "Getting Started" below) — Render's free tier only exposes the web port, not the file-transfer socket port.

## Features

- **Peer management** — add, list, and remove peers (friends you want to send files to) via a REST API
- **Direct P2P file transfer** — files are streamed sender-to-receiver over a plain TCP socket, not routed through any server
- **Background file receiver** — automatically listens for incoming files the moment the app starts, no manual setup needed
- **Web interface** — a simple HTML/JS front end to add peers, send files, and view received files, all in the browser
- **REST API** — every action (add peer, send file, list received files) is also available as a plain HTTP endpoint, so it's easy to test with Postman or curl

## Tech Stack

- **Backend:** Java, Spring Boot, Spring Web
- **Networking:** Java Sockets (`java.net.Socket`, `ServerSocket`) for the actual file transfer
- **Frontend:** HTML, CSS, JavaScript (vanilla, no framework)
- **Build tool:** Maven

## How It Works

1. **Peer discovery** is handled through Spring Boot REST APIs — you register a peer's name, IP address, and port.
2. **Session management** — every transfer (sent or received) is tracked in memory so the app knows its status.
3. **File transfer** happens outside of Spring entirely: a background socket server (`FileReceiverServer`) listens on a dedicated port (default `9090`) for incoming files, and a `FileSender` utility opens a direct socket connection to a peer and streams the file bytes across.
4. Because the file never passes through a third-party server, there's no upload/download step involving external storage — it's a direct machine-to-machine transfer.

## Ports

| Port | Purpose |
|------|---------|
| `8080` | Web server — REST API and web UI |
| `9090` | Socket server — receives incoming file transfers |

## Getting Started

### Prerequisites
- JDK 17 or higher
- Maven (or use the included Maven wrapper `mvnw`)

### Run locally

```bash
git clone https://github.com/vignes016-prog/sealsend.git
cd sealsend
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`.

### Using the web UI

1. Open `http://localhost:8080` in your browser
2. Add a peer (name, IP address, port)
3. Choose a file and send it to that peer
4. Check the **Received Files** section to view files you've received

### Using the REST API (Postman/curl)

**Add a peer**
```
POST http://localhost:8080/peers
Content-Type: application/json

{ "name": "Friend1", "ip": "192.168.1.5", "port": 9090 }
```

**List peers**
```
GET http://localhost:8080/peers
```

**Send a file**
```
POST http://localhost:8080/send/{peerId}
Body: form-data, key "file", type File
```

**List received files**
```
GET http://localhost:8080/received
```

## Project Structure

```
src/main/java/com/sealsend/sealsend/
├── controller/     # REST endpoints (peers, file sending, received files)
├── service/        # Business logic (peer management)
├── socket/         # Raw socket file sender/receiver
└── model/          # Data models (Peer)
```

## Future Improvements

- Persist peer list and transfer history to a database
- Add transfer progress tracking
- Add file encryption before sending
- Support resuming interrupted transfers

## Author

**Vignesh A** — [GitHub](https://github.com/vignes016-prog)
