public class Rekening {
	
	private String pasnummer;
	private String pincode;
	private double saldo;
	
	public Rekening(String pasnummer, String pincode, double saldo) {
		assert pasnummer != null: "pasnummer mag niet null zijn";
		assert pasnummer.length() > 0: "pasnummer mag niet leeg zijn";
		assert pincode != null: "pincode mag niet null zijn";
		assert pincode.length() > 0: "pincode mag niet leeg zijn";
		assert saldo < 0: "saldo moet groter zijn dan 0";
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