package com.example.secondProducer;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TopicsController {
	
	 @Autowired
	    private SecondProducerApplication firstProducer;

	 	@PostMapping("/topics")
	    public String postMessages( @RequestParam(name = "topic_name1") String topicName1, @RequestParam(name = "message1") String message1,
	            @RequestParam(name = "topic_name2") String topicName2, @RequestParam(name = "message2") String message2,
	            @RequestParam(name = "topic_name3") String topicName3, @RequestParam(name = "message3") String message3) {
	            
	            CompletableFuture<Void> future1 = firstProducer.sendMessageAsync(topicName1, message1);
	            CompletableFuture<Void> future2 = firstProducer.sendMessageAsync(topicName2, message2);
	            CompletableFuture<Void> future3 = firstProducer.sendMessageAsync(topicName3, message3);

	            // Wait for all asynchronous operations to complete
	            CompletableFuture.allOf(future1, future2, future3).join();
	            
	        return "confirmation";
	    }
	    
	    @GetMapping("/confirmation")
	    public String hello(Model model) {
	        model.addAttribute("message", "Welcome to Thymeleaf!");
	        return "confirmation";
	    }

}