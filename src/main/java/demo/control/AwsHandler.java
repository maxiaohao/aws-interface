package demo.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.math.NumberUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStateChange;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;

import demo.exception.DefaultAwsInterfaceException;
import demo.model.InstInfo;

/**
 * AwsClient for doing runInstances, describeInstances and terminateInstances.
 *
 * @author xma11 <maxiaohao@gmail.com>
 * @date 30 May 2017
 *
 */
public class AwsHandler {

    private static AwsHandler singleton = new AwsHandler();


    private AwsHandler() {

    }


    public static AwsHandler getInstance() {
        return singleton;
    }

    /**
     * Ec2 client stub for calling aws.
     */
    private static AmazonEC2 ec2 = null;

    private static String AWS_CREDENTIALS_FILE = "aws-credentials.properties";
    private static String AWS_CONF_FILE = "aws-conf.properties";

    private static String PROP_ENDPOINT = "endpoint";
    private static String PROP_REGION = "region";
    private static String PROP_IMAGE_ID = "default.image";
    private static String PROP_INST_TYPE = "inst.type";
    private static String PROP_MAX_QTY_AT_A_TIME = "max.qty.at.a.time";

    private static Properties awsConfProps = new Properties();

    /**
     * A map containing all the started instances' IDs and names - also restricts the insts that can destroy.
     */
    private static Map<String, InstInfo> instMap = new ConcurrentHashMap<String, InstInfo>();

    static {
        initEc2Client();
    }


    /**
     * Setup the the AmazonEC2 client object from specifications *.properties. <br>
     * This method is called when Ec2Handler is initializing.
     */
    private static void initEc2Client() {

        AWSCredentials credentials = null;

        try {
            // load the aws credentials
            credentials = new PropertiesCredentials(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(AWS_CREDENTIALS_FILE));

            // load the aws conf properties
            awsConfProps.load(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(AWS_CONF_FILE));

        } catch (IOException e) {

        }

        ec2 = AmazonEC2ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(
                        new EndpointConfiguration(awsConfProps.getProperty(PROP_ENDPOINT), awsConfProps
                                .getProperty(PROP_REGION)))
                .build();
    }


    /**
     * Run EC2 instances per qty and name and put info into the map.
     *
     * @param qty
     * @param name
     * @throws DefaultAwsInterfaceException
     */
    public void runInsts(int qty, String name) throws DefaultAwsInterfaceException {
        int maxQtyAtATime = NumberUtils.toInt(awsConfProps.getProperty(PROP_MAX_QTY_AT_A_TIME));
        // check if qty is valid
        if (qty > maxQtyAtATime || qty <= 0) {
            throw new DefaultAwsInterfaceException("invalid qty(" + qty + ")! The allowed qty ranges from 1 to "
                    + maxQtyAtATime);
        }
        RunInstancesRequest req = new RunInstancesRequest(awsConfProps.getProperty(PROP_IMAGE_ID), qty, qty);
        req.setInstanceType(awsConfProps.getProperty(PROP_INST_TYPE));

        RunInstancesResult result = ec2.runInstances(req);

        List<Instance> list = result.getReservation().getInstances();
        if (list != null) {
            for (Instance i : list) {
                InstInfo info = new InstInfo();
                info.setId(i.getInstanceId());
                info.setName(name);
                info.setImage(i.getImageId());
                info.setPublicIp(i.getPublicIpAddress());
                info.setType(i.getInstanceType());
                info.setState(i.getState().getName());
                instMap.put(i.getInstanceId(), info);
            }
        }
    }


    public Collection<InstInfo> describeInsts() {
        DescribeInstancesRequest req = new DescribeInstancesRequest();
        req.setInstanceIds(instMap.keySet());
        DescribeInstancesResult result = ec2.describeInstances(req);
        for (Reservation r : result.getReservations()) {
            List<Instance> list = r.getInstances();
            if (list != null) {
                for (Instance i : list) {
                    // get the "name" from old instInfo
                    String name = null;
                    InstInfo oldInfo = instMap.get(i.getInstanceId());
                    if (null != oldInfo) {
                        name = oldInfo.getName();
                    }
                    if (null == name) {
                        continue;
                    }

                    // update the instInfo in map
                    InstInfo info = new InstInfo();
                    info.setId(i.getInstanceId());
                    info.setName(name);
                    info.setImage(i.getImageId());
                    info.setPublicIp(i.getPublicIpAddress());
                    info.setType(i.getInstanceType());
                    info.setState(i.getState().getName());
                    instMap.put(i.getInstanceId(), info);
                }
            }
        }
        return instMap.values();
    }


    /**
     * Destroy an instance if it was started by aws-interface.
     *
     * @param id
     * @throws DefaultAwsInterfaceException
     */
    public void terminateInst(String id) throws DefaultAwsInterfaceException {
        if (null == instMap.get(id)) {
            throw new DefaultAwsInterfaceException("invalid instance (" + id
                    + ") which was not started by aws-interface");
        }
        List<String> ids = new ArrayList<String>();
        ids.add(id);
        TerminateInstancesRequest req = new TerminateInstancesRequest(ids);
        TerminateInstancesResult result = ec2.terminateInstances(req);

        List<InstanceStateChange> list = result.getTerminatingInstances();
        if (list != null) {
            for (InstanceStateChange isc : list) {
                // remove terminated insts from map
                instMap.remove(isc.getInstanceId());
            }
        }
    }
}
