package cn.gamemate.common;

/**
 * @author jameszhang
 *
 */
public final class PP {
	
	
	private PP() {}
	
	public static void println(byte[] data){
		
			int i = 0;
			for (byte b : data) {
				String t = Integer.toHexString(b & 0xFF).toUpperCase();
				if (t.length() > 1) {
					System.out.print(t.charAt(t.length() - 2));
				} else {
					System.out.print('0');
				}
				System.out.print(t.charAt(t.length() - 1));
				System.out.print(" ");
				i++;
				if (i >= 20) {
					System.out.println();
					i = 0;
				}
				// System.out.printf("%1H ", b);
			}
			System.out.println();
		
	}

}
