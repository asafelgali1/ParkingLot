import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ParkingLotGUI provides a modern graphical user interface to the Smart Parking Lot system.
 * Features: Add/Remove car, show lot status, statistics, history, and car cloning.
 */
public class ParkingLotGUI extends JFrame {
    private final ParkingLot parkingLot;
    private final CarFactory carFactory;
    private JTextField plateFieldAdd;
    private JTextField plateFieldRemove;
    private JTextField plateFieldClone;
    private JTextArea outputAreaStatus;
    private JTextArea outputAreaStats;
    private DefaultTableModel historyModel;
    private JTextArea outputAreaClone;

    /**
     * Constructs the ParkingLotGUI and initializes all GUI components.
     */
    public ParkingLotGUI() {
        super("🚗 Smart Parking Lot Management System");
        this.parkingLot = ParkingLot.createLot(10, new HourlyPricingStrategy(10.0));
        this.carFactory = new RegularCarFactory();

        // Modern Look & Feel
        setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Car Entry/Exit", makeEntryExitTab());
        tabs.addTab("Parking Status", makeStatusTab());
        tabs.addTab("Statistics", makeStatsTab());
        tabs.addTab("History", makeHistoryTab());
        tabs.addTab("Clone Car", makeCloneTab());

        add(tabs);

        // Live updates on lot changes
        parkingLot.addObserver(_ -> {
            updateStatus();
            updateStats();
            updateHistory();
        });

        // Initial updates
        updateStatus();
        updateStats();
        updateHistory();
    }

    /**
     * Creates the Entry/Exit tab.
     */
    private JPanel makeEntryExitTab() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Entry Panel
        JPanel entryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        entryPanel.setBackground(new Color(236, 240, 241));
        entryPanel.setBorder(BorderFactory.createTitledBorder("Car Entry"));

        plateFieldAdd = new JTextField(12);
        plateFieldAdd.setFont(new Font("SansSerif", Font.PLAIN, 18));
        JButton btnAdd = makeButton("➕ Add Car", new Color(39, 174, 96));
        btnAdd.addActionListener(_ -> onAddCar());

        entryPanel.add(new JLabel("License Plate:"));
        entryPanel.add(plateFieldAdd);
        entryPanel.add(btnAdd);

        // Exit Panel
        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        exitPanel.setBackground(new Color(236, 240, 241));
        exitPanel.setBorder(BorderFactory.createTitledBorder("Car Exit"));

        plateFieldRemove = new JTextField(12);
        plateFieldRemove.setFont(new Font("SansSerif", Font.PLAIN, 18));
        JButton btnRemove = makeButton("🅿️ Remove Car", new Color(192, 57, 43));
        btnRemove.addActionListener(_ -> onRemoveCar());

        exitPanel.add(new JLabel("License Plate:"));
        exitPanel.add(plateFieldRemove);
        exitPanel.add(btnRemove);

        panel.add(entryPanel);
        panel.add(exitPanel);

