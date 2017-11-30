/**
 * Copyright (c) 2016-present, RxJava Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package com.mar.lib.async.rxjava.internal.operators.maybe;

import com.mar.lib.async.rxjava.*;
import com.mar.lib.async.rxjava.internal.disposables.EmptyDisposable;
import com.mar.lib.async.rxjava.internal.fuseable.ScalarCallable;

/**
 * Signals an onComplete.
 */
public final class MaybeEmpty extends Maybe<Object> implements ScalarCallable<Object> {

    public static final MaybeEmpty INSTANCE = new MaybeEmpty();

    @Override
    protected void subscribeActual(MaybeObserver<? super Object> observer) {
        EmptyDisposable.complete(observer);
    }

    @Override
    public Object call() {
        return null; // nulls of ScalarCallable are considered empty sources
    }
}
