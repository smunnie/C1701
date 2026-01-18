package controller;
import app.ITTicketingSimpleApp;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import model.Ticket;
import ui.*;

public class Controllers {
    private final ITTicketingSimpleApp app;
    private final TableView<Ticket> table;
    public final DeleteTicketDialog deleteDialogs;
    public final ResolveTicketDialog resolveDialog;
    public final ChangepriorityTicketDialog changepriorityDialog;
    public final TicketDetailsDialog ticketDetailsDialog;
    public final NewTicketDialog newTicketDialog;
    public final DecreaseTicketPriorityDialog decreasePriDialog;

    // constructor
    public Controllers(ITTicketingSimpleApp app, TableView<Ticket> table) {
        this.app = app;
        this.table = table;
        this.deleteDialogs = new DeleteTicketDialog(app);
        this.decreasePriDialog = new DecreaseTicketPriorityDialog();
        this.newTicketDialog = new NewTicketDialog(app);
        this.resolveDialog = new ResolveTicketDialog();
        this.changepriorityDialog = new ChangepriorityTicketDialog();
        this.ticketDetailsDialog = new TicketDetailsDialog();

    }

    public void onDelete(ActionEvent e) {
        Ticket t = getSelectedOrWarn("Please select a ticket to delete.");
        if (t == null) return;

        deleteDialogs.show(t, table);
    }

    public void onResolve(ActionEvent e) {
        Ticket t = getSelectedOrWarn("Please select a ticket to resolve.");
        if (t == null) return;

        resolveDialog.show(t, table);
    }

    public void onChangePriority(ActionEvent e) {
        Ticket t = getSelectedOrWarn("Please select a ticket to change priority.");
        if (t == null) return;

        changepriorityDialog.show(t, table);
    }

    public void onDecreasePriority(ActionEvent e){
        Ticket t = getSelectedOrWarn("Please select a ticket to change priority.");
        if (t == null) return;
        decreasePriDialog.show(t,table);
    }

    public void onLogout(ActionEvent e) {
        app.logout();
    }

    public void onClickNewTicket(ActionEvent e){
        Ticket t = getSelectedOrWarn("Please select a ticket to change priority.");
        if (t == null) return;

        newTicketDialog.show();
    }

    /**
     * Call this when user double-clicks a row
     */
    public void onRowDoubleClick(Ticket selectedTicket) {
        if (selectedTicket == null) return;
        ticketDetailsDialog.show(selectedTicket, app);
    }

    //helper function

    private Ticket getSelectedOrWarn(String message) {
        Ticket t = table.getSelectionModel().getSelectedItem();
        if (t == null) {
            new Alert(Alert.AlertType.INFORMATION, message).showAndWait();
        }
        return t;

    }
}