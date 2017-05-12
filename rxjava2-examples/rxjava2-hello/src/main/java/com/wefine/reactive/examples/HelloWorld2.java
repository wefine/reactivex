package com.wefine.reactive.examples;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class HelloWorld2 {
    public static void main(String[] args) {

        Flowable<String> flowable = Flowable.create(e -> {
            e.onNext("xxx");
            e.onNext("hello RxJava 2");
            e.onComplete();
        }, BackpressureStrategy.BUFFER);

        Subscriber<String> subscriber1 = new Subscriber<String>() {

            public void onSubscribe(Subscription s) {
                System.out.println("subscriber1 onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            public void onNext(String message) {
                System.out.println(message);
            }

            public void onError(Throwable t) {

            }

            public void onComplete() {
                System.out.println("subscriber1 onComplete");
            }
        };

        Subscriber<String> subscriber2 = new Subscriber<String>() {
            public void onSubscribe(Subscription subscription) {
                System.out.println("subscriber2 onSubscribe");

                subscription.request(Long.MAX_VALUE);
            }

            public void onNext(String message) {
                System.out.println("第二个订阅类" + message);
            }

            public void onError(Throwable t) {
            }

            public void onComplete() {
                System.out.println("subscriber2 onComplete");
            }
        };

        flowable.subscribe(subscriber1);// 订阅多个主题
        flowable.subscribe(subscriber2);
    }
}