/*******************************************************************************
 * Copyright � 2018 Atos Spain SA. All rights reserved.
 * This file is part of SLAM.
 * SLAM is free software: you can redistribute it and/or modify it under the terms of Apache 2.0
 * THE SOFTWARE IS PROVIDED �AS IS�, WITHOUT ANY WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT, IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * See LICENSE file for full license information in the project root.
 *******************************************************************************/
package eu.atos.sla.evaluation.guarantee;

import java.util.Date;
import java.util.List;

import eu.atos.sla.datamodel.IAgreement;
import eu.atos.sla.datamodel.IGuaranteeTerm;
import eu.atos.sla.datamodel.IViolation;
import eu.atos.sla.monitoring.IMonitoringMetric;

/**
 * Interface for the evaluator of a Service Level Objective.
 * @see IGuaranteeTerm#getServiceLevel() 
 * @author rsosa
 *
 */
public interface IServiceLevelEvaluator {
	
	/**
	 * Evaluate if a list of metrics fulfill the service level of a guarantee term.
	 * 
	 * @param agreement Agreement that contains the guarantee term to evaluate
	 * @param term Guarantee term to evaluate
	 * @param metrics List of metrics to evaluate.
	 * @param now The metrics have been retrieved until now.
	 * @return Detected violations.
	 */
	List<IViolation> evaluate(IAgreement agreement, IGuaranteeTerm term, List<IMonitoringMetric> metrics, Date now);

}
