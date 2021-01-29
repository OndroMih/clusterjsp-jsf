package clusterjsf;

import java.net.UnknownHostException;
import java.util.*;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.*;

@RequestScoped
@Named
public class Model {

    private static final String ATTRIBUTE_PREFIX = "cluster.";

    @Inject
    private HttpServletRequest request;

    public boolean invalidateSessionIfRequested() {
        String action = request.getParameter("action");
        System.out.println("ClearSession.xhtml: invalidating session");
        if (action != null && action.equals("CLEAR SESSION")) {
            request.getSession().invalidate();
            return true;
        } else {
            return false;
        }
    }

    public void setJSessionIdInstanceCookie() {
        String jsidiname = "JSESSIONIDINSTANCE";
        String jsidivalue = System.getProperty("com.sun.aas.instanceName");
        String jsidipath = request.getContextPath();
        Cookie jsessionIdInstance = new Cookie(jsidiname, jsidivalue);
        jsessionIdInstance.setPath(jsidipath);
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addCookie(jsessionIdInstance);
    }

    public void addDataFromRequestToSession() {
        String dataname = request.getParameter("dataName");
        String datavalue = request.getParameter("dataValue");
        if (dataname != null && datavalue != null && !dataname.equals("")) {
            System.out.println("Add to session: " + dataname + " = " + datavalue);
            request.getSession().setAttribute(ATTRIBUTE_PREFIX + dataname, datavalue);
        }
    }

    public Map<String, String> getSessionAttributes() {
        Map<String, String> result = new HashMap<>();
        Enumeration valueNames = request.getSession().getAttributeNames();
        while (valueNames.hasMoreElements()) {
            String param = (String) valueNames.nextElement();
            if (param.startsWith(ATTRIBUTE_PREFIX))  {
                String value = request.getSession().getAttribute(param).toString();
                result.put(param.substring(ATTRIBUTE_PREFIX.length()), value);
            }
        }
        return result;
    }

    public HttpSession getSession() {
        return request.getSession();
    }

    public Date getSessionLastAccessedTime() {
        return new java.util.Date(request.getSession().getLastAccessedTime());
    }

    public Date getSessionCreationTime() {
        return new java.util.Date(request.getSession().getCreationTime());
    }

    public String getSessionId() {
        return request.getSession(false).getId();
    }

    public String getHostAddress() throws UnknownHostException {
        return java.net.InetAddress.getLocalHost().getHostAddress();
    }

    public String getInstanceName() {
        return System.getProperty("com.sun.aas.instanceName");
    }

    public String getHostName() throws UnknownHostException {
        return java.net.InetAddress.getLocalHost().getHostName();
    }

    public int getServerPort() {
        return request.getServerPort();
    }

    public String getServerName() {
        return request.getServerName();
    }

    public String getContextPath() {
        return request.getContextPath();
    }
}
