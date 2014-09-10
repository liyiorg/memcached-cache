/*
 *    Copyright 2012 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.caches.memcached;

import java.util.HashMap;

import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

/**
 * Setter from String to ConnectionFactory representation.
 *
 * @author Simone Tripodi
 */
final class ConnectionFactorySetter extends AbstractPropertySetter<ConnectionFactory> {


    /**
     * Instantiates a String to ConnectionFactory setter.
     */
    public ConnectionFactorySetter() {
        super("org.mybatis.caches.memcached.connectionfactory",
                "connectionFactory",
                new DefaultConnectionFactory());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ConnectionFactory convert(String property) throws Throwable {
    	String[] values = property.split("?");
    	System.out.println(values[1]);
    	if(values.length == 2){
    		HashMap<String,String> map = new HashMap<String,String>();
    		for(String pv : values[1].split("&")){
    			 String[] pvArray = pv.split("=");
    			 map.put(pvArray[0], pvArray[1]);
    		}
    		System.out.println(map.get("user")+" " + map.get("pwd"));
    		if(map.containsKey("user")&&map.containsKey("pwd")){
    			AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"}, new PlainCallbackHandler(map.get("user"), map.get("pwd")));
    			return new ConnectionFactoryBuilder().setProtocol(Protocol.BINARY)
                        .setAuthDescriptor(ad)
                        .build();
    		}
    	}


    	Class<?> clazz = Class.forName(property);
        if (!ConnectionFactory.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Class '"
                    + clazz.getName()
                    + "' is not a valid '"
                    + ConnectionFactory.class.getName()
                    + "' implementation");
        }
        return (ConnectionFactory) clazz.newInstance();
    }

}
