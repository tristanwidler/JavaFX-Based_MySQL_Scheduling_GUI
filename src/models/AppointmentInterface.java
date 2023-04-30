package models;

import javafx.collections.ObservableList;

/**
 * This Interface serves as a shell for methods that return Appointment object lists.
 * @author Tristan Widler, Stu ID(001213512)
 * @version 1.0
 * @since 2021-13-21
 */
public interface AppointmentInterface {

    /** This abstract method is shell to be implemented with lambda expressions.
     * This abstract method was created as there are multiple places in the program that an
     * ObservableList of Appointments is returned which is filtered via a variety of methods.
     * @return the instantiated ObservableList of Appointments
     */
    ObservableList<Appointment> gatherAppts ();

}
