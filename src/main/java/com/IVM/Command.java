package com.IVM;

import java.util.function.Consumer;

public class Command {
    private IVMConsumer<String[]> lambda;
    private String[] arg_types;

    public Command(IVMConsumer<String[]> consumer, String[] args) {
        lambda = consumer;
        arg_types = args;
    }

    public Command(IVMConsumer<String[]> consumer) {
        lambda = consumer;
        arg_types = new String[0];
    }

    public void invoke(String[] args) throws IVMException {
        lambda.acceptThrows(args);
    }

    public String[] getArgTypes() { return arg_types; }
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
