package coffee_machine;

import calculatorv2_core.Equation;

public class Calculator_test {
	
	public int l = 1;
	
	public void testCalculator() {
		Equation eq = new Equation("1+1");
		
		
		System.out.println("---------------");
		
		System.out.println(eq);
	}
	
	public static void test(int l) throws Exception {
		int i = 2;
		int a = 3;
		l = a + l;
		
	}
	
	public static void main(String[] args) {
		Runnable r = new Java_test();
		r.run();
		
		Java_test j = (Java_test) r;
		
		
		j.run();
		j.hello();

		
		double d = 0;
		
		int i = (int) d;
		
		
		try {
			test(5);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		i++;
		
	}
	

}
