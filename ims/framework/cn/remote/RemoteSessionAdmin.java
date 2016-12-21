/**
 * RemoteSessionAdminWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package ims.framework.cn.remote;

public interface RemoteSessionAdmin extends java.rmi.Remote {
    public String login(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
    public void logout() throws java.rmi.RemoteException;
    public void logout(java.lang.String sessionToken) throws java.rmi.RemoteException;
    public java.lang.String[] listSessionsWS() throws java.rmi.RemoteException;
    public void terminateSessionWS(java.lang.String sessionID) throws java.rmi.RemoteException;
    public java.lang.String toggleLoggingWS(String sessionID) throws java.rmi.RemoteException;
    public java.lang.String getLogFileWS(java.lang.String sessionID, Integer tailSize) throws java.rmi.RemoteException;
}
