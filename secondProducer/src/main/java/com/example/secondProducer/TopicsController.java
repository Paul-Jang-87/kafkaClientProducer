package com.example.secondProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/topics")
public class TopicsController {
	
	@Autowired
    private SecondProducerApplication firstProducer;

    @PostMapping
    public String postMessage(@RequestParam String topic_Name) {
        firstProducer.sendMessage(topic_Name, "YourMessageHere");
        return "confirmation"; // This corresponds to the confirmation.html file
    }

}