import types.OPCInteger;
import types.OPCObject;

public class Test {
	
	public static void main(String[] args) throws Exception{
		OPCAdapter opcAD1 = new OPCAdapter("opc.tcp://localhost:53530/OPCUA/SimulationServer", 5, "Counter1");
        Thread.sleep(2000);
        for(int i = 0; i < 10; i++){
            OPCInteger opcInt = (OPCInteger) opcAD1.getOpcObj();
            int value = opcInt.value;
            String type = opcInt.type;
            //OPCInteger opcIntObj = new OPCInteger(input, type);
            System.out.println("Value: " + value);
            System.out.println("timestamp: " + opcInt.timestamp);
            System.out.println("type: " + type);
            String xml = opcAD1.convertToXML();
            System.out.println(xml);

           // System.out.println("Value: " + value);
           // System.out.println("Timestamp: " + timestamp);


            System.out.println(opcAD1.getValueAlt());
            Thread.sleep(2000);
        }

	}

}
