import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * A tool for efficiently computing very large Fibonnaci numbers using 
 * the matrix form of the Fibonnaci recurrence.
 */
public class Fibonnaci {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		Pattern integer = Pattern.compile("^-?\\d+$"); //Regex for matching integers

		while(true) {
			System.out.println("Please input any integer, N, to compute the Nth Fibonnaci number or q to quit.");
			String input = s.nextLine();

			if(input.equals("q"))
				break;

			Matcher matcher = integer.matcher(input);

			if(matcher.matches()) {
				BigInteger bigInt = new BigInteger(input);
				System.out.println("The " + input + "th Fibonnaci number is: " + fib(bigInt) + "\n");
			} else {
				System.out.println("Not an integer!\n");
			}
		}
	}

	/**
	 * An early attempt that made use of the solution to the Fibonnaci Recurrence.
	 * Such a solution will never be precise enough.
	 */
	public static BigInteger approximateFib(BigInteger input) {
    	int n = input.intValue();
    	MathContext precision = new MathContext(50000, RoundingMode.FLOOR);

    
    	BigDecimal root5 = new BigDecimal(Math.sqrt(5), precision);
    
    	BigDecimal c1 = root5.pow(-1, precision);
    	BigDecimal c2 = c1.negate();

    	BigDecimal r1 = root5.add(new BigDecimal(1.0), precision)
    						 .divide(new BigDecimal(2.0), precision);

    	BigDecimal r2 = root5.negate().add(new BigDecimal(1.0), precision)
    						 .divide(new BigDecimal(2.0), precision);

    	BigDecimal t1 = r1.pow(n, precision).multiply(c1, precision);
    	BigDecimal t2 = r2.pow(n, precision).multiply(c2, precision);

    	BigDecimal fib = t1.add(t2, precision);

    	return fib.toBigInteger();
	}
	
	/**
	 * Computes the nth Fibonacci number where n = <input>. This method works
	 * quickly for very large n with and remains feasible for 
	 * abs(n) in [0, 10000000]. Note that this implementation extended the definition
	 * of Fibonacci numbers to allow for negative inputs.
	 * 
	 * @param  input the n for which you want to determine f(n)
	 * @return       the nth fibonacci number
	 */
	public static BigInteger fib(BigInteger input) {
		BigInteger[][] f1 = {{BigInteger.ONE, BigInteger.ONE}};
		BigInteger[][] t = {{BigInteger.ZERO, BigInteger.ONE},
		                    {BigInteger.ONE, BigInteger.ONE}};
		BigInteger[][] tInverse = {{BigInteger.ONE.negate(), BigInteger.ONE},
	                               {BigInteger.ONE, BigInteger.ZERO}};

		if(input.intValue() == 1)
			return BigInteger.ONE;

		if(input.intValue() < 1)
			return multiply(exp(tInverse, Math.abs(input.intValue())+1), f1)[0][0];
		else
			return multiply(exp(t, input.intValue()-1), f1)[0][0];
	}

	/**
	 * Computes the product of two BigInteger matrices.
	 *
	 * @param  a the first multiplicand
	 * @param  b the second multiplicand
	 * @return   the product of <a> and <b>
	 */
	public static BigInteger[][] multiply(BigInteger[][] a, BigInteger[][] b) {
		if(a.length != b[0].length) //must conform to matrix multiplication
			throw new IllegalArgumentException();

		BigInteger[][] result = new BigInteger[a.length][b[0].length];

		for(int i = 0; i < a[0].length; i++) //ith row of A
			for(int j = 0; j < b.length; j++) //jth column of B
				for(int k = 0; k < a.length; k++) { //kth column of A
					if(result[j][i] == null)
						result[j][i] = BigInteger.ZERO;

					result[j][i] = result[j][i].add(a[k][i].multiply(b[j][k]));
				}
		return result;
	}

	/**
	 * Raises the square BigInteger matrix <a> to the power <n>.
	 *
	 * @param  a the square matrix
	 * @param  n the exponent
	 * @return   the resultant product matrix  
	 */
	public static BigInteger[][] exp(BigInteger[][] a, int n) {
		if(a.length != a[0].length)
			throw new IllegalArgumentException();

		if(n == 1)
			return a;

		if(n % 2 == 1)
			return multiply(a, exp(a, n-1));

		BigInteger[][] x = exp(a, n/2);
		return multiply(x, x);
	}

}