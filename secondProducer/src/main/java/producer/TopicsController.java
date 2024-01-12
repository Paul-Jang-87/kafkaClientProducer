package producer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TopicsController {
	
	@Autowired
    private KafkaProducerApp firstProducer;

    @PostMapping("/topics")
    public String postMessages(@RequestParam List<String> topicNames, @RequestParam List<String> messages) {
        if (topicNames.size() != messages.size()) {
            // Handle error, sizes should match
            return "error";
        }

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        // Send messages asynchronously
        for (int i = 0; i < topicNames.size(); i++) {
            CompletableFuture<Void> future = firstProducer.sendMessageAsync(topicNames.get(i), messages.get(i));
            futures.add(future);
        }

        // Wait for all asynchronous operations to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return "confirmation";
    }
	    
	    @GetMapping("/confirmation")
	    public String hello(Model model) {
	        model.addAttribute("message", "Welcome to Thymeleaf!");
	        return "confirmation";
	    }

}