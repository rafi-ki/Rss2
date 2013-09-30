/*
 * Copyright (C) 2012 the diamond:dogs|group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.diamonddogs.example.http.factory;

import at.diamonddogs.example.http.dataobject.NonTimeCriticalExampleConfiguration;
import at.diamonddogs.nontimecritical.NonTimeCriticalTaskQueue.NonTimeCriticalTaskQueueConfiguration;
import at.diamonddogs.nontimecritical.NonTimeCriticalTaskQueue.NonTimeCriticalTaskQueueConfigurationFactory;

/**
 * A custom implementation of the {@link NonTimeCriticalExampleConfigFactory}
 * interface, that returns an instance of our custom made
 * {@link NonTimeCriticalExampleConfiguration}
 */
public class NonTimeCriticalExampleConfigFactory implements NonTimeCriticalTaskQueueConfigurationFactory {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NonTimeCriticalTaskQueueConfiguration newInstance() {
		return new NonTimeCriticalExampleConfiguration();
	}

}
