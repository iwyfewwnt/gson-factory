/*
 * Copyright 2023 iwyfewwnt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.iwyfewwnt.gsonfactory;

import com.google.gson.*;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.reflect.TypeToken;

/**
 * An abstract type adapter factory.
 */
@SuppressWarnings({"unused", "unchecked"})
public abstract class AbstractTypeAdapterFactory implements TypeAdapterFactory {

	/**
	 * Initialize an {@link AbstractTypeAdapterFactory} instance.
	 */
	protected AbstractTypeAdapterFactory() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		Object typeAdapter = this.initTypeAdapter(type.getRawType());

		if (typeAdapter == null) {
			return null;
		}

		if (typeAdapter instanceof TypeAdapter) {
			return (TypeAdapter<T>) typeAdapter;
		}

		JsonSerializer<T> serializer = null;
		if (typeAdapter instanceof JsonSerializer) {
			serializer = (JsonSerializer<T>) typeAdapter;
		}

		JsonDeserializer<T> deserializer = null;
		if (typeAdapter instanceof JsonDeserializer) {
			deserializer = (JsonDeserializer<T>) typeAdapter;
		}

		if (serializer == null && deserializer == null) {
			throw new IllegalStateException("Both the serializer & deserializer are <null>");
		}

		// #nullSafe argument is added since google/gson#2.10
		return new TreeTypeAdapter<>(serializer, deserializer, gson, type, null, false);
	}


	/**
	 * Initialize a type adapter for the provided class.
	 *
	 * @param clazz		type class to initialize a type adapter for
	 * @return			associated type adapter or {@code null}
	 */
	protected abstract Object initTypeAdapter(Class<?> clazz);
}
