package ui;

import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import model.Ticket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class TicketFilters {

    private TicketFilters() {
        // Utility class: no objects needed
    }

    /**
     * Applies status + date range + request type filters to the given FilteredList.
     */
    public static void applyFilters(FilteredList<Ticket> filteredTickets,
                              DatePicker fromDatePicker,
                              DatePicker toDatePicker,
                              ComboBox<String> requestBox,
                              String selectedStatus) {
        filteredTickets.setPredicate(ticket -> {
            boolean passesFilters = true;

            //Status filter (existing functionality)
            if (!selectedStatus.equals("Any")) {
                //convert both strings
                String ticketStatus = ticket.getStatus().name().toLowerCase();
                String filterStatus = selectedStatus.toLowerCase();
                passesFilters = ticketStatus.equals(filterStatus);
            }

            //Date range filter - LocalDateTime comparisons
            LocalDate fromDate = fromDatePicker.getValue();
            LocalDate toDate = toDatePicker.getValue();
            LocalDateTime ticketDateTime = ticket.getCreatedDate();

            if (fromDate != null) {
                //convert LocalDate to LocalDateTime
                //method chaining demonstrated here
                LocalDateTime fromDateTime = fromDate.atStartOfDay();
                passesFilters = passesFilters && !ticketDateTime.isBefore(fromDateTime);
            }

            if (toDate != null) {
                LocalDateTime toDateTime = toDate.atTime(LocalTime.MAX);
                passesFilters = passesFilters && !ticketDateTime.isAfter(toDateTime);
            }

            // request type filter
            String selectedRequestType = requestBox.getValue();
            //map UI display names to actual enum values as might differ from enum names
            String enumEquivalent = mapRequestTypeToEnum(selectedRequestType);
            if (enumEquivalent != null){
                //Compare ticket's request type with selected type
                String ticketRequestType = ticket.getRequestType().name();
                passesFilters = passesFilters && ticketRequestType.equalsIgnoreCase(enumEquivalent);
            }
            return passesFilters; // Ticket passes all filters
        });
    }

    /**
     * Maps UI display names to enum names.
     * Returns null to mean "no filter".
     */
    public static String mapRequestTypeToEnum(String uiDisplayName) {
        if (uiDisplayName == null || uiDisplayName.equalsIgnoreCase("All")) {
            return null;
        }

        return switch (uiDisplayName.toLowerCase()) {
            case "security issues" -> "security_issues";
            case "new computer configuration" -> "new_computer_configuration";
            case "software/app installation" -> "software_app_installation";
            case "network issue" -> "network_issue";
            default -> null;
        };
    }

    /**
     * Clears filter controls and resets predicate to show all tickets.
     */
    public static void clearFilters(FilteredList<Ticket> filteredTickets,
                                    ComboBox<String> statusBox,
                                    DatePicker fromDatePicker,
                                    DatePicker toDatePicker,
                                    ComboBox<String> requestBox) {

        // clear all filter controls
        statusBox.setValue("Any");
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);
        requestBox.setValue("All");

        // Reset all filters
        filteredTickets.setPredicate(t -> true);
        System.out.println("All filters cleared");
    }

}