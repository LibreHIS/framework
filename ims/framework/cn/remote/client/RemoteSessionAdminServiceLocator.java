/**
 * RemoteSessionAdminServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package ims.framework.cn.remote.client;

import ims.framework.cn.remote.RemoteSessionAdmin;

public class RemoteSessionAdminServiceLocator extends org.apache.axis.client.Service implements RemoteSessionAdminService {

    /**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public RemoteSessionAdminServiceLocator() {
    }


    public RemoteSessionAdminServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public RemoteSessionAdminServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for RemoteSessionAdminWebService
    private java.lang.String remoteSessionAdminWebService_address = "http://localhost:8080/MichaelsProject/services/RemoteSessionAdminWebService";

    public java.lang.String getRemoteSessionAdminWebServiceAddress() {
        return remoteSessionAdminWebService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MichaelsProjectWebServiceWSDDServiceName = "RemoteSessionAdminWebService";

    public java.lang.String getMichaelsProjectWebServiceWSDDServiceName() {
        return MichaelsProjectWebServiceWSDDServiceName;
    }

    public void setMichaelsProjectWebServiceWSDDServiceName(java.lang.String name) {
        MichaelsProjectWebServiceWSDDServiceName = name;
    }

    public RemoteSessionAdmin getRemoteSessionAdmin() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(remoteSessionAdminWebService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRemoteSessionAdminWebService(endpoint);
    }

    public RemoteSessionAdmin getRemoteSessionAdminWebService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            RemoteSessionAdminSoapBindingStub _stub = new RemoteSessionAdminSoapBindingStub(portAddress, this);
            _stub.setPortName(getMichaelsProjectWebServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMichaelsProjectWebServiceEndpointAddress(java.lang.String address) {
        remoteSessionAdminWebService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (RemoteSessionAdmin.class.isAssignableFrom(serviceEndpointInterface)) {
                RemoteSessionAdminSoapBindingStub _stub = new RemoteSessionAdminSoapBindingStub(new java.net.URL(remoteSessionAdminWebService_address), this);
                _stub.setPortName(getMichaelsProjectWebServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("RemoteSessionAdminWebService".equals(inputPortName)) {
            return getRemoteSessionAdmin();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8080/MichaelsProject/services/RemoteSessionAdminWebService", "MichaelsProjectWebServiceService");
    }

    private java.util.HashSet ports = null;

    @SuppressWarnings("unchecked")
	public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://localhost:8080/MichaelsProject/services/RemoteSessionAdminWebService", "RemoteSessionAdminWebService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("RemoteSessionAdminWebService".equals(portName)) {
            setMichaelsProjectWebServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
