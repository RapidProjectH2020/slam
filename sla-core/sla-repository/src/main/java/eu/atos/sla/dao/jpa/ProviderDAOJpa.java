/*******************************************************************************
 * Copyright � 2018 Atos Spain SA. All rights reserved.
 * This file is part of SLAM.
 * SLAM is free software: you can redistribute it and/or modify it under the terms of Apache 2.0
 * THE SOFTWARE IS PROVIDED �AS IS�, WITHOUT ANY WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT, IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * See LICENSE file for full license information in the project root.
 *******************************************************************************/
package eu.atos.sla.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;

import eu.atos.sla.dao.IProviderDAO;
import eu.atos.sla.datamodel.IProvider;
import eu.atos.sla.datamodel.bean.Provider;

@Service("ProviderRepository")
public class ProviderDAOJpa implements IProviderDAO {
	private static Logger logger = LoggerFactory.getLogger(ProviderDAOJpa.class);
	private EntityManager entityManager;

	@PersistenceContext(unitName = "slarepositoryDB")
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Provider getById(Long id) {
		return entityManager.find(Provider.class, id);
	}

	@Override
	public Provider getByUUID(String uuid) {
		try {
			Query query = entityManager
					.createNamedQuery(Provider.QUERY_FIND_BY_UUID);
			query.setParameter("uuid", uuid);
			Provider provider = null;

			provider = (Provider) query.getSingleResult();
			System.out.println("Provider name:" + provider.getName());

			return provider;

		} catch (NoResultException e) {
			logger.debug("No Result found: " + e);
			return null;
		}
	}

	@Override
	public IProvider getByName(String name) {
		try {
			Query query = entityManager
					.createNamedQuery(Provider.QUERY_FIND_BY_NAME);
			query.setParameter("name", name);
			Provider provider = null;

			provider = (Provider) query.getSingleResult();
			System.out.println("Provider uuid:" + provider.getUuid());

			return provider;

		} catch (NoResultException e) {
			logger.debug("No Result found: " + e);
			return null;
		}
	}

	@Override
	public IProvider getLastProvider() {
		Query query = entityManager
				.createQuery("SELECT p FROM Provider p order by id DESC");
		query.setMaxResults(1);
		return (Provider) query.getSingleResult();
	}

	@Override
	public List<IProvider> getAll() {
		TypedQuery<IProvider> query = entityManager.createNamedQuery(
				Provider.QUERY_FIND_ALL, IProvider.class);
		List<IProvider> providers = null;
		providers = query.getResultList();

		if (providers != null) {
			logger.debug("Number of providers:" + providers.size());
		} else {
			logger.debug("No Result found.");
		}

		return providers;
	}

	@Override
	@Rollback(false)
	public IProvider save(IProvider provider) {

		entityManager.persist(provider);
		entityManager.flush();

		return provider;
	}

	@Override
	public boolean update(IProvider provider) {
		entityManager.merge(provider);
		entityManager.flush();
		return true;
	}

	@Override
	public boolean delete(IProvider provider) {
		Long id = provider.getId();
		try {
			provider = entityManager.getReference(Provider.class, id);
			entityManager.remove(provider);
			entityManager.flush();
			return true;
		} catch (EntityNotFoundException e) {
			logger.debug("Provider[{}] not found", id);
			return false;
		}
	}
}
