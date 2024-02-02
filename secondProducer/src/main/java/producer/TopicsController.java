package producer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TopicsController {
	
	@Autowired
    private KafkaProducerApp Producer;

    @PostMapping("/topics")
    public String postMessages(@RequestParam List<String> topicNames, @RequestParam List<String> messages) {
        if (topicNames.size() != messages.size()) {
            // Handle error, sizes should match
            return "error";
        }

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        // Send messages asynchronously
        for (int i = 0; i < topicNames.size(); i++) {
            CompletableFuture<Void> future = Producer.sendMessageAsync(topicNames.get(i), messages.get(i));
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
	    
	    @PostMapping("/gcapi/post/{topic}")
		public String getApiData(@PathVariable("topic") String tranId, @RequestBody String msg) {

			String topic_name = tranId.toUpperCase();

			switch (topic_name) {
			case "firsttopic": //나중에 실제 토픽이름으로 변경.
				
				//다른 토픽에 이 부분 복사 붙여넣기 하면 됨.
				List<CompletableFuture<Void>> futures = new ArrayList<>();
		            CompletableFuture<Void> future = Producer.sendMessageAsync(topic_name, msg);
		            futures.add(future);
		        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
				System.out.println(msg);
				//다른 토픽에 이 부분 복사 붙여넣기 하면 됨.
				
				break;
				
			case "두 번째 토픽 이름":
				
				
				break;
			case "세 번째 토픽 이름":
				
				
				break;
			default:
				break;
			}
			return "confirmation";
		}

}