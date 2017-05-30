package demo.exception;

/**
 * Basic exception type for aws-interface exception hierarchy for future extension.
 *
 * @author xma11 <maxiaohao@gmail.com>
 * @date 30 May 2017
 *
 */
public class DefaultAwsInterfaceException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    public DefaultAwsInterfaceException(String errMsg) {
        super(errMsg);
    }


    public DefaultAwsInterfaceException(String errMsg, Throwable cause) {
        super(errMsg, cause);
    }

}
