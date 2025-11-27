'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var connectingElement = document.querySelector('.connecting');
var messageArea = document.querySelector('#messageArea');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');

var stompClient = null;
var username = null;

document.querySelector('#usernameForm').addEventListener('submit', connect);
messageForm.addEventListener('submit', sendMessage);

document.querySelector("#leaveBtn").addEventListener("click", leaveChat);

/* ---------------- CONNECT ---------------- */
function connect(event) {
    event.preventDefault();

    username = document.querySelector('#name').value.trim();

    if (!username) return;

    usernamePage.classList.add('hidden');
    chatPage.classList.remove('hidden');

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
}

/* ---------------- ON CONNECTED ---------------- */
function onConnected() {
    console.log("Connected!");

    stompClient.subscribe('/topic/chat', onMessageReceived);

    stompClient.send("/app/addUser", {}, JSON.stringify({
        sender: username,
        type: 'JOIN'
    }));

    connectingElement.classList.add('hidden');
}

/* ---------------- ON ERROR ---------------- */
function onError() {
    connectingElement.textContent = "WebSocket connection failed!";
    connectingElement.style.color = "red";
}

/* ---------------- SEND MESSAGE ---------------- */
function sendMessage(event) {
    event.preventDefault();

    var msg = messageInput.value.trim();
    if (!msg) return;

    stompClient.send("/app/sendMessage", {}, JSON.stringify({
        sender: username,
        content: msg,
        type: "CHAT"
    }));

    messageInput.value = "";
}

/* ---------------- RECEIVE MESSAGE ---------------- */
function onMessageReceived(payload) {
    try {
        var msg = JSON.parse(payload.body);
        var li = document.createElement('li');

        switch (msg.type) {
            case 'JOIN':
                li.classList.add('event-message');
                li.textContent = `${msg.sender} joined!`;
                break;
            case 'CHAT':
                li.classList.add(msg.sender === username ? 'self' : 'other');
                li.textContent = `${msg.sender}: ${msg.content}`;
                break;
            case 'LEAVE':
                li.classList.add('leave-message');
                li.textContent = `${msg.sender} left!`;
                break;
            case 'TIME':
                li.classList.add('time-message'); // dedicated class for scheduler time
                li.textContent = msg.content;
                console.log(msg.content);
                break;
        }

        messageArea.appendChild(li);
        messageArea.scrollTop = messageArea.scrollHeight;
    } catch (e) {
        console.error('Error parsing message:', e);
    }
}



function leaveChat() {
    // Disconnect client
    if (stompClient) {
        stompClient.disconnect();
    }

    // Clear all messages from UI
    const messageArea = document.getElementById("messageArea");
    messageArea.innerHTML = "";

    // Return to username page
    chatPage.classList.add("hidden");
    usernamePage.classList.remove("hidden");

    // Optional: clear username field
    document.getElementById("name").value = "";
}
