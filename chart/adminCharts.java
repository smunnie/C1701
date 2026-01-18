package Charts;

import app.ITTicketingSimpleApp;
import javafx.geometry.Side;
import model.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;

import java.time.Month;
import java.util.EnumMap;
import java.util.Map;

public class adminCharts {
    private final ITTicketingSimpleApp app;

    public adminCharts(ITTicketingSimpleApp app) {
        this.app = app;
    }

    public BarChart<String, Number> getMonthlyTicketsBarChart() {
        return createMonthlyTicketsChart();
    }

    public PieChart getMonthlyTicketsPieChart() {
        return createMonthlyTicketsPieChart();
    }
    public BarChart<String, Number> getRequestTypeOverTimeChart() {
        return createRequestTypeOverTimeChart();
    }
    private BarChart<String, Number> createMonthlyTicketsChart() {

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Tickets Raised");

        BarChart<String, Number> barchart = new BarChart<>(xAxis, yAxis);
        barchart.setTitle("Tickets Raised in 2025 ");

        int max = app.getAllTickets().size();
        if (max == 0) max = 1;
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(max + 1);
        yAxis.setTickUnit(1);
        yAxis.setMinorTickVisible(false);


        int[] monthlyCount = new int[12];
        for (Ticket t : app.getAllTickets()) {
            int month = t.getCreatedDate().getMonthValue();
            monthlyCount[month - 1]++;
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Tickets");
        String[] monthNames = {
                "Jan","Feb","Mar","Apr","May","Jun",
                "Jul","Aug","Sep","Oct","Nov","Dec"
        };

        for (int i = 0; i < 12; i++) {
            series.getData().add(new XYChart.Data<>(monthNames[i], monthlyCount[i]));
        }

        barchart.getData().add(series);
        barchart.setCategoryGap(10);
        barchart.setBarGap(3);

        return barchart;
    }
    private PieChart createMonthlyTicketsPieChart() {

        int[] monthlyCount = new int[12];
        for (Ticket t : app.getAllTickets()) {
            int month = t.getCreatedDate().getMonthValue();
            monthlyCount[month - 1]++;
        }
        String[] monthNames = {
                "Jan","Feb","Mar","Apr","May","Jun",
                "Jul","Aug","Sep","Oct","Nov","Dec"
        };

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

        for (int i = 0; i < 12; i++) {
            if (monthlyCount[i] > 0) {
                pieData.add(new PieChart.Data(
                        monthNames[i] + " (" + monthlyCount[i] + ")",
                        monthlyCount[i]
                ));
            }
        }

        PieChart pieChart = new PieChart(pieData);
        pieChart.setTitle("Tickets per Month");
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);
        pieChart.setClockwise(true);
        pieChart.setStartAngle(90);

        pieChart.setPrefWidth(350);
        pieChart.setPrefHeight(300);

        return pieChart;
    }
    private BarChart<String, Number> createRequestTypeOverTimeChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of Tickets");

        int max = app.getAllTickets().size();
        if (max == 0) max = 1;

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(max + 1);
        yAxis.setTickUnit(1);
        yAxis.setMinorTickVisible(false);
        yAxis.setForceZeroInRange(true);

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Request Type Distribution Over Time");

        chart.setLegendVisible(true);
        chart.setLegendSide(Side.RIGHT);

        Map<Ticket.JobRequest, int[]> data = new EnumMap<>(Ticket.JobRequest.class);
        for (Ticket.JobRequest type : Ticket.JobRequest.values()) {
            data.put(type, new int[12]);
        }

        for (Ticket t : app.getAllTickets()) {
            int month = t.getCreatedDate().getMonthValue() - 1;
            data.get(t.getRequestType())[month]++;
        }

        for (Ticket.JobRequest type : Ticket.JobRequest.values()) {

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(type.name().replace("_", " "));

            int[] counts = data.get(type);
            for (int i = 0; i < 12; i++) {
                series.getData().add(
                        new XYChart.Data<>(
                                Month.of(i + 1).name().substring(0, 3),
                                counts[i]
                        )
                );
            }

            chart.getData().add(series);
        }

        chart.setCategoryGap(10);
        chart.setBarGap(4);
        return chart;
    }


}
