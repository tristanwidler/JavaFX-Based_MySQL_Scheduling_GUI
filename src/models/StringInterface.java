package models;

import javafx.collections.ObservableList;

/**
 * This Interface serves as a shell for methods that return strings.
 * @author Tristan Widler, Stu ID(001213512)
 * @version 1.0
 * @since 2021-13-21
 */
public interface StringInterface {

    /** This abstract method is shell to be implemented with lambda expressions.
     * This abstract method was created as there are multiple places in the program that a String is returned
     * which gathers information from an Appointment object ObservableList.
     * @param apptList list used to aid in filling the string
     * @return the String created in the method
     */
    String structureString (ObservableList<Appointment> apptList);

}
