package demo.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ListInstRespTest {

    @Test
    public void test() {
        List<InstInfo> list = new ArrayList<InstInfo>();
        list.add(new InstInfo());
        list.add(new InstInfo());
        list.add(new InstInfo());

        ListInstResp resp1 = new ListInstResp();
        resp1.setInsts(list);
        Assert.assertTrue(resp1.isPrime());

        list.add(new InstInfo());
        resp1.setInsts(list);
        Assert.assertFalse(resp1.isPrime());

    }

}
