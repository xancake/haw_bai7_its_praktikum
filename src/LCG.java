
public class LCG {
	private long x;

	public LCG(long seed) {
		super();
		this.x = seed;
	}

	public int nextInt(){
		int a = 1103515245;
		int b = 12345;
		int m = 99859;
		this.x = (a * x + b) % m;
		
		return (int) this.x;
	}
}
