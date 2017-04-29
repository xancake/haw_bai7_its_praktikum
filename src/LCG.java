
public class LCG {
	private long _x;

	public LCG(long seed) {
		_x = seed;
	}

	public int nextInt() {
		int a = 1103515245;
		int b = 12345;
		int m = 99859;
		_x = (a * _x + b) % m;
		return (int)_x;
	}
	
	public static void main(String... args) {
		long seed = 3529757982L;
		
		switch(args.length) {
			case 0: {
				break;
			}
			case 1: {
				seed = Long.parseLong(args[0]);
				break;
			}
			default: {
				System.out.println("Usage: LCG [seed]");
				System.exit(1);
			}
		}
		
		LCG testLCG = new LCG(seed);
		for (int i = 0; i < 255; i++) {
			System.out.println(testLCG.nextInt() & 0x000000FF);
		}
	}
}
