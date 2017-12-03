/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author Bernardo
 */
public class AccessException extends Exception{

    public AccessException (String reason) {
        super(reason);
    }

    public AccessException (String reason, Throwable rootCause) {
        super(reason, rootCause);
    }
}
