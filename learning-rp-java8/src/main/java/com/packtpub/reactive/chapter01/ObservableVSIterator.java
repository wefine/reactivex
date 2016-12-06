package com.packtpub.reactive.chapter01;

import com.packtpub.reactive.common.Program;
import rx.Observable;

import java.util.Arrays;
import java.util.List;

/**
 * Demonstrate the differences between Iterators and Observables.
 *
 * @author meddle
 */
public class ObservableVSIterator implements Program {

    private static void usingIteratorExample() {
        List<String> list = Arrays
                .asList("One", "Two", "Three", "Four", "Five");

        // While there is a next element, PULL it from the source and print it.
        for (String aList : list) {
            System.out.println(aList);
        }
    }

    private static void usingObservableExample() {
        List<String> list = Arrays
                .asList("One", "Two", "Three", "Four", "Five");

        Observable<String> observable = Observable.from(list);

        // Subscribe to the Observable. It will PUSH it's values to the Subscriber, and it will be printed.
        // (1)
        observable.subscribe(
                System.out::println,    // onNext
                System.err::println,    // onError
                () -> {
                    System.out.println("We've finished!"); // onCompleted
                }
        );
    }

    public static void main(String[] args) {
        new ObservableVSIterator().run();
    }

    @Override
    public String name() {
        return "Iterator vs Observable";
    }

    @Override
    public void run() {
        System.out.println("Running Iterator example:");
        usingIteratorExample();

        System.out.println("Running Observable example:");
        usingObservableExample();
    }

    @Override
    public int chapter() {
        return 1;
    }
}
