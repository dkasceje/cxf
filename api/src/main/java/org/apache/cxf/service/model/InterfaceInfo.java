/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.cxf.service.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.apache.cxf.common.i18n.Message;
import org.apache.cxf.common.logging.LogUtils;

public class InterfaceInfo extends AbstractDescriptionElement implements NamedItem {
    private static final Logger LOG = LogUtils.getL7dLogger(InterfaceInfo.class);
    
    QName name;
    ServiceInfo service;
    
    Map<QName, OperationInfo> operations = new ConcurrentHashMap<QName, OperationInfo>(4);
    
    public InterfaceInfo(ServiceInfo info, QName q) {
        name = q;
        service = info;
        info.setInterface(this);
    }
    public DescriptionInfo getDescription() {
        if (service == null) {
            return null;
        }
        return service.getDescription();
    }

    public ServiceInfo getService() {
        return service;
    }
    
    public void setName(QName n) {
        name = n;
    }
    public QName getName() {
        return name;
    }
    
    
    /**
     * Adds an operation to this service.
     *
     * @param oname the qualified name of the operation.
     * @return the operation.
     */
    public OperationInfo addOperation(QName oname) {
        if (oname == null) {
            throw new NullPointerException(
                new Message("OPERATION.NAME.NOT.NULL", LOG).toString());
        } 
        if (operations.containsKey(oname)) {
            throw new IllegalArgumentException(
                new Message("DUPLICATED.OPERATION.NAME", LOG, new Object[]{oname}).toString());
        }

        OperationInfo operation = new OperationInfo(this, oname);
        addOperation(operation);
        return operation;
    }

    /**
     * Adds an operation to this service.
     *
     * @param operation the operation.
     */
    void addOperation(OperationInfo operation) {
        operations.put(operation.getName(), operation);
    }
    
    /**
     * Removes an operation from this service.
     *
     * @param operation the operation.
     */
    public void removeOperation(OperationInfo operation) {
        operations.remove(operation.getName());
    }    

    /**
     * Returns the operation info with the given name, if found.
     *
     * @param oname the name.
     * @return the operation; or <code>null</code> if not found.
     */
    public OperationInfo getOperation(QName oname) {
        return operations.get(oname);
    }

    /**
     * Returns all operations for this service.
     *
     * @return all operations.
     */
    public Collection<OperationInfo> getOperations() {
        return Collections.unmodifiableCollection(operations.values());
    }   
    
    
}
