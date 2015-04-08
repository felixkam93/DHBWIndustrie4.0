


	import java.io.File;
    import java.io.StringWriter;
    import javax.xml.bind.JAXBContext;
	import javax.xml.bind.JAXBException;
	import javax.xml.bind.Marshaller;
	 
	public class JavatoXML {
		public static void main(String[] args) {
	 
		  Customer customer = new Customer();
		  customer.setId(101);
		  customer.setName("Felix");
		  customer.setAge(24);
	 
		  try {
	 
			File file = new File("C:\\Users\\Felix\\Desktop\\file.xml");
              //String result = "";
			JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	        Byte result = null;
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	 
			jaxbMarshaller.marshal(customer,file);
			jaxbMarshaller.marshal(customer, System.out);
              java.io.StringWriter sw = new StringWriter();
			jaxbMarshaller.marshal(customer, sw );

		      } catch (JAXBException e) {
			e.printStackTrace();
		      }
	 
		}
	}