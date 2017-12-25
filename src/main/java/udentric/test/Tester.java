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

 public class Tester {
	public static void beginAsync() {
		REPORTER = new AsyncReporter(Thread.currentThread());
	}

	public static void endAsync(int count) {
		REPORTER.waitForCompletion(count);
		REPORTER = SYNC_REPORTER;
	}

	static interface Reporter {
		void addEvent(Event ev);

		void waitForCompletion(int count);
	}

	static final Reporter SYNC_REPORTER = new Reporter() {
		@Override
		public void addEvent(Event ev) {
			if (ev.type == Event.Type.ERROR) {
				throw ev.toAssertionError();
			}
		}

		@Override
		public void waitForCompletion(int count) {
		}
	};

	static Reporter REPORTER = SYNC_REPORTER;
 }
