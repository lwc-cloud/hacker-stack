var source = new EventSource(remote + '/ai_chat/你好呀，帮我写一个Java Hello World');
        source.onmessage = function(event) {
            console.log(event.data)
        };
        source.onerror = function(event) {
            console.error("EventSource failed:", event);
        };