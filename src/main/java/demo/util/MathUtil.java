package demo.util;

/**
 * Math Utils.
 *
 * @author xma11 <maxiaohao@gmail.com>
 * @date 30 May 2017
 *
 */
public class MathUtil {

    private static final MathUtil singleton = new MathUtil();


    /**
     * forbid constructor for singleton class
     */
    private MathUtil() {

    }


    public static MathUtil getInstance() {
        return singleton;
    }


    /**
     * Return if the given integer is a prime number.
     *
     * @return
     */
    public boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        } else {
            // test if mod(n,i)==0, i ranges in (2..n/2)
            for (int i = 2; i < n / 2 + 1; i++) {
                if (n % i == 0) {
                    return false;
                }
            }
            return true;
        }
    }

}
