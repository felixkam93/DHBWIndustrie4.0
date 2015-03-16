import types.OPCInteger;

public class Test {
	
	public static void main(String[] args) throws Exception{
		OPCAdapter opcAD1 = new OPCAdapter("opc.tcp://localhost:53530/OPCUA/SimulationServer", 5, "Counter1");
        Thread.sleep(2000);
        for(int i = 0; i < 10; i++){
            String inputComplete = opcAD1.getValue();
            String type = opcAD1.getType();
            OPCInteger opcIntObj = new OPCInteger(inputComplete, type);
            System.out.println("Value: " + opcIntObj.value);
            System.out.println("timestamp: " + opcIntObj.timestamp);
            System.out.println("type: " + opcIntObj.type);

           // System.out.println("Value: " + value);
           // System.out.println("Timestamp: " + timestamp);

           // System.out.println(opcAD1.getValue());
            System.out.println(opcAD1.getValueAlt());
            Thread.sleep(2000);
        }

	}

}
