import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.client.*;
import com.prosysopc.ua.nodes.UaType;
import com.prosysopc.ua.nodes.UaVariable;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.*;
import org.opcfoundation.ua.transport.security.SecurityMode;

import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.SecureIdentityException;
import types.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


public class OPCAdapter implements DataAdapter{
	
	public UaClient client;
  //  private OPCObject opcObj;
    private String currentValue;
    private String currentTimestamp;
    private String alternativeString;
    private String type;
    private Connection connection;
    private Channel channel;

    private Marshaller jaxbMarshaller;
    private Unmarshaller jaxbUnmarshaller;
    private String rabbitMQQueue = "data";
    private String rabbitMQIp = "192.168.0.192";



	public OPCAdapter(String url) throws Exception{

		
		// Create client object 
            client = new UaClient(url);
            client.setSecurityMode(SecurityMode.NONE);

            initialize(client);
            client.connect();
            final DataValue value = client.readValue(Identifiers.Server_ServerStatus_State);

            client.getAddressSpace().setMaxReferencesPerNode(1000);
            NodeId nid = Identifiers.RootFolder;

            List<ReferenceDescription> references = client.getAddressSpace().browse(nid);

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(rabbitMQIp);
            connection = factory.newConnection();
            channel = connection.createChannel();
            JAXBContext opcContext = null;
            try {
                opcContext = JAXBContext.newInstance(OPCDataObject.class);
                jaxbMarshaller = opcContext.createMarshaller();
                jaxbUnmarshaller = opcContext.createUnmarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            } catch (JAXBException e) {
                e.printStackTrace();
            }


        }
	

    public void addItem(int namespaceIndex, String identifier) throws Exception {
        NodeId target2 = new NodeId(namespaceIndex, identifier);

        Subscription subscription = new Subscription();
        MonitoredDataItem item = new MonitoredDataItem(target2, Attributes.Value, MonitoringMode.Reporting);


        subscription.addItem(item);
        client.addSubscription(subscription);
        UaVariable variable = (UaVariable) client.getAddressSpace().getNode(target2);
        NodeId dataTypeId = variable.getDataTypeId();
        UaType dataType = variable.getDataType();
        String dataTypeString = dataType.getDisplayName().getText();
        dataTypeString = dataTypeString.toUpperCase();

        item.setDataChangeListener(new MonitoredDataItemListener() {

            @Override
            public void onDataChange(MonitoredDataItem arg0, DataValue arg1,DataValue arg2) {
               NodeId nodeId = arg0.getNodeId();
                //System.out.println(identifier + ": " + arg1.getValue().toString());
               String value=arg1.getValue().toString();
               String timestamp = arg1.getSourceTimestamp().toString();
                try {
                    UaVariable variable = (UaVariable) client.getAddressSpace().getNode(nodeId);
                    UaType dataType = variable.getDataType();
                    String dataTypeString = dataType.getDisplayName().getText();
                    dataTypeString = dataTypeString.toUpperCase();


                //OPCDataObject<?> opcObj = new OPCDataObject<>();
                if(dataTypeString.equals("INT32")){
                   OPCDataObject<Integer> opcObj = new OPCDataObject<Integer>();
                    opcObj = new OPCDataObject<Integer>();

                    int valueInt = Integer.parseInt(value);
                    opcObj.value = valueInt;
                    opcObj.sourcename = identifier;
                    opcObj.timestamp = timestamp;
                    String XMLresult = this.marshall(opcObj);
                    try {
                        sendToMQ(XMLresult,rabbitMQQueue);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(XMLresult);
                }else if(dataTypeString.equals("DOUBLE")){
                    OPCDataObject<Double> opcObj = new OPCDataObject<>();
                   // opcObj = new OPCDataObject<Double>();
                   // String value = arg1.getValue().toString();
                    double valueDouble = Double.parseDouble(value);
                    opcObj.value = valueDouble;
                    opcObj.sourcename = identifier;
                    opcObj.timestamp = timestamp;
                }else if(dataTypeString.equals("BOOLEAN")){
                    OPCDataObject<Boolean> opcObj = new OPCDataObject<>();
                   //  opcObj = new OPCDataObject<Boolean>();
                   // String value = arg1.getValue().toString();
                    boolean valueBoolean = Boolean.parseBoolean(value);
                    opcObj.value = valueBoolean;
                    opcObj.sourcename = identifier;
                    opcObj.timestamp = timestamp;
                }else if(dataTypeString.equals("STRING")){
                    OPCDataObject<String> opcObj = new OPCDataObject<>();
                     //opcObj = new OPCDataObject<String>();
                    //String value = arg1.getValue().toString();
                    opcObj.value = value;
                    opcObj.sourcename = identifier;
                    opcObj.timestamp = timestamp;
                }

                } catch (ServiceException e) {
                    e.printStackTrace();
                } catch (AddressSpaceException e) {
                    e.printStackTrace();
                }
            }

            private String marshall(OPCDataObject<?> opcObj) {
                java.io.StringWriter sw = new StringWriter();
                String result="";
                try {
                    jaxbMarshaller.marshal(opcObj, sw);
                     result = sw.toString();
                    sw.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return result;
            }
        });



    }

	@Override
	public boolean subscribe() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unsubscribe() {

		return true;
	}


    @Override
    public void sendToMQ(String message, String queueName) throws IOException {
        //falls queue noch nicht existiert
        channel.queueDeclare(queueName, false, false, false, null);
        //sende message im byte format
        channel.basicPublish("", queueName, null, message.getBytes());
        System.out.println(" OPCadapter sent '" + message + "'");


    }

    @Override
    public void closeConnectionMQ() throws Exception {
        channel.close();
        connection.close();
    }


    @Override
    public String convertToXML() throws Exception {
        String result="";
       /* File file = new File("C:\\Users\\D059496\\Desktop\\file.xml");
        JAXBContext jaxbContext = null;



        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        //jaxbMarshaller.marshal(opcObj, file);
        //jaxbMarshaller.marshal(opcObj, System.out);
        java.io.StringWriter sw = new StringWriter();
        jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        jaxbMarshaller.marshal(opcObj, sw);
        String result = sw.toString();
        sw.close();
       // System.out.println(result);
            */

        return result;
    }

    @Override
    public String getValue() {
        String result = "Value=" + currentValue + "; Timestamp=" + currentTimestamp;
        return result;
        //return temp;
    }

   /* public OPCObject getOpcObj() {

        return this.opcObj;
        //return temp;
    }*/

    public  String getValueAlt(){
        return alternativeString;
    }

    public String getType(){
        return type;
    }

    protected void initialize(UaClient client) throws SecureIdentityException, IOException, UnknownHostException {
		// *** Application Description is sent to the server
		ApplicationDescription appDescription = new ApplicationDescription();
		appDescription.setApplicationName(new LocalizedText("DHBW Client",Locale.GERMAN));
		
		// 'localhost' (all lower case) in the URI is converted to the actual
		// host name of the computer in which the application is run
		appDescription.setApplicationUri("urn:localhost:UA:DHBWClient");
		appDescription.setProductUri("urn:prosysopc.com:UA:DHBWClient");
		appDescription.setApplicationType(ApplicationType.Client);

		final ApplicationIdentity identity = new ApplicationIdentity();
		identity.setApplicationDescription(appDescription);
		client.setApplicationIdentity(identity);
	}

}
