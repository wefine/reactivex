package com.wefine.reactive.examples;


import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackpressureTest {
    private static Logger log = LoggerFactory.getLogger(BackpressureTest.class);

    @Test
    public void test() throws InterruptedException {
        Flowable.create((FlowableOnSubscribe<Integer>) emitter -> {
            for (int i = 0; i < 129; i++) {
                log.info("emit " + i);
                emitter.onNext(i);
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        log.info("subscription:", s);
                        // request 方法指明消费能力
                        s.request(1000);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        log.info("onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        log.info("onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                    }
                });

        Thread.sleep(1000L);
    }
}
