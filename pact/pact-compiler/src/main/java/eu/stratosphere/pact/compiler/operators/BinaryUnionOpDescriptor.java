/***********************************************************************************************************************
 *
 * Copyright (C) 2012 by the Stratosphere project (http://stratosphere.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 **********************************************************************************************************************/

package eu.stratosphere.pact.compiler.operators;

import java.util.Collections;
import java.util.List;

import eu.stratosphere.pact.compiler.dataproperties.GlobalProperties;
import eu.stratosphere.pact.compiler.dataproperties.LocalProperties;
import eu.stratosphere.pact.compiler.dataproperties.PartitioningProperty;
import eu.stratosphere.pact.compiler.dataproperties.RequestedGlobalProperties;
import eu.stratosphere.pact.compiler.dataproperties.RequestedLocalProperties;
import eu.stratosphere.pact.compiler.plan.BinaryUnionNode;
import eu.stratosphere.pact.compiler.plan.TwoInputNode;
import eu.stratosphere.pact.compiler.plan.candidate.BinaryUnionPlanNode;
import eu.stratosphere.pact.compiler.plan.candidate.Channel;
import eu.stratosphere.pact.compiler.plan.candidate.DualInputPlanNode;
import eu.stratosphere.pact.runtime.task.DriverStrategy;

/**
 *
 */
public class BinaryUnionOpDescriptor extends OperatorDescriptorDual
{
	/* (non-Javadoc)
	 * @see eu.stratosphere.pact.compiler.dataproperties.DriverProperties#getStrategy()
	 */
	@Override
	public DriverStrategy getStrategy() {
		return DriverStrategy.NONE;
	}
	
	/* (non-Javadoc)
	 * @see eu.stratosphere.pact.compiler.operators.OperatorDescriptorDual#createPossibleGlobalProperties()
	 */
	@Override
	protected List<GlobalPropertiesPair> createPossibleGlobalProperties() {
		// all properties are possible
		return Collections.singletonList(new GlobalPropertiesPair(
			new RequestedGlobalProperties(), new RequestedGlobalProperties()));
	}

	/* (non-Javadoc)
	 * @see eu.stratosphere.pact.compiler.dataproperties.DriverPropertiesDual#createPossibleLocalProperties()
	 */
	@Override
	protected List<LocalPropertiesPair> createPossibleLocalProperties() {
		// all properties are possible
		return Collections.singletonList(new LocalPropertiesPair(
			new RequestedLocalProperties(), new RequestedLocalProperties()));
	}

	/* (non-Javadoc)
	 * @see eu.stratosphere.pact.compiler.dataproperties.DriverPropertiesDual#instantiate(eu.stratosphere.pact.compiler.plan.candidate.Channel, eu.stratosphere.pact.compiler.plan.candidate.Channel, eu.stratosphere.pact.compiler.plan.TwoInputNode)
	 */
	@Override
	public DualInputPlanNode instantiate(Channel in1, Channel in2, TwoInputNode node) {
		return new BinaryUnionPlanNode((BinaryUnionNode) node, in1, in2);
	}

	/* (non-Javadoc)
	 * @see eu.stratosphere.pact.compiler.operators.OperatorDescriptorDual#computeGlobalProperties(eu.stratosphere.pact.compiler.dataproperties.GlobalProperties, eu.stratosphere.pact.compiler.dataproperties.GlobalProperties)
	 */
	@Override
	public GlobalProperties computeGlobalProperties(GlobalProperties in1, GlobalProperties in2) {
		GlobalProperties newProps = new GlobalProperties();
		
		if (in1.getPartitioning() == PartitioningProperty.HASH_PARTITIONED &&
			in2.getPartitioning() == PartitioningProperty.HASH_PARTITIONED &&
			in1.getPartitioningFields().equals(in2.getPartitioningFields()))
		{
			newProps.setHashPartitioned(in1.getPartitioningFields());
		}
		
		return newProps;
	}
	
	/* (non-Javadoc)
	 * @see eu.stratosphere.pact.compiler.operators.OperatorDescriptorDual#computeLocalProperties(eu.stratosphere.pact.compiler.dataproperties.LocalProperties, eu.stratosphere.pact.compiler.dataproperties.LocalProperties)
	 */
	@Override
	public LocalProperties computeLocalProperties(LocalProperties in1, LocalProperties in2) {
		// all local properties are destroyed
		return new LocalProperties();
	}
}