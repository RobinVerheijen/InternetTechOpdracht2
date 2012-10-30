import static org.junit.Assert.*;

import org.junit.Test;


public class RekeningTest{

	@Test
	public void test() {
		Rekening rekening = new Rekening("200", "200", 200);
		assertNotNull(rekening);
	}
	@Test(expected = AssertionError.class)
	public void PasnummerNull()	{
		Rekening rekening = new Rekening(null, "200", 200);
		assertNotNull(rekening);
	}
	@Test(expected = AssertionError.class)
	public void PasnummerLeeg()	{
		Rekening rekening = new Rekening("", "200", 200);
		assertNotNull(rekening);
	}
	@Test(expected = AssertionError.class)
	public void PincodeNull()	{
		Rekening rekening = new Rekening("214", null, 200);
		assertNotNull(rekening);
	}	
	@Test(expected = AssertionError.class)
	public void PincodeLeeg()	{
		Rekening rekening = new Rekening("214", "", 200);
		assertNotNull(rekening);
	}
	@Test(expected = AssertionError.class)
	public void saldoLagerDanNul()	{
		Rekening rekening = new Rekening("214", "2323", -200);
		assertNotNull(rekening);
	}
	@Test(expected = AssertionError.class)
	public void getNullPasnummer()	{
		Rekening rekening = new Rekening(null, "2323", 200);
		rekening.getPasnummer();
		assertNotNull(rekening);
	}
	@Test(expected = AssertionError.class)
	public void getLeegPasnummer()	{
		Rekening rekening = new Rekening("", "2323", 200);
		rekening.getPasnummer();
		assertNotNull(rekening);
	}
	@Test(expected = AssertionError.class)
	public void getNullPincode()	{
		Rekening rekening = new Rekening("214", null, 200);
		rekening.getPincode();
		assertNotNull(rekening);
	}
	@Test(expected = AssertionError.class)
	public void getLegePincode()	{
		Rekening rekening = new Rekening("214", "", 200);
		rekening.getPincode();
		assertNotNull(rekening);
	}
	@Test(expected = AssertionError.class)
	public void getTeLaagSaldo()	{
		Rekening rekening = new Rekening("214", "2323", -200);
		rekening.getSaldo();
		assertNotNull(rekening);
	}

}
