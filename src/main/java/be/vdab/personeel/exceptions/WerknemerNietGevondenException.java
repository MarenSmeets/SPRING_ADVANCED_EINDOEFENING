package be.vdab.personeel.exceptions;

public class WerknemerNietGevondenException extends RuntimeException {

    public WerknemerNietGevondenException(){

    }
    public WerknemerNietGevondenException(String msg){
        super(msg);
    }
}
