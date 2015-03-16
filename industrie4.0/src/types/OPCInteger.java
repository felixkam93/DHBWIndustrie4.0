package types;

/**
 * Created by D059496 on 16.03.2015.
 */
public class OPCInteger extends OPCObject{

    public int value;
    public String timestamp;
    public String type;

    public OPCInteger(String inputString, String type){
        this.type = type;
        String[] input = inputString.split(";");
        String valueObjects[] = input[0].split("=");
        value = Integer.parseInt(valueObjects[1]);
        timestamp = input[1].split("=")[1];

        this.timestamp = timestamp;
       // this.value = //konvertiere zu Integer;

    }


}
