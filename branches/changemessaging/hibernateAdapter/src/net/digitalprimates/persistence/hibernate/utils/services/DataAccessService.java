package net.digitalprimates.persistence.hibernate.utils.services;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;

import net.digitalprimates.persistence.state.ObjectChangeMessage;
import net.digitalprimates.persistence.state.ObjectChangeResult;
/**
 * A general purpose DataAccess service which
 * facilitates proxy loading and proxy saving
 * through an IProxyLoadService and IProxyUpdateService respectively.
 * 
 * Can be configured with custom services, or initialized to defaults
 * by simply passing the SesssionFactory
 * @author Marty Pitt
 *
 */
public class DataAccessService implements IProxyUpdateService, IProxyLoadService {

	private final IProxyUpdateService proxyUpdaterService;
	private final IProxyLoadService proxyLoadService;
	public DataAccessService(SessionFactory sessionFactory,IProxyUpdateService proxyUpdateService,IProxyLoadService proxyLoadService)
	{
		this.proxyLoadService = proxyLoadService;
		this.proxyUpdaterService = proxyUpdateService;
	}
	public DataAccessService(SessionFactory sessionFactory)
	{
		proxyLoadService = new ProxyLoadService(sessionFactory);
		proxyUpdaterService = new ProxyUpdaterService(sessionFactory);
	}
	@Override
	public Set<ObjectChangeResult> saveBean(
			List<ObjectChangeMessage> objectChangeMessage) {
		return proxyUpdaterService.saveBean(objectChangeMessage);
	}

	@Override
	public Set<ObjectChangeResult> saveBean(
			ObjectChangeMessage objectChangeMessage) {
		return proxyUpdaterService.saveBean(objectChangeMessage);
	}
	@Override
	public Object loadBean(Class daoClass, Serializable id) {
		return proxyLoadService.loadBean(daoClass, id);
	}

}
