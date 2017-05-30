package demo.util;

import org.junit.Assert;
import org.junit.Test;

public class MathUtilTest {

    @Test
    public void test() {
        Assert.assertFalse(MathUtil.getInstance().isPrime(1));
        Assert.assertFalse(MathUtil.getInstance().isPrime(0));
        Assert.assertFalse(MathUtil.getInstance().isPrime(-9));
        Assert.assertTrue(MathUtil.getInstance().isPrime(2));
        Assert.assertTrue(MathUtil.getInstance().isPrime(3));
        Assert.assertFalse(MathUtil.getInstance().isPrime(9));
        Assert.assertFalse(MathUtil.getInstance().isPrime(10));
        Assert.assertTrue(MathUtil.getInstance().isPrime(23));
        Assert.assertFalse(MathUtil.getInstance().isPrime(20000));
        Assert.assertTrue(MathUtil.getInstance().isPrime(977));
    }

}
