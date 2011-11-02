package cn.gamemate.app.taskqueue.amqp;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

import cn.gamemate.app.taskqueue.QueueFactory;

public class AmqpQueueFactory extends QueueFactory{
	
	private Channel channel;
	
	public AmqpQueueFactory() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(getHost());
		try {
			Connection connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare("dota_rep_parsed", false, false, false, null);
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Runnable f = new Runnable() {

			@Override
			public void run() {
				QueueingConsumer consumer = new QueueingConsumer(channel);
				try {
					channel.basicConsume("dota_rep_parsed", true, consumer);
				} catch (IOException e1) {
					throw new RuntimeException(e1);
				}
				
				while(true){
					try {
						QueueingConsumer.Delivery delivery = consumer.nextDelivery(60*1000);
						if (delivery != null){
							String message = new String(delivery.getBody());
							System.out.println("Received '" + message + "'");
						}
					} catch (ShutdownSignalException e) {
						throw new RuntimeException(e);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
				
			}
			
		};
		new Thread(f, "amqpConsumerThread").start();
	}
	
	public String getHost(){
		return System.getProperty("amqp.host", "dev.gamemate.cn");
	}
	
}
