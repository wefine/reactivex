package com.packtpub.reactive.chapter01;

import com.packtpub.reactive.common.CreateObservable;
import com.packtpub.reactive.common.Program;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

/**
 * A demonstration of how to implement a sum that updates automatically when any of its collectors changes.
 *
 * @author meddle
 */
public class ReactiveSumV1 implements Program {

    /**
     * The Observable returned by this method, only reacts to values in the form
     * <varName> = <value> or <varName> : <value>.
     * It emits the <value>.
     */
    private static Observable<Double> varStream(final String varName,
                                                Observable<String> input) {
        final Pattern pattern = Pattern.compile("^\\s*" + varName
                + "\\s*[:|=]\\s*(-?\\d+\\.?\\d*)$");

        return input.map(pattern::matcher)
                .filter(matcher -> matcher.matches() && matcher.group(1) != null)
                .map(matcher -> matcher.group(1))
                .filter(Objects::nonNull)
                .map(Double::parseDouble);
    }

    /**
     * Here the input is executed on a separate thread, so we block the current one until it sends
     * a `completed` notification.
     */
    public static void main(String[] args) {
        System.out.println();
        System.out.println("Reacitve Sum. Type 'a: <number>' and 'b: <number>' to try it.");

        new ReactiveSumV1().run();
    }

    public String name() {
        return "Reactive Sum, version 1";
    }

    public void run() {
        ConnectableObservable<String> input = CreateObservable.from(System.in);

        Observable<Double> a = varStream("a", input);
        Observable<Double> b = varStream("b", input);

        ReactiveSum sum = new ReactiveSum(a, b);

        input.connect();

        try {
            sum.getLatch().await();
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public int chapter() {
        return 1;
    }

    /**
     * The sum is just an Observer, which subscribes to a stream created by combining 'a' and 'b', via summing.
     *
     * @author meddle
     */
    public static final class ReactiveSum implements Observer<Double> {

        private CountDownLatch latch = new CountDownLatch(1);

        private double sum;
        private Subscription subscription = null;

        ReactiveSum(Observable<Double> a, Observable<Double> b) {
            this.sum = 0;

            subscribe(a, b);
        }

        private void subscribe(Observable<Double> a, Observable<Double> b) {
            // combineLatest creates an Observable, sending notifications on changes of either of its sources.
            // This notifications are formed using a Func2.
            this.subscription = Observable
                    .combineLatest(a, b, (a1, b1) -> a1 + b1)
                    .subscribeOn(Schedulers.io())
                    .subscribe(this);
        }

        public void unsubscribe() {
            this.subscription.unsubscribe();
            this.latch.countDown();
        }

        public void onCompleted() {
            System.out.println("Exiting last sum was : " + this.sum);
            this.latch.countDown();
        }

        public void onError(Throwable e) {
            System.err.println("Got an error!");
            e.printStackTrace();
        }

        public void onNext(Double sum) {
            this.sum = sum;
            System.out.println("update : a + b = " + sum);
        }

        CountDownLatch getLatch() {
            return latch;
        }
    }
}
