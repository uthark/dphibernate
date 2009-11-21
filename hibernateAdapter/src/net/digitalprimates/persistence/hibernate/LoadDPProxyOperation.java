package net.digitalprimates.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import flex.messaging.messages.RemotingMessage;

/**
 * Maps special "loadDPProxy" method to the value specified in the config.
 * @author Marty Pitt
 *
 */
public class LoadDPProxyOperation implements DPHibernateOperation {

	private final String loadMethodName;
	public LoadDPProxyOperation(String loadMethodName)
	{
		this.loadMethodName = loadMethodName;
	}
	public boolean appliesForMessage(RemotingMessage message) {
		return "loadDPProxy".equals(message.getOperation());
	}

	public void execute(RemotingMessage remotingMessage) {
		try
		{
			remotingMessage.setOperation(loadMethodName);
			List paramArray = remotingMessage.getParameters();
			List args = new ArrayList();
			Object param = paramArray.get(1);
			// TODO : Clean this up.
			// I've refactored to try to pass the name of the class, rather than the object
			// itself.  THis is messy, but will be much quicker as save time on needless serialization
			if (param instanceof String)
			{
				// Is it a class name?
				try
				{
					Class targetClass = Class.forName((String) param);
					args.add(targetClass);
				} catch (ClassNotFoundException ex)
				{
					// Not a class name that we know about.
					args.add(paramArray.get(1).getClass());
				}
			}
			else
			{
				//args.add(Class.forName(paramArray.get(1).getClass().getName()));
				args.add(paramArray.get(1).getClass());
			}
			args.add(paramArray.get(0));

			remotingMessage.setParameters(args);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

}