package com.wefine.reactive.examples;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;


public class HelloWorld1 {
    public static void main(String[] args) {
        Flowable<String> flowable = Flowable.create(e -> {
            e.onNext("xxx");
            e.onNext("hello RxJava 2");
            e.onComplete();
        }, BackpressureStrategy.BUFFER);

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(s);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
                System.out.println("onCompxxx");
            }

        };

        flowable.subscribe(subscriber); // 订阅
    }
}