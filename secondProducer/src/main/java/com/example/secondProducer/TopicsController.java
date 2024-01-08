package com.example.secondProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/topics")
public class TopicsController {
	
	 @Autowired
	    private SecondProducerApplication firstProducer;

	    @PostMapping
	    public String postMessages(@RequestParam String topic_name1,String message1,
	    		String topic_name2,String message2,String topic_name3,String message3) {
	            firstProducer.sendMessage(topic_name1, message1);
	            firstProducer.sendMessage(topic_name2, message2);
	            firstProducer.sendMessage(topic_name3, message3);
	        return "confirmation";
	    }
	    
	    @GetMapping("/hello")
	    public String hello(Model model) {
	        model.addAttribute("message", "Welcome to Thymeleaf!");
	        return "hello";
	    }

}