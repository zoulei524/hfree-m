package me.zoulei.test;

public class TestConstractor {

	final String a;
	
	public TestConstractor() {
		this.a = "112";
	}
	
	public TestConstractor(String a) {
		this();
	}

	public static void main(String[] args) {
		System.out.println(new TestConstractor("asadsa").a);
		long a = 3;
		int b = 7;
		double x = b;
		double c = a/x;
		System.out.println(c);
	}

}
