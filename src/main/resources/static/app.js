const API_BASE = "";

const peerForm = document.getElementById("peerForm");
const peerMessage = document.getElementById("peerMessage");
const peerList = document.getElementById("peerList");
const peerSelect = document.getElementById("peerSelect");
const refreshBtn = document.getElementById("refreshBtn");
const sendForm = document.getElementById("sendForm");
const sendMessage = document.getElementById("sendMessage");

// ---- Add a peer ----
peerForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const name = document.getElementById("peerName").value;
    const ip = document.getElementById("peerIp").value;
    const port = document.getElementById("peerPort").value;

    try {
        const res = await fetch(`${API_BASE}/peers`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name, ip, port: Number(port) })
        });

        if (!res.ok) {
            const errText = await res.text();
            throw new Error(errText || "Failed to add peer");
        }

        peerMessage.textContent = "Peer added successfully.";
        peerMessage.className = "message success";
        peerForm.reset();
        loadPeers();
    } catch (err) {
        peerMessage.textContent = "Error: " + err.message;
        peerMessage.className = "message error";
    }
});

// ---- Load and display peers ----
async function loadPeers() {
    try {
        const res = await fetch(`${API_BASE}/peers`);
        const peers = await res.json();

        peerList.innerHTML = "";
        peerSelect.innerHTML = '<option value="">-- Select a peer --</option>';

        peers.forEach((peer) => {
            const li = document.createElement("li");
            li.textContent = `${peer.name} (${peer.ip}:${peer.port})`;
            peerList.appendChild(li);

            const option = document.createElement("option");
            option.value = peer.id;
            option.textContent = `${peer.name} (${peer.ip}:${peer.port})`;
            peerSelect.appendChild(option);
        });

        if (peers.length === 0) {
            peerList.innerHTML = "<li>No peers added yet.</li>";
        }
    } catch (err) {
        peerList.innerHTML = "<li>Could not load peers.</li>";
    }
}

refreshBtn.addEventListener("click", loadPeers);

// ---- Send a file ----
sendForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const peerId = document.getElementById("peerSelect").value;
    const file = document.getElementById("fileInput").files[0];

    if (!peerId || !file) {
        sendMessage.textContent = "Please select a peer and a file.";
        sendMessage.className = "message error";
        return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
        const res = await fetch(`${API_BASE}/send/${peerId}`, {
            method: "POST",
            body: formData
        });

        if (!res.ok) {
            const errText = await res.text();
            throw new Error(errText || "Failed to send file");
        }

        const data = await res.json();
        sendMessage.textContent = `Sent "${data.file}" to ${data.to}.`;
        sendMessage.className = "message success";
        sendForm.reset();
    } catch (err) {
        sendMessage.textContent = "Error: " + err.message;
        sendMessage.className = "message error";
    }
});

// Load peers when the page opens
loadPeers();