        return panel;
    }

    /**
     * Creates the Parking Status tab.
     */
    private JPanel makeStatusTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        outputAreaStatus = new JTextArea(18, 45);
        outputAreaStatus.setEditable(false);
        outputAreaStatus.setFont(new Font("Monospaced", Font.PLAIN, 16));
        outputAreaStatus.setBackground(new Color(250, 250, 250));
        outputAreaStatus.setForeground(new Color(44, 62, 80));
        outputAreaStatus.setLineWrap(true);
        outputAreaStatus.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(outputAreaStatus);
        scroll.setBorder(BorderFactory.createTitledBorder("Current Parking Lot Status"));

        JButton btnRefresh = makeButton("🔄 Refresh", new Color(41, 128, 185));
        btnRefresh.addActionListener(_ -> updateStatus());

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(btnRefresh, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Creates the Statistics tab.
     */
    private JPanel makeStatsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        outputAreaStats = new JTextArea(12, 45);
        outputAreaStats.setEditable(false);
        outputAreaStats.setFont(new Font("Monospaced", Font.PLAIN, 16));
        outputAreaStats.setBackground(new Color(250, 250, 250));
        outputAreaStats.setForeground(new Color(44, 62, 80));
        outputAreaStats.setLineWrap(true);
        outputAreaStats.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(outputAreaStats);
        scroll.setBorder(BorderFactory.createTitledBorder("Lot Statistics"));

        JButton btnRefresh = makeButton("📊 Refresh Stats", new Color(41, 128, 185));
        btnRefresh.addActionListener(_ -> updateStats());

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(btnRefresh, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Creates the History tab.
     */
    private JPanel makeHistoryTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Table
        String[] columns = {"License Plate", "Entry Time", "Exit Time", "Duration (min)", "Paid (NIS)"};
        historyModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable historyTable = new JTable(historyModel);
        historyTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        historyTable.setRowHeight(22);

        JScrollPane scroll = new JScrollPane(historyTable);
        scroll.setBorder(BorderFactory.createTitledBorder("Parking Lot History"));

        panel.add(scroll, BorderLayout.CENTER);

        JButton btnRefresh = makeButton("🔄 Refresh History", new Color(127, 140, 141));
        btnRefresh.addActionListener(_ -> updateHistory());
        panel.add(btnRefresh, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creates the Clone Car tab.
     */
    private JPanel makeCloneTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        topPanel.setBackground(new Color(236, 240, 241));
        topPanel.setBorder(BorderFactory.createTitledBorder("Clone Car Data (Prototype)"));

        plateFieldClone = new JTextField(12);
        plateFieldClone.setFont(new Font("SansSerif", Font.PLAIN, 18));
        JButton btnClone = makeButton("🧬 Clone Car", new Color(155, 89, 182));
        btnClone.addActionListener(_ -> onCloneCar());

        topPanel.add(new JLabel("License Plate:"));
        topPanel.add(plateFieldClone);
        topPanel.add(btnClone);

        outputAreaClone = new JTextArea(8, 50);
        outputAreaClone.setEditable(false);
        outputAreaClone.setFont(new Font("Monospaced", Font.PLAIN, 16));
        outputAreaClone.setBackground(new Color(250, 250, 250));
        outputAreaClone.setForeground(new Color(44, 62, 80));
        outputAreaClone.setLineWrap(true);
        outputAreaClone.setWrapStyleWord(true);
        outputAreaClone.setBorder(BorderFactory.createTitledBorder("Clone Result"));

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(outputAreaClone), BorderLayout.CENTER);
        return panel;
    }

    /**
     * Helper to create a modern-looking button.
     */
    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setBackground(bg);
        btn.setForeground(Color.white);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 73, 94), 1, true),
                new EmptyBorder(8, 18, 8, 18)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Handles adding a new car to the parking lot.
     */
    /**
     * Handles adding a new car to the parking lot.
     */
    private void onAddCar() {
        String plate = plateFieldAdd.getText().trim();
        if (plate.isEmpty()) {
            showMessage("Please enter a license plate.");
            return;
        }
        Car car = carFactory.createCar(plate);
        boolean added = parkingLot.addCar(car);
        if (added) {
            showMessage("Car entered the parking lot.");
            plateFieldAdd.setText("");
        } else {
            // Check if car is already in the lot
            boolean exists = false;
            for (ParkingSpot spot : parkingLot.getSpots()) {
                if (spot.isOccupied() && spot.getParkedCar().getLicensePlate().equals(plate)) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                showMessage("A car with this license plate is already parked in the lot.");
            } else {
                showMessage("Parking lot is full.");
            }
        }
    }

    /**
     * Handles removing a car from the parking lot.
     */
    private void onRemoveCar() {
        String plate = plateFieldRemove.getText().trim();
        if (plate.isEmpty()) {
            showMessage("Please enter a license plate.");
            return;
        }
        boolean removed = parkingLot.removeCar(plate);
        if (removed) {
            showMessage("Car exited the parking lot.");
            plateFieldRemove.setText("");
        } else {
            showMessage("Car not found in parking lot.");
        }
    }

    /**
     * Handles cloning a car's data.
     */
    private void onCloneCar() {
        String plate = plateFieldClone.getText().trim();
        if (plate.isEmpty()) {
            showMessage("Please enter a license plate to clone.");
            return;
        }
        Car foundCar = null;
        for (ParkingSpot spot : parkingLot.getSpots()) {
            if (spot.isOccupied() && spot.getParkedCar().getLicensePlate().equals(plate)) {
                foundCar = spot.getParkedCar();
                break;
            }
        }
        if (foundCar != null) {
            Car cloned = foundCar.clone();
            outputAreaClone.setText("Original car:\n" + foundCar + "\n\nCloned car:\n" + cloned);
            showMessage("Car data cloned successfully!");
        } else {
            outputAreaClone.setText("Car not found in parking lot.");
            showMessage("Car not found in parking lot.");
        }
    }

    /**
     * Updates the status display.
     */
    private void updateStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("Parking Spot Status:\n");
        int i = 1;
        for (ParkingSpot spot : parkingLot.getSpots()) {
            sb.append("Spot ").append(i++).append(": ");
            if (spot.isOccupied()) {
                sb.append("Occupied (").append(spot.getParkedCar().getLicensePlate()).append(")\n");
            } else {
                sb.append("Free\n");
            }
        }
        outputAreaStatus.setText(sb.toString());
    }

    /**
     * Updates the statistics display.
     */
    private void updateStats() {
        String sb = "----- Parking Lot Statistics -----\n" +
                "Total spots: " + parkingLot.getTotalSpots() + '\n' +
                "Occupied spots: " + parkingLot.getOccupiedSpots() + '\n' +
                "Free spots: " + parkingLot.getFreeSpots() + '\n' +
                "Average parking time (minutes): " + String.format("%.2f", parkingLot.getAverageParkingTimeMinutes()) + '\n' +
                "Today's revenue: " + String.format("%.2f", parkingLot.getTodaysRevenue()) + " NIS\n" +
                "----------------------------------\n";
        outputAreaStats.setText(sb);
    }

    /**
     * Updates the history table.
     */
    private void updateHistory() {
        List<CarHistoryEntry> hist = parkingLot.getHistory();
        historyModel.setRowCount(0);
        for (CarHistoryEntry entry : hist) {
            String entryTime = formatTime(entry.getEntryTime());
            String exitTime = formatTime(entry.getExitTime());
            double duration = (entry.getExitTime() - entry.getEntryTime()) / (1000.0 * 60.0);
            historyModel.addRow(new Object[]{
                    entry.getCar().getLicensePlate(),
                    entryTime,
                    exitTime,
                    String.format("%.1f", duration),
                    String.format("%.2f", entry.getPaid())
            });
        }
    }

    /**
     * Formats a timestamp to human-readable date and time.
     */
    private String formatTime(long millis) {
        if (millis < 0) return "-";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new java.util.Date(millis));
    }

    /**
     * Shows a popup message dialog.
     * @param msg the message to display
     */
    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    /**
     * Main entry point for the GUI application.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ParkingLotGUI().setVisible(true);
        });
    }
}