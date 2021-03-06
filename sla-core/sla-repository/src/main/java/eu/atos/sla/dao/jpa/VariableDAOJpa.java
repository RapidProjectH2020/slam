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

import eu.atos.sla.dao.IVariableDAO;
import eu.atos.sla.datamodel.IVariable;
import eu.atos.sla.datamodel.bean.Variable;

@Repository("VariableRepository")
public class VariableDAOJpa implements IVariableDAO {
	private static Logger logger = LoggerFactory.getLogger(VariableDAOJpa.class);
	private EntityManager entityManager;

	@PersistenceContext(unitName = "slarepositoryDB")
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Variable getById(Long id) {
		return entityManager.find(Variable.class, id);
	}

	
	@Override
	public IVariable getByName(String name) {
		try {
			TypedQuery<IVariable> query = entityManager.createNamedQuery(
					Variable.QUERY_FIND_BY_NAME, IVariable.class);
			query.setParameter("name", name);
			IVariable variable = new Variable();

			variable = query.getSingleResult();
			

			return variable;

		} catch (NoResultException e) {
			logger.debug("No Result found: " + e);
			return null;
		}
	}

	@Override
	public List<IVariable> getAll() {

		TypedQuery<IVariable> query = entityManager.createNamedQuery(
				Variable.QUERY_FIND_ALL, IVariable.class);
		List<IVariable> services = new ArrayList<IVariable>();
		services = query.getResultList();

		if (services != null) {
			logger.debug("Number of services:" + services.size());
		} else {
			logger.debug("No Result found.");
		}

		return services;
	}

	@Override
	public IVariable save(IVariable variable){
		
			entityManager.persist(variable);
			entityManager.flush();
		
		return variable;
	}

	@Override
	public boolean update(IVariable variable) {
		entityManager.merge(variable);
		entityManager.flush();
		return true;
	}

	@Override
	public boolean delete(IVariable variable) {
		try {
			variable = entityManager
					.getReference(Variable.class, variable.getId());
			entityManager.remove(variable);
			entityManager.flush();
			return true;
		} catch (EntityNotFoundException e) {
			logger.debug("Variable[{}] not found", variable.getId());
			return false;
		}
	}
}
