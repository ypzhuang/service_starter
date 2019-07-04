package com.hptiger.starter.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.hptiger.starter.config.MQTTConfiguration;
import com.mysql.jdbc.StringUtils;

import java.nio.charset.Charset;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;

@Service
public class MqttHandler {
	private static final Logger log = LoggerFactory.getLogger(MqttHandler.class);

	@Autowired
	private MQTTConfiguration config;
	
	private String mqttUser = null;
	private String mqttPassword = null;	
	private String clientId = null;


	private MqttClient mqttClient = null;	
	
	public MqttHandler() {		
	}	
	
	@PostConstruct
	public void init() {
		this.mqttUser = config.getMqttDefaultUser();
		this.mqttPassword = config.getMqttDefaultPassword();
		this.clientId = config.getClientId();
	}

	public void setMqttUser(String mqttUser) {
		this.mqttUser = mqttUser;
	}
	
	public String getMqttUser() {
		if(StringUtils.isEmptyOrWhitespaceOnly(this.mqttUser)) {
			this.mqttUser = null;
		}
		return this.mqttUser;
	}

	public void setMqttPassword(String mqttPassword) {
		this.mqttPassword = mqttPassword;
	}
	
	public String getMqttPassword() {
		if (this.mqttPassword == null)
			this.mqttPassword = "";
		return this.mqttPassword;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	public MqttClient getMqttClient() {
		if (this.mqttClient == null) {
			return this.connect();
		}
		return this.mqttClient;
	}

	public String getClientId() {
		if(StringUtils.isEmptyOrWhitespaceOnly(this.clientId)) {
			this.clientId = MqttClient.generateClientId();
		}
		return this.clientId;
	}
	
	public String getMQTTServer() {
		return this.config.getMqttServers().get(0);		
	}
	
	public String getLastWillTopic() {
		String topic = String.format(this.config.getLastWillTopic(), getClientId());
		log.debug("last will topic: {}",topic);	
		return topic;
	}
	
	public String getDefaultPubTopic() {
		String topic = String.format(this.config.getMqttDefaultPubTopic(), getClientId());
		log.debug("default pub topic: {}",topic);	
		return topic;
	}
	
	public String getDefaultSubTopic() {
		String topic = String.format(this.config.getMqttDefaultSubTopic(), getClientId());
		log.debug("default sub topic: {}",topic);	
		return topic;
	}

	public MqttClient connect(){
		MemoryPersistence persistence = new MemoryPersistence();
		try {
			this.mqttClient = new MqttClient(this.getMQTTServer(), this.getClientId(), persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setAutomaticReconnect(true);			
			connOpts.setServerURIs(this.config.getMqttServers().toArray(new String[0]));
			connOpts.setCleanSession(this.config.cleanSession);
			connOpts.setUserName(this.getMqttUser());
			connOpts.setPassword(this.getMqttPassword().toCharArray());
			connOpts.setConnectionTimeout(config.connectionTimeoutInSeconds);
			connOpts.setKeepAliveInterval(config.keepAliveIntervalInSeconds);
			connOpts.setSocketFactory(SslUtil.getSocketFactory(config.caFilePath, config.clientCrtFilePath, config.clientKeyFilePath, config.clientCrtFilePasswod));			
			connOpts.setWill(this.getLastWillTopic(), this.getLastWillMessge(), 1, false);
			this.mqttClient.connect(connOpts);
			log.debug("mqtt connected");

		} catch (MqttException  me) {
			log.error("error to connect to mqtt server :" +  this.config.getMqttServers(), me);			
		} catch (Exception e) {
			log.error("SslUtil error.",e);	
		}
		return this.mqttClient;
	}
	
	public byte[] getLastWillMessge() {
		Gson gson = new Gson();
		LastWillMessage msg = new LastWillMessage();
		msg.setDeviceId(this.getClientId());
		String strMsg = gson.toJson(msg);
		return strMsg.getBytes(Charset.forName("UTF-8"));		
	}	


	public void publishMessage(String topic, String content, int qos) throws Exception {
		MqttMessage message = new MqttMessage(content.getBytes(Charset.forName("UTF-8")));
		message.setQos(qos);
		getMqttClient().publish(topic, message);
		log.debug("published message: {}", content);
	}

	public void publishMessage(String topic, String content) throws Exception {
		this.publishMessage(topic, content, 1);
	}
	
	public void publishMessage(String content) throws Exception {		
		this.publishMessage(getDefaultPubTopic(), content, 1);
	}

	public void subscribeTopic(String topicFilter) {
		this.subscribeTopic(topicFilter, new IMqttMessageListener() {
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				log.debug("received message on topic: " + topic);
				log.debug("	message id:{}, Qos:{},content:{}", message.getId(), message.getQos(),
						new String(message.getPayload(),Charset.forName("UTF-8")));
			}
		});
	}

	public void subscribeTopic(String topicFilter, IMqttMessageListener listener) {
		try {
			getMqttClient().subscribe(topicFilter, listener);
			log.debug("subscribed topic: {} ", topicFilter);
		} catch (MqttException me) {
			log.error("error to subscribe topic: " + topicFilter, me);
		}
	}
	
	public void subscribeTopic(IMqttMessageListener listener) {
		this.subscribeTopic(getDefaultSubTopic(), listener);
	}

	public void unsubscribeTopic(String topicFilter) {
		try {
			getMqttClient().unsubscribe(topicFilter);
		} catch (MqttException e) {
			log.error("error to unsubscribe topic: " + topicFilter, e);
		}
	}

	
	@PreDestroy
	public void close() {
		try {
			if (mqttClient != null) {
				this.disconnect();
				mqttClient.close();
				log.info("mqtt client closed");
				mqttClient = null;
			}
		} catch (MqttException e) {
			log.error("mqtt client closed error", e);
		}
	}

	public void disconnect() {
		try {
			if (mqttClient.isConnected()) {
				mqttClient.disconnect(10000L);
				log.info("mqtt client disconected");
			}
		} catch (MqttException e) {
			log.error("mqtt client disconnected error", e);
		}
	}
}
