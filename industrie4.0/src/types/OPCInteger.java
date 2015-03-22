package types;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by D059496 on 16.03.2015.
 */
@XmlRootElement
public class OPCInteger extends OPCObject{
    @XmlElement
    public int value;

    public OPCInteger(String inputString, String type){
        this.type = type;
        String[] input = inputString.split(";");
        String valueObjects[] = input[0].split("=");
        value = Integer.parseInt(valueObjects[1]);
        timestamp = input[1].split("=")[1];

        this.timestamp = timestamp;
       // this.value = //konvertiere zu Integer;

    }


    public OPCInteger() {

    }
    public void setValue(String input){
        value = Integer.parseInt(input);

    };
}
