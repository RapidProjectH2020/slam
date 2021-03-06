/*******************************************************************************
 * Copyright � 2018 Atos Spain SA. All rights reserved.
 * This file is part of SLAM.
 * SLAM is free software: you can redistribute it and/or modify it under the terms of Apache 2.0
 * THE SOFTWARE IS PROVIDED �AS IS�, WITHOUT ANY WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT, IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * See LICENSE file for full license information in the project root.
 *******************************************************************************/
package eu.atos.sla.datamodel.bean;

import java.io.Serializable;

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
import javax.persistence.Table;

import eu.atos.sla.datamodel.IProvider;
import eu.atos.sla.datamodel.ITemplate;

@Entity
@Table(name = "template")
@NamedQueries({
		@NamedQuery(name = Template.QUERY_FIND_ALL, query = "SELECT t FROM Template t"),
		@NamedQuery(name = Template.QUERY_FIND_BY_UUID, query = "SELECT t FROM Template t WHERE t.uuid = :uuid"),
		@NamedQuery(name = Template.QUERY_SEARCH, query = "SELECT t FROM Template t "
				+ "WHERE (:providerId is null or t.provider.uuid = :providerId) "
				+ "AND (:flagServiceIds is null or t.serviceId in (:serviceIds))"),
		@NamedQuery(name = Template.QUERY_FIND_BY_AGREEMENT, query = "SELECT a.template FROM Agreement a "
				+ "WHERE a.agreementId = :agreementId"),
		})
public class Template implements ITemplate, Serializable {

	public final static String QUERY_FIND_ALL = "Template.findAll";
	public final static String QUERY_FIND_BY_UUID = "Template.getByUuid";
	public final static String QUERY_SEARCH = "Template.search";
	public final static String QUERY_FIND_BY_AGREEMENT = "Template.getByAgreement";
	

	private static final long serialVersionUID = -6390910175637896300L;
	private Long id;
	private String uuid;
	private String text;
	private String serviceId;
	private String name;
	private IProvider provider;

	public Template() {
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

	 @Override
	 public int hashCode() {
		 
		 return uuid.hashCode();
	 }
	 
	 @Override
	 public boolean equals(Object obj) {
		 if (this == obj) {
			 return true;
		 }
	     if (!(obj instanceof Template)) {
	    	 return false;
	     }
	     Template that = (Template) obj;
	     return uuid.equals(that.getUuid());
	}	
	@Column(name = "uuid", unique = true, nullable = false)
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Column(name = "text", columnDefinition = "longtext", nullable = false)
	@Lob
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name = "service_id", nullable = true)
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	@Column(name = "name", nullable = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	@ManyToOne(targetEntity = Provider.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "provider_id", referencedColumnName = "id", nullable = false)
	public IProvider getProvider() {
		return provider;
	}

	public void setProvider(IProvider provider) {
		this.provider = provider;
	}

}
