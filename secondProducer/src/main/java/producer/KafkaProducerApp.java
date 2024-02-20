package producer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaProducerApp {

	private Properties props = new Properties();

	@PostConstruct
	public void initialize() {//카프카 프로듀서 서버 초기화
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); //서버,포트 설정. (실제로 서버와 포트 번호로 변경될 부분)
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
	}

	@Async
	public CompletableFuture<Void> sendMessageAsync(String topic, String message) {//sendMessage함수를 비동기로 실행
		CompletableFuture<Void> future = new CompletableFuture<>();

		try {
			sendMessage(topic, message);
			future.complete(null);
		} catch (Exception e) {
			future.completeExceptionally(e);
		}

		return future;
	}

	public void sendMessage(String topic, String message) {

		
		Producer<String, String> producer = new KafkaProducer<String, String>(props);

//		String key = ""; 
		String value = "";

		try {

//			SimpleDateFormat form = new SimpleDateFormat("hh:mm:ss");
//			Date now = new Date();
//			String nowtime = form.format(now);

				value = message;

//				ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, key, value);
				ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, value);

				producer.send(record, new Callback() {

					@Override
					public void onCompletion(RecordMetadata metadata, Exception e) {

						SimpleDateFormat form = new SimpleDateFormat("MM/dd::hh/mm/ss");
						Date now = new Date();
						String nowtime = form.format(now);

						if (metadata != null) {

							String infoString = String.format("Success partition : %d, offset : %d",
									metadata.partition(), metadata.offset());
							log.info("{}  {}",nowtime,infoString);

						} else {
							
							 String infoString = String.format("Failed %s", e.getMessage());
		                        log.error(infoString);

		                        int maxRetries = 3;
		                        int retryCount = 0;
		                        while (metadata == null && retryCount < maxRetries) {
		                            log.info("Retrying...");
		                            producer.send(record, this); // 메시지 재전송.
		                            retryCount++;
		                        }

		                        if (metadata == null) {
		                            log.error("Max retries reached. Unable to send the message.");
		                        }
						}

					}
				});


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			producer.close();
		}

	}

}
