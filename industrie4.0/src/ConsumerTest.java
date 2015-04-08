import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by Felix on 08.04.2015.
 */
public class ConsumerTest {
    private Connection connection;
    public Channel channel;

    public static void main(String args[]) throws Exception{

        ConsumerTest test = new ConsumerTest();

        test.doTest();
        Thread.sleep(30000);
        test.channel.basicCancel("felix-pc");

    }

    public void doTest()throws Exception{



        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.27");
        connection = factory.newConnection();
        channel = connection.createChannel();


        boolean autoAck = false;
        channel.basicConsume("data", autoAck, "felix-pc",

                new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,byte[] body)throws IOException{
                        String routingKey = envelope.getRoutingKey();
                        //String contentType = properties.contentType;
                        long deliveryTag = envelope.getDeliveryTag();
                        String answer = new String(body, "UTF-8");
                        System.out.println(answer);
                        // (process the message components here ...)
                        channel.basicAck(deliveryTag, false);
                    }
                });



    }
}
