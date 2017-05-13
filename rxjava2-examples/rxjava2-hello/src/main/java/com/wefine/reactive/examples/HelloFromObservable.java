package com.wefine.reactive.examples;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HelloFromObservable {
    public static void main(String[] args) throws InterruptedException {
        HelloFromObservable hello = new HelloFromObservable();

        Observable<String> observable = hello.create();
        observable = hello.just();
        observable = hello.fromIterable();
        observable = hello.defer();


        Observer<String> observer = new Observer<String>() {
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe");
            }

            public void onNext(String message) {
                System.out.println("我接收到数据: " + message);
            }

            public void onError(Throwable e) {
            }

            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        observable.subscribe(observer);
        Thread.sleep(1000);
    }

    private Observable<String> create() {

        return Observable.create(e -> {
            // do something
            e.onNext("create changed");
        });
    }

    private Observable<String> just() {

        return Observable.just("just changed!");
    }

    private Observable<String> fromIterable() {

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("Hello" + i);
        }

        return Observable.fromIterable(list);
    }

    private Observable<String> defer() {
        return Observable.defer(() -> Observable.just(" defer hello"));
    }

    private Observable<Long> interval() {
        return Observable.interval(2, TimeUnit.SECONDS);
    }

    private Observable<Integer> range() {
        return Observable.range(1, 20);
    }

    private Observable<Integer> repeat() {

        return Observable.just(123).repeat();
    }

    private void operation() {
        Observable.just("hello").map(String::length);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("Hello" + i);
        }

        // flatMap
        Observable.just(list).flatMap(Observable::fromIterable);

        // filter
        Observable.just(list)
                .flatMap((Function<List<String>, ObservableSource<?>>) Observable::fromIterable)
                .filter(s -> {
                    String newStr = (String) s;
                    return newStr.charAt(5) - '0' > 5;
                })
                .subscribe(o -> System.out.println((String) o));

        Observable.just(list)
                .flatMap((Function<List<String>, ObservableSource<?>>) Observable::fromIterable)
                .take(5)    // 最多个数
                .subscribe(s -> System.out.println((String) s));

        Observable.just(list)
                .flatMap((Function<List<String>, ObservableSource<?>>) Observable::fromIterable)
                .take(5)
                .doOnNext(o -> System.out.println("before subscribe"))
                .subscribe(s -> System.out.println((String) s));
    }
}
