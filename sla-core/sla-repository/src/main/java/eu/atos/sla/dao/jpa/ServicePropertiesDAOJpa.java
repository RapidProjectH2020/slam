/*******************************************************************************
 * Copyright � 2018 Atos Spain SA. All rights reserved.
 * This file is part of SLAM.
 * SLAM is free software: you can redistribute it and/or modify it under the terms of Apache 2.0
 * THE SOFTWARE IS PROVIDED �AS IS�, WITHOUT ANY WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT, IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * See LICENSE file for full license information in the project root.
 *******************************************************************************/
package eu.atos.sla.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import eu.atos.sla.dao.IServicePropertiesDAO;
import eu.atos.sla.datamodel.IServiceProperties;
import eu.atos.sla.datamodel.bean.ServiceProperties;

@Repository("ServicePropertiesRepository")
public class ServicePropertiesDAOJpa implements IServicePropertiesDAO {
	private static Logger logger = LoggerFactory.getLogger(ServicePropertiesDAOJpa.class);
	private EntityManager entityManager;

	@PersistenceContext(unitName = "slarepositoryDB")
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public ServiceProperties getById(Long id) {
		return entityManager.find(ServiceProperties.class, id);
	}

	
	@Override
	public IServiceProperties getByName(String name) {
		try {
			TypedQuery<IServiceProperties> query = entityManager.createNamedQuery(
					ServiceProperties.QUERY_FIND_BY_NAME, IServiceProperties.class);
			query.setParameter("name", name);
			IServiceProperties serviceProperties = new ServiceProperties();

			serviceProperties = query.getSingleResult();
			

			return serviceProperties;

		} catch (NoResultException e) {
			logger.debug("No Result found: " + e);
			return null;
		}
	}

	@Override
	public List<IServiceProperties> getAll() {

		TypedQuery<IServiceProperties> query = entityManager.createNamedQuery(
				ServiceProperties.QUERY_FIND_ALL, IServiceProperties.class);
		List<IServiceProperties> services = new ArrayList<IServiceProperties>();
		services = query.getResultList();

		if (services != null) {
			logger.debug("Number of services:" + services.size());
		} else {
			logger.debug("No Result found.");
		}

		return services;
	}

	@Override
	public IServiceProperties save(IServiceProperties serviceProperties){
		
			entityManager.persist(serviceProperties);
			entityManager.flush();
		
		return serviceProperties;
	}

	@Override
	public boolean update(IServiceProperties serviceProperties) {
		entityManager.merge(serviceProperties);
		entityManager.flush();
		return true;
	}

	@Override
	public boolean delete(IServiceProperties serviceProperties) {
		try {
			serviceProperties = entityManager
					.getReference(ServiceProperties.class, serviceProperties.getId());
			entityManager.remove(serviceProperties);
			entityManager.flush();
			return true;
		} catch (EntityNotFoundException e) {
			logger.debug("ServiceProperties[{}] not found", serviceProperties.getId());
			return false;
		}
	}
}
