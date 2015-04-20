import types.OPCBoolean;
import types.OPCInteger;
import types.OPCObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Test {
	
	public static void main(String[] args) throws Exception{

        ArrayList<String> sensors = new ArrayList<>();
        ArrayList<OPCAdapter> adapters = new ArrayList<>();
        BufferedReader datei = new BufferedReader(new FileReader("C://Users/D059496/Desktop/input2.txt"));
        String zeile = datei.readLine();
        //OPCAdapter opcAD1 = new OPCAdapter("opc.tcp://192.168.0.102:49320");
        OPCAdapter opcAD1 = new OPCAdapter("opc.tcp://WDFN00291103A:53530/OPCUA/SimulationServer");
        opcAD1.addItem(5, "Counter1");

        /*while(zeile != null){
            System.out.print(zeile);
            //opcAD1.addItem(2, zeile);
            opcAD1.addItem(5, zeile);
             //OPCAdapter opcAD1 = new OPCAdapter("opc.tcp://192.168.0.102:49320", 2, zeile);
            //adapters.add(opcAD1);
            zeile = datei.readLine();
        }
        datei.close();*/




        while (true){


        }

	}

}
