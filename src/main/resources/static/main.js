var uuid = undefined
var stompClient = undefined

var WebSocketEndPoint = "/ws-prototype"
var AppPrefix = "/booking"
var BrokerPrefix = "/meetingroom/"

function connect(event) {
    if (uuid === undefined) {
        var WebSocket = new SockJS(WebSocketEndPoint);
        stompClient = Stomp.over(WebSocket);
        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}

function sendMessage(event) {
    var chatMessage = {
        sender: uuid,
        content: "Inputs From Web App"
    }
    if (chatMessage && stompClient) {
        stompClient.send(AppPrefix + "/message/" + chatMessage.sender, {}, JSON.stringify(chatMessage));
    }
    event.preventDefault();
}

function onConnected() {
    document.getElementById("entry-page").style.display = "none";
    document.getElementById("main-page").style.display = "block";
    uuid = uuidv4();
    console.log("WebSocket Transport URL: " + stompClient.ws._transport.url)
    stompClient.subscribe(BrokerPrefix + uuid, onMessageReceived);
    var chatMessage = {
        sender: uuid,
        content: "Initial Message"
    }
    stompClient.send(AppPrefix + "/init/" + chatMessage.sender, {}, JSON.stringify(chatMessage))
}

function onError(error) {
    console.log("OnError: " + error);
}

function onMessageReceived(message) {
    var response = JSON.parse(message.body);
    console.log("Response From Server: (" + response.sender + ") " + response.content)
}

function uuidv4() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random() * 16 | 0,
            v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

var entryForm = document.querySelector('#entryForm');
var mainForm = document.querySelector('#mainForm');

entryForm.addEventListener('submit', connect, true)
mainForm.addEventListener('submit', sendMessage, true)