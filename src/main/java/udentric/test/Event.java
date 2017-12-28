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

import java.util.Arrays;

class Event {
	static Event assertFailure(String message, Throwable cause) {
		return new Event(message, cause);
	}

	static Event assertSuccess() {
		return new Event();
	}

	private Event() {
		type = Type.SUCCESS;
		message = null;
		cause = null;
		origin = null;
		stack = null;
	}

	private Event(String message_, Throwable cause_) {
		type = Type.ERROR;
		message = message_;
		cause = cause_;
		origin = Thread.currentThread();
		stack = origin.getStackTrace();
	}

	boolean isError() {
		return type == Type.ERROR;
	}

	AssertionError toAssertionError() {
		AssertionError err = message != null
			? new AssertionError(message)
			: new AssertionError();

		if (cause != null)
			err.initCause(cause);

		int pos = 0;
		if (stack[pos].getClassName().equals(
			Thread.class.getCanonicalName()
		))
			pos++;

		while (stack[pos].getClassName().equals(
			Event.class.getCanonicalName()
		))
			pos++;

		while (stack[pos].getClassName().equals(
			Assert.class.getCanonicalName()
		))
			pos++;

		err.setStackTrace(Arrays.copyOfRange(stack, pos, stack.length));
		return err;
	}

	enum Type {
		ERROR,
		SUCCESS;
	}

	final Type type;
	final String message;
	final Throwable cause;
	final Thread origin;
	final StackTraceElement[] stack;
}
