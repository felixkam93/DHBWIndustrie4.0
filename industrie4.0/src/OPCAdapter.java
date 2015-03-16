import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Locale;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MonitoringMode;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.transport.security.SecurityMode;

import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.SecureIdentityException;
import com.prosysopc.ua.client.MonitoredDataItem;
import com.prosysopc.ua.client.MonitoredDataItemListener;
import com.prosysopc.ua.client.Subscription;
import com.prosysopc.ua.client.UaClient;


public class OPCAdapter implements DataAdapter{
	
	UaClient client;
	
	public OPCAdapter(String url, int namespaceIndex, String identifier ) throws Exception{
		
		// Create client object 
				client = new UaClient(url);
				client.setSecurityMode(SecurityMode.NONE);
				
				initialize(client);
				client.connect();
				DataValue value = client.readValue(Identifiers.Server_ServerStatus_State);

				client.getAddressSpace().setMaxReferencesPerNode(1000);
				NodeId nid = Identifiers.RootFolder; 
				
				List<ReferenceDescription> references = client.getAddressSpace().browse(nid);
				
				// Example of Namespace Browsing 
				NodeId target; 
				ReferenceDescription r = references.get(0);
				
				target = client.getAddressSpace().getNamespaceTable().toNodeId(r.getNodeId()); 
				references = client.getAddressSpace().browse(target);
				r = references.get(4);
				target = client.getAddressSpace().getNamespaceTable().toNodeId(r.getNodeId());
				
				NodeId target2 = new NodeId(namespaceIndex, identifier);
				
				Subscription subscription = new Subscription();
				MonitoredDataItem item = new MonitoredDataItem(target2, Attributes.Value, MonitoringMode.Reporting);
				
				subscription.addItem(item);
				client.addSubscription(subscription);
				
				
				item.setDataChangeListener(new MonitoredDataItemListener() {
					
					@Override
					public void onDataChange(MonitoredDataItem arg0, DataValue arg1,
							DataValue arg2) {
						System.out.println(arg1);
						
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
