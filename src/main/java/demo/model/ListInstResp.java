package demo.model;

import java.util.ArrayList;
import java.util.Collection;

import demo.util.MathUtil;

/**
 * A response package for listing the Ec2 instances booted by aws-interface.
 *
 * @author xma11 <maxiaohao@gmail.com>
 * @date 30 May 2017
 *
 */
public class ListInstResp {

    Collection<InstInfo> insts = new ArrayList<InstInfo>();
    boolean isPrime = false;


    public Collection<InstInfo> getInsts() {
        return insts;
    }


    public void setInsts(Collection<InstInfo> insts) {
        this.insts = insts;

        // also set if qty is prime
        this.isPrime = MathUtil.getInstance().isPrime(insts.size());
    }


    public boolean isPrime() {
        return isPrime;
    }


    public void setPrime(boolean isPrime) {
        this.isPrime = isPrime;
    }

}
