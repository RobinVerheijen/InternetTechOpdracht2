
/**
 * Modelklasse voor rekening.
 *
 */
public class Rekening {
	
	private String pasnummer;
	private String pincode;
	private double saldo;
	
	/**
	 * constructor
	 * @param pasnummer String pasnummer
	 * @param pincode	String pincode
	 * @param saldo		Double saldo
	 */
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

	//getters en setters
	public String getPasnummer() {
		assert pasnummer != null: "pasnummer mag niet null zijn";
		assert pasnummer.length() > 0: "pasnummer mag niet leeg zijn";
		return pasnummer;
	}

	public String getPincode() {
		assert pincode != null: "pincode mag niet null zijn";
		assert pincode.length() > 0: "pincode mag niet leeg zijn";
		return pincode;
	}

	public double getSaldo() {
		assert saldo < 0: "saldo moet groter zijn dan 0";
		return saldo;
	}

	public void setSaldo(double saldo) {
		assert saldo < 0: "saldo moet groter zijn dan 0";
		this.saldo = saldo;
	}

}