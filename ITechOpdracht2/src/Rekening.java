public class Rekening {
	
	private String pasnummer;
	private String pincode;
	private double saldo;
	
	public Rekening(String pasnummer, String pincode, double saldo) {
		this.pasnummer = pasnummer;
		this.pincode = pincode;
		this.saldo = saldo;
	}

	public String getPasnummer() {
		return pasnummer;
	}

	public String getPincode() {
		return pincode;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

}