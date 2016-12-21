/**
 * MichaelsProjectWebServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package ims.framework.cn.remote.client;

import ims.framework.cn.remote.RemoteSessionAdmin;

public interface RemoteSessionAdminService extends javax.xml.rpc.Service {
    public java.lang.String getRemoteSessionAdminWebServiceAddress();

    public RemoteSessionAdmin getRemoteSessionAdmin() throws javax.xml.rpc.ServiceException;

    public RemoteSessionAdmin getRemoteSessionAdminWebService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
