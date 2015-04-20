package types;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by D059496 on 19.04.2015.
 */
@XmlRootElement
public class OPCDataObject<T> {


    @XmlElement
    public String timestamp;
    @XmlElement
    public String sourcename;
    @XmlElement
    public T value;

}
