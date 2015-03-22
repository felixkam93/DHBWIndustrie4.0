package types;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/**
 * Created by D059496 on 16.03.2015.
 */
@XmlRootElement
public class OPCObject {

    @XmlElement
    public String timestamp;
    @XmlElement
    public String sourcename;
    @XmlElement
    public String type;
    @XmlElement
    public Object value;

    public void setValue(String input){

    };




}
