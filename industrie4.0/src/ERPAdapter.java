
public class ERPAdapter implements DataAdapter {
	
	public ERPAdapter(){
		
	}

	@Override
	public boolean subscribe() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unsubscribe() {
		// TODO Auto-generated method stub
		return false;
	}

    @Override
    public void sendToMQ(String message, String queueName) {

    }

    @Override
    public void closeConnectionMQ() {

    }

    @Override
    public String convertToXML() {
        return null;
    }

    @Override
    public String getValue() {
        String result = "";
        return result;
    }

    @Override
    public String getType() {
        return null;
    }

}
