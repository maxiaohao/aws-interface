package demo.control;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;

import demo.exception.DefaultAwsInterfaceException;
import demo.model.InstInfo;
import demo.model.ListInstResp;
import demo.util.CrudResult;
import demo.util.JsonUtils;

/**
 * Ajax request handler servlet as a json printer as well.
 *
 * @author xma11 <maxiaohao@gmail.com>
 * @date 31 May 2017
 *
 */
public class MainServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String ACTION_DESCRIBE_INSTANCES = "describeInstances";
    private static final String ACTION_RUN_INSTANCES = "runInstances";
    private static final String ACTION_TERMINATE_INSTANCES = "terminateInstances";


    /**
     * Default constructor.
     */
    public MainServlet() {
        // TODO Auto-generated constructor stub
    }


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        response.setContentType("text/json");
        String action = request.getParameter("action");

        if (ACTION_DESCRIBE_INSTANCES.equals(action)) {
            ListInstResp resp = new ListInstResp();
            Collection<InstInfo> insts = AwsHandler.getInstance().describeInsts();
            resp.setInsts(insts);
            JsonUtils.writeAsJson(response.getWriter(), new CrudResult(true, resp));
            return;
        } else if (ACTION_RUN_INSTANCES.equals(action)) {
            String qtyStr = request.getParameter("qty");
            String name = request.getParameter("name");
            if (null == qtyStr || qtyStr.length() == 0 || !NumberUtils.isNumber(qtyStr)
                    || NumberUtils.toInt(qtyStr) < 1) {
                JsonUtils.writeAsJson(response.getWriter(), new CrudResult(false, "invalid input: qty"));
                return;
            }
            if (null == qtyStr || qtyStr.length() == 0 || !NumberUtils.isNumber(qtyStr)
                    || NumberUtils.toInt(qtyStr) < 1) {
                JsonUtils.writeAsJson(response.getWriter(), new CrudResult(false, "invalid input: name"));
                return;
            }

            try {
                AwsHandler.getInstance().runInsts(NumberUtils.toInt(qtyStr), name);
                JsonUtils.writeAsJson(response.getWriter(), new CrudResult(true));
                return;
            } catch (DefaultAwsInterfaceException e) {
                JsonUtils.writeAsJson(response.getWriter(), new CrudResult(false, "Error: " + e.getMessage()));
                return;
            }

        } else if (ACTION_TERMINATE_INSTANCES.equals(action)) {
            String id = request.getParameter("id");
            if (null == id || id.length() == 0) {
                JsonUtils.writeAsJson(response.getWriter(), new CrudResult(false, "invalid instance id: " + id));
                return;
            }
            try {
                AwsHandler.getInstance().terminateInst(id);
                JsonUtils.writeAsJson(response.getWriter(), new CrudResult(true));
                return;
            } catch (DefaultAwsInterfaceException e) {
                JsonUtils.writeAsJson(response.getWriter(), new CrudResult(false, "Error: " + e.getMessage()));
                return;
            }
        }

    }
}
