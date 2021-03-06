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

package com.mar.lib.async.rxjava.internal.operators.single;

import java.util.concurrent.Callable;

import com.mar.lib.async.rxjava.*;
import com.mar.lib.async.rxjava.exceptions.Exceptions;
import com.mar.lib.async.rxjava.internal.disposables.EmptyDisposable;

public final class SingleFromCallable<T> extends Single<T> {

    final Callable<? extends T> callable;

    public SingleFromCallable(Callable<? extends T> callable) {
        this.callable = callable;
    }

    @Override
    protected void subscribeActual(SingleObserver<? super T> s) {

        s.onSubscribe(EmptyDisposable.INSTANCE);
        try {
            T v = callable.call();
            if (v != null) {
                s.onSuccess(v);
            } else {
                s.onError(new NullPointerException("The callable returned a null value"));
            }
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            s.onError(e);
        }
    }

}
