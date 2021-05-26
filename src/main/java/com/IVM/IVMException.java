package com.IVM;

public class IVMException extends Throwable {
    public IVMException() {
        super("Unknown IVMException happened!");
    }

    public IVMException(IVMException e) {
        super(e.getMessage());
    }

    public IVMException(String message) {
        super(message);
    }
}
