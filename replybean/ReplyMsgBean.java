/*
Karan Thaker
MDB - Two Servers 
This class is a message-driven bean that implements the message listener interface.
*/


import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.ActivationConfigProperty;
import javax.jms.ConnectionFactory;
import javax.jms.Topic;
import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.MessageProducer;
import javax.jms.MessageListener;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.JMSException;
import javax.annotation.Resource;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.logging.Logger;


@MessageDriven(mappedName = "jms/Topic")
public class ReplyMsgBean implements MessageListener {
    static final Logger logger = Logger.getLogger("ReplyMsgBean");
    @Resource(mappedName = "jms/ConnectionFactory")
    public ConnectionFactory connectionFactory;
    @Resource
    public MessageDrivenContext mdc;
    Connection con = null;

    public ReplyMsgBean() {
    }

    // Creates the connection.
    @PostConstruct
    public void makeConnection() {
        try {
            con = connectionFactory.createConnection();
        } catch (Throwable t) {
            logger.severe(
                    "ReplyMsgBean.makeConnection:" + "Exception: "
                    + t.toString());
        }
    }

    public void onMessage(Message inMessage) {
        TextMessage msg = null;
        Session ses = null;
        MessageProducer producer = null;
        TextMessage replyMsg = null;

        try {
            if (inMessage instanceof TextMessage) {
                msg = (TextMessage) inMessage;
                logger.info("ReplyMsgBean: Received message: " + msg.getText());
                con = connectionFactory.createConnection();
                ses = con.createSession(true, 0);

                producer = ses.createProducer((Topic) msg.getJMSReplyTo());
                replyMsg = ses.createTextMessage(
                            "ReplyMsgBean " + "processed message: "
                            + msg.getText());
                replyMsg.setJMSCorrelationID(msg.getJMSMessageID());
                replyMsg.setIntProperty(
                    "id",
                    msg.getIntProperty("id"));
                producer.send(replyMsg);
                con.close();
            } else {
                logger.warning(
                        "Message of wrong type: "
                        + inMessage.getClass().getName());
            }
        } catch (JMSException e) {
            logger.severe(
                    "ReplyMsgBean.onMessage: JMSException: " + e.toString());
        } catch (Throwable te) {
            logger.severe(
                    "ReplyMsgBean.onMessage: Exception: " + te.toString());
        }
    }

    
     //Closes the connection.
    @PreDestroy
    public void endConnection() throws RuntimeException {
        System.out.println("In ReplyMsgBean.endConnection()");

        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
