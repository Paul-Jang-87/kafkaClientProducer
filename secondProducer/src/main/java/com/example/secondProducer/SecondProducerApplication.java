package com.example.secondProducer;

import java.io.IOException;
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
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.mypurecloud.sdk.v2.ApiException;

import genesysapi.ApiConfigure;
import jakarta.annotation.PostConstruct;

@Service
public class SecondProducerApplication {

	private Properties props = new Properties();

	@PostConstruct
	public void initialize() {
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
	}

	@Async
	public CompletableFuture<Void> sendMessageAsync(String topic, String message) {
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
		JSONArray entities = null ;
		ApiConfigure apitest = null;
		JSONObject json = null;
		
		try {
			apitest = new ApiConfigure();
			String url = "https://api.apne2.pure.cloud/api/v2/outbound/campaigns";
			json = apitest.callApiRestTemplate_GET(url);
			entities = json.getJSONArray("entities");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Producer<String, String> producer = new KafkaProducer<String, String>(props);
		System.out.println(topic + ", " + entities);

		String key = "";
		String value = "";

		try {

			SimpleDateFormat form = new SimpleDateFormat("hh:mm:ss");
			Date now = new Date();
			String nowtime = form.format(now);

			for (int i = 0; i < 2; i++) {

				key = String.valueOf(i);
				value = nowtime + " " + String.valueOf(i)+" "+entities+" "+message;

				ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, key, value);

				producer.send(record, new Callback() {

					@Override
					public void onCompletion(RecordMetadata metadata, Exception e) {

						SimpleDateFormat form = new SimpleDateFormat("MM/dd::hh/mm/ss");
						Date now = new Date();
						String nowtime = form.format(now);

						if (metadata != null) {

							String infoString = String.format("Success partition : %d, offset : %d",
									metadata.partition(), metadata.offset());
							System.out.println(nowtime + " " + infoString);

						} else {
							String infoString = String.format("Failed %s", e.getMessage());
							System.err.println(infoString);
						}

					}
				});

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			producer.close();
		}

	}

}
