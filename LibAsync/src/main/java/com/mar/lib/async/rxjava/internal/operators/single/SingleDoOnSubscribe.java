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

import com.mar.lib.async.rxjava.*;
import com.mar.lib.async.rxjava.disposables.Disposable;
import com.mar.lib.async.rxjava.exceptions.Exceptions;
import com.mar.lib.async.rxjava.functions.Consumer;
import com.mar.lib.async.rxjava.internal.disposables.EmptyDisposable;
import com.mar.lib.async.rxjava.plugins.RxJavaPlugins;

/**
 * Calls a callback when the upstream calls onSubscribe with a disposable.
 *
 * @param <T> the value type
 */
public final class SingleDoOnSubscribe<T> extends Single<T> {

    final SingleSource<T> source;

    final Consumer<? super Disposable> onSubscribe;

    public SingleDoOnSubscribe(SingleSource<T> source, Consumer<? super Disposable> onSubscribe) {
        this.source = source;
        this.onSubscribe = onSubscribe;
    }

    @Override
    protected void subscribeActual(final SingleObserver<? super T> s) {
        source.subscribe(new DoOnSubscribeSingleObserver<T>(s, onSubscribe));
    }

    static final class DoOnSubscribeSingleObserver<T> implements SingleObserver<T> {

        final SingleObserver<? super T> actual;

        final Consumer<? super Disposable> onSubscribe;

        boolean done;

        DoOnSubscribeSingleObserver(SingleObserver<? super T> actual, Consumer<? super Disposable> onSubscribe) {
            this.actual = actual;
            this.onSubscribe = onSubscribe;
        }

        @Override
        public void onSubscribe(Disposable d) {
            try {
                onSubscribe.accept(d);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                done = true;
                d.dispose();
                EmptyDisposable.error(ex, actual);
                return;
            }

            actual.onSubscribe(d);
        }

        @Override
        public void onSuccess(T value) {
            if (done) {
                return;
            }
            actual.onSuccess(value);
        }

        @Override
        public void onError(Throwable e) {
            if (done) {
                RxJavaPlugins.onError(e);
                return;
            }
            actual.onError(e);
        }
    }

}
