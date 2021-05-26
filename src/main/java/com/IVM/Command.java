package com.IVM;

import java.util.function.Consumer;

public class Command {
    private IVMConsumer<String[]> lambda;
    private int min_args, max_args;

    public Command(IVMConsumer<String[]> consumer, int args) {
        lambda = consumer;
        min_args = max_args = args;
    }

    public Command(IVMConsumer<String[]> consumer, int min, int max) {
        lambda = consumer;
        min_args = min;
        max_args = max;
    }

    public void invoke(String[] args) throws IVMException {
        if (args.length > max_args || args.length < min_args) {
            throw new IVMException("Invalid amount of arguments!");
        }
        lambda.acceptThrows(args);
    }
}

@FunctionalInterface
interface IVMConsumer<T> extends Consumer<T>{
    @Override
    default void accept(final T elem) {
        try {
            acceptThrows(elem);
        } catch (final IVMException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    void acceptThrows(T elem) throws IVMException;
}
