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

package com.mar.lib.async.rxjava.internal.operators.flowable;

import org.reactivestreams.Subscriber;

import com.mar.lib.async.rxjava.Flowable;
import com.mar.lib.async.rxjava.annotations.*;
import com.mar.lib.async.rxjava.functions.Consumer;
import com.mar.lib.async.rxjava.internal.fuseable.ConditionalSubscriber;
import com.mar.lib.async.rxjava.internal.subscribers.*;

/**
 * Calls a consumer after pushing the current item to the downstream.
 * @param <T> the value type
 * @since 2.0.1 - experimental
 */
@Experimental
public final class FlowableDoAfterNext<T> extends AbstractFlowableWithUpstream<T, T> {

    final Consumer<? super T> onAfterNext;

    public FlowableDoAfterNext(Flowable<T> source, Consumer<? super T> onAfterNext) {
        super(source);
        this.onAfterNext = onAfterNext;
    }

    @Override
    protected void subscribeActual(Subscriber<? super T> s) {
        if (s instanceof ConditionalSubscriber) {
            source.subscribe(new DoAfterConditionalSubscriber<T>((ConditionalSubscriber<? super T>)s, onAfterNext));
        } else {
            source.subscribe(new DoAfterSubscriber<T>(s, onAfterNext));
        }
    }

    static final class DoAfterSubscriber<T> extends BasicFuseableSubscriber<T, T> {

        final Consumer<? super T> onAfterNext;

        DoAfterSubscriber(Subscriber<? super T> actual, Consumer<? super T> onAfterNext) {
            super(actual);
            this.onAfterNext = onAfterNext;
        }

        @Override
        public void onNext(T t) {
            if (done) {
                return;
            }
            actual.onNext(t);

            if (sourceMode == NONE) {
                try {
                    onAfterNext.accept(t);
                } catch (Throwable ex) {
                    fail(ex);
                }
            }
        }

        @Override
        public int requestFusion(int mode) {
            return transitiveBoundaryFusion(mode);
        }

        @Nullable
        @Override
        public T poll() throws Exception {
            T v = qs.poll();
            if (v != null) {
                onAfterNext.accept(v);
            }
            return v;
        }
    }

    static final class DoAfterConditionalSubscriber<T> extends BasicFuseableConditionalSubscriber<T, T> {

        final Consumer<? super T> onAfterNext;

        DoAfterConditionalSubscriber(ConditionalSubscriber<? super T> actual, Consumer<? super T> onAfterNext) {
            super(actual);
            this.onAfterNext = onAfterNext;
        }

        @Override
        public void onNext(T t) {
            actual.onNext(t);

            if (sourceMode == NONE) {
                try {
                    onAfterNext.accept(t);
                } catch (Throwable ex) {
                    fail(ex);
                }
            }
        }

        @Override
        public boolean tryOnNext(T t) {
            boolean b = actual.tryOnNext(t);
            try {
                onAfterNext.accept(t);
            } catch (Throwable ex) {
                fail(ex);
            }
            return b;
        }

        @Override
        public int requestFusion(int mode) {
            return transitiveBoundaryFusion(mode);
        }

        @Nullable
        @Override
        public T poll() throws Exception {
            T v = qs.poll();
            if (v != null) {
                onAfterNext.accept(v);
            }
            return v;
        }
    }
}