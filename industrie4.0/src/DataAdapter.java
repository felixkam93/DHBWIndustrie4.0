import java.io.IOException;

public interface DataAdapter {
	
	public boolean subscribe();
	
	public boolean unsubscribe();

    public void sendToMQ(String message, String queueName) throws IOException;
    public void closeConnectionMQ() throws  Exception;
    public String convertToXML() throws  Exception;

    public String getValue();
    public String getType();

}
