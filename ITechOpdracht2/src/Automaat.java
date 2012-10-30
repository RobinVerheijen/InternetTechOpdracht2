
public class Automaat {

	private int id;
	private String address;
	
	public Automaat(int theId, String theAddress)	{
		id = theId;
		address = theAddress;
	}
	public static void main(String[] args) throws Exception {
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
