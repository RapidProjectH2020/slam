/*******************************************************************************
 * Copyright � 2018 Atos Spain SA. All rights reserved.
 * This file is part of SLAM.
 * SLAM is free software: you can redistribute it and/or modify it under the terms of Apache 2.0
 * THE SOFTWARE IS PROVIDED �AS IS�, WITHOUT ANY WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT, IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * See LICENSE file for full license information in the project root.
 *******************************************************************************/
package eu.atos.sla.datamodel.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;

import eu.atos.sla.datamodel.IAgreement;
import eu.atos.sla.datamodel.IGuaranteeTerm;
import eu.atos.sla.datamodel.IProvider;
import eu.atos.sla.datamodel.IServiceProperties;
import eu.atos.sla.datamodel.ITemplate;

/**
 * A POJO Object that stores all the information from a Agreement
 * 
 * @author Pedro Rey - Atos
 */
@Entity
@Table(name = "agreement")
@NamedQueries({
		@NamedQuery(name = Agreement.QUERY_FIND_ALL, query = "SELECT p FROM Agreement p"),
		@NamedQuery(name = Agreement.QUERY_FIND_BY_AGREEMENT_ID, query = "SELECT p FROM Agreement p where p.agreementId = :agreementId "),
		@NamedQuery(name = Agreement.QUERY_FIND_BY_CONSUMER, query = "SELECT p FROM Agreement p where p.consumer = :consumerId "),
		@NamedQuery(name = Agreement.QUERY_FIND_BY_PROVIDER, query = "SELECT p FROM Agreement p where  p.provider.uuid = :providerUuid "),
		@NamedQuery(name = Agreement.QUERY_FIND_BY_TEMPLATEUUID, query = "SELECT p FROM Agreement p where  p.template.uuid = :templateUUID "),
		@NamedQuery(name = Agreement.QUERY_ACTIVE_AGREEMENTS, query = "SELECT p FROM Agreement p where p.expirationDate > :actualDate "),
		@NamedQuery(name = Agreement.QUERY_FIND_BY_TEMPLATEUUID_AND_CONSUMER, query = "SELECT p FROM Agreement p where (p.template.uuid = :templateUUID) AND (p.consumer = :consumerId)"),
		@NamedQuery(name = Agreement.QUERY_SEARCH, query = "SELECT a FROM Agreement a "
				+ "WHERE (:providerId is null or a.provider.uuid = :providerId) "
				+ "AND (:consumerId is null or a.consumer = :consumerId) "
				+ "AND (:templateId is null or a.template.uuid = :templateId) "
				+ "AND (:active is null "
				+ "    or (:active = true and a.expirationDate > current_timestamp()) "
				+ "    or (:active = false and a.expirationDate <= current_timestamp()))") })

public class Agreement implements IAgreement, Serializable {
	public final static String QUERY_FIND_ALL = "Agreement.findAll";
	public final static String QUERY_FIND_ALL_AGREEMENTS = "Agreement.findAllAgreements";
	public final static String QUERY_FIND_BY_PROVIDER = "Agreement.findByProvider";
	public final static String QUERY_FIND_BY_CONSUMER = "Agreement.findByConsumer";
	public final static String QUERY_FIND_BY_AGREEMENT_ID = "Agreement.getByAgreementId";
	public final static String QUERY_ACTIVE_AGREEMENTS = "Agreement.getActiveAgreements";
	public final static String QUERY_FIND_BY_TEMPLATEUUID = "Agreement.getByTemplateUUID";
	public final static String QUERY_FIND_BY_TEMPLATEUUID_AND_CONSUMER = "Agreement.getByTemplateUUIDAndConsumer";
	public final static String QUERY_SEARCH = "Agreement.search";

	private static final long serialVersionUID = -5939038640423447257L;

	private Long id;
	private String agreementId;
	private String consumer;
	private IProvider provider;
	private ITemplate template;
	private Date expirationDate;
	private AgreementStatus status;
	private String text;
	private List<IServiceProperties> serviceProperties;
	private List<IGuaranteeTerm> guaranteeTerms;
	private String serviceId;
	private Boolean hasGTermToBeEvaluatedAtEndOfEnformcement;
	private String name;
							
	public Agreement() {
	}

	public Agreement(String agreementId) {
		this.agreementId = agreementId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "agreement_id", unique = true)
	public String getAgreementId() {

		return agreementId;
	}

	public void setAgreementId(String agreementId) {

		this.agreementId = agreementId;
	}

	@Column(name = "consumer")
	public String getConsumer() {
		return consumer;
	}

	public void setConsumer(String consumer) {
		this.consumer = consumer;
	}

	@ManyToOne(targetEntity = Provider.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "provider_id", referencedColumnName = "id", nullable = false)
	public IProvider getProvider() {
		return provider;
	}

	public void setProvider(IProvider provider) {
		this.provider = provider;
	}

	@ManyToOne(targetEntity = Template.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "template_id", referencedColumnName = "id", nullable = false)
	public ITemplate getTemplate() {
		return template;
	}

	public void setTemplate(ITemplate template) {
		this.template = template;
	}

	@Column(name = "expiration_time")
	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	@Transient
	public AgreementStatus getStatus() {
		return status;
	}

	public void setStatus(AgreementStatus status) {
		this.status = status;
	}

	@Column(name = "text", columnDefinition = "longtext")
	@Lob
	public String getText() {
		return text;
	}

	@Lob
	public void setText(String text) {
		this.text = text;
	}

	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	@OneToMany(targetEntity = ServiceProperties.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "agreement_id", referencedColumnName = "id", nullable = true)
	public List<IServiceProperties> getServiceProperties() {
		return serviceProperties;
	}

	public void setServiceProperties(List<IServiceProperties> serviceProperties) {
		this.serviceProperties = serviceProperties;
	}

	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	@OneToMany(targetEntity = GuaranteeTerm.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "agreement_id", referencedColumnName = "id", nullable = true)
	public List<IGuaranteeTerm> getGuaranteeTerms() {
		return guaranteeTerms;
	}

	public void setGuaranteeTerms(List<IGuaranteeTerm> guaranteeTerms) {
		this.guaranteeTerms = guaranteeTerms;

	}

	@Column(name = "service_id")
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}


	@Column(name = "metrics_eval_end")
	public Boolean getHasGTermToBeEvaluatedAtEndOfEnformcement() {
		return hasGTermToBeEvaluatedAtEndOfEnformcement;
	}

	public void setHasGTermToBeEvaluatedAtEndOfEnformcement(
			Boolean hasGTermToBeEvaluatedAtEndOfEnformcement) {
		this.hasGTermToBeEvaluatedAtEndOfEnformcement = hasGTermToBeEvaluatedAtEndOfEnformcement;
	}

	@Column(name = "name")
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}