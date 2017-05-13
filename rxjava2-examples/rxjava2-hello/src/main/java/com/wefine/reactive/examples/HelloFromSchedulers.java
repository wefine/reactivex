package com.wefine.reactive.examples;


import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloFromSchedulers {
    private static Logger log = LoggerFactory.getLogger(HelloFromSchedulers.class);

    public static void main(String[] args) throws InterruptedException {
        Observable
                .create((ObservableOnSubscribe<Integer>) e -> {
                    log.info("Thread： " + Thread.currentThread().getName());
                    log.info("Data:" + 1 + "");
                    e.onNext(1);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(integer -> {
                    log.info("Thread：" + Thread.currentThread().getName());
                    log.info("Data received:" + "integer:" + integer);
                });

        Thread.sleep(1000L);
    }

}
