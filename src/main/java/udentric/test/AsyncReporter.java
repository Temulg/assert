/*
 * Copyright (c) 2017 Alex Dubov <oakad@yahoo.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package udentric.test;

import java.util.concurrent.LinkedTransferQueue;

class AsyncReporter implements Tester.Reporter {
	AsyncReporter(Thread mainThread_) {
		mainThread = mainThread_;
	}

	@Override
	public void addEvent(Event ev) {
		boolean otherThread = Thread.currentThread() != mainThread;

		if (otherThread)
			eventQueue.put(ev);

		if (ev.isError())
			abortTest = true;

		if (abortTest) {
			if (otherThread)
				throw new TestAbortedError();
			else
				throw ev.toAssertionError();
		}
	}

	@Override
	public void waitForCompletion(int count) {
		while (count > 0) {
			try {
				Event ev = eventQueue.take();
				if (ev.isError()) {
					throw ev.toAssertionError();
				} else
					count--;
			} catch (InterruptedException e) {
				throw new AssertionError(
					"test interrupted", e
				);
			}
		}
	}

	private final Thread mainThread;
	private final LinkedTransferQueue<
		Event
	> eventQueue = new LinkedTransferQueue<>();
	private volatile boolean abortTest = false;
}
