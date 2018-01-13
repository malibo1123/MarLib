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

package com.mar.lib.async.rxjava.internal.operators.observable;

import com.mar.lib.async.rxjava.*;
import com.mar.lib.async.rxjava.annotations.Experimental;
import com.mar.lib.async.rxjava.annotations.Nullable;
import com.mar.lib.async.rxjava.disposables.Disposable;
import com.mar.lib.async.rxjava.exceptions.Exceptions;
import com.mar.lib.async.rxjava.functions.Action;
import com.mar.lib.async.rxjava.internal.disposables.DisposableHelper;
import com.mar.lib.async.rxjava.internal.fuseable.*;
import com.mar.lib.async.rxjava.internal.observers.BasicIntQueueDisposable;
import com.mar.lib.async.rxjava.plugins.RxJavaPlugins;

/**
 * Execute an action after an onError, onComplete or a dispose event.
 *
 * @param <T> the value type
 * @since 2.0.1 - experimental
 */
@Experimental
public final class ObservableDoFinally<T> extends AbstractObservableWithUpstream<T, T> {

    final Action onFinally;

    public ObservableDoFinally(ObservableSource<T> source, Action onFinally) {
        super(source);
        this.onFinally = onFinally;
    }

    @Override
    protected void subscribeActual(Observer<? super T> s) {
        source.subscribe(new DoFinallyObserver<T>(s, onFinally));
    }

    static final class DoFinallyObserver<T> extends BasicIntQueueDisposable<T> implements Observer<T> {

        private static final long serialVersionUID = 4109457741734051389L;

        final Observer<? super T> actual;

        final Action onFinally;

        Disposable d;

        QueueDisposable<T> qd;

        boolean syncFused;

        DoFinallyObserver(Observer<? super T> actual, Action onFinally) {
            this.actual = actual;
            this.onFinally = onFinally;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.d, d)) {
                this.d = d;
                if (d instanceof QueueDisposable) {
                    this.qd = (QueueDisposable<T>)d;
                }

                actual.onSubscribe(this);
            }
        }

        @Override
        public void onNext(T t) {
            actual.onNext(t);
        }

        @Override
        public void onError(Throwable t) {
            actual.onError(t);
            runFinally();
        }

        @Override
        public void onComplete() {
            actual.onComplete();
            runFinally();
        }

        @Override
        public void dispose() {
            d.dispose();
            runFinally();
        }

        @Override
        public boolean isDisposed() {
            return d.isDisposed();
        }

        @Override
        public int requestFusion(int mode) {
            QueueDisposable<T> qd = this.qd;
            if (qd != null && (mode & BOUNDARY) == 0) {
                int m = qd.requestFusion(mode);
                if (m != NONE) {
                    syncFused = m == SYNC;
                }
                return m;
            }
            return NONE;
        }

        @Override
        public void clear() {
            qd.clear();
        }

        @Override
        public boolean isEmpty() {
            return qd.isEmpty();
        }

        @Nullable
        @Override
        public T poll() throws Exception {
            T v = qd.poll();
            if (v == null && syncFused) {
                runFinally();
            }
            return v;
        }

        void runFinally() {
            if (compareAndSet(0, 1)) {
                try {
                    onFinally.run();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaPlugins.onError(ex);
                }
            }
        }
    }
}