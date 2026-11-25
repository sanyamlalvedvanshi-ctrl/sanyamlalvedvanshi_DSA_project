import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class CustomerBillingSystem {
    private static final BigDecimal GST_RATE = new BigDecimal("0.05");
    private static final Path DATA_FILE = Paths.get("bills.txt");
    private static final Queue<CustomerBill> waitingQueue = new LinkedList<>();
    private static final List<CustomerBill> allBills = new ArrayList<>();
    private static int nextBillNumber = 101;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadBillsFromFile();
        System.out.println("==========================================");
        System.out.println("          CUSTOMER BILLING SYSTEM");
        System.out.println("==========================================");
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1: addCustomer(); break;
                case 2: processNextBill(); break;
                case 3: viewQueue(); break;
                case 4: searchBill(); break;
                case 5:
                    saveBillsToFile();
                    System.out.println("All records saved. Thank you!");
                    running = false;
                    break;
                default: System.out.println("Invalid menu option. Please choose 1 to 5.");
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n--------------- MAIN MENU ---------------");
        System.out.println("1. Add Customer to Queue");
        System.out.println("2. Process Next Bill");
        System.out.println("3. View Waiting Queue");
        System.out.println("4. Search Bill by ID");
        System.out.println("5. Save and Exit");
        System.out.println("-----------------------------------------");
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private static BigDecimal readPositiveAmount() {
        while (true) {
            System.out.print("Enter purchase amount: Rs. ");
            try {
                BigDecimal amount = new BigDecimal(scanner.nextLine().trim());
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Invalid amount. Amount must be greater than 0.");
                    continue;
                }
                return amount.setScale(2, RoundingMode.HALF_UP);
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a valid number.");
            }
        }
    }

    private static void addCustomer() {
        System.out.println("\n----------- ADD CUSTOMER -----------");
        String name;
        while (true) {
            System.out.print("Enter customer name: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Customer name cannot be empty.");
            } else if (name.contains("|") || name.contains("\n") || name.contains("\\r")) {
                System.out.println("Please do not use the | character or line breaks in the name.");
            } else break;
        }

        BigDecimal amount = readPositiveAmount();
        String billId = "BL" + nextBillNumber++;
        CustomerBill bill = new CustomerBill(billId, name, amount);
        waitingQueue.offer(bill);
        allBills.add(bill);

        System.out.println("\nCustomer added successfully!");
        System.out.println("Customer ID: " + bill.getBillId());
        System.out.println("Customer: " + bill.getCustomerName());
        System.out.println("Amount: Rs. " + money(bill.getAmount()));
        System.out.println("Position: " + waitingQueue.size());
        saveBillsToFile();
    }

    private static void processNextBill() {
        System.out.println("\n----------- PROCESS NEXT BILL -----------");
        CustomerBill bill = waitingQueue.poll();
        if (bill == null) {
            System.out.println("Queue is empty. No customer is waiting.");
            return;
        }
        bill.generateBill();
        System.out.println("\nBill generated successfully!");
        printBill(bill);
        saveBillsToFile();
    }

    private static void viewQueue() {
        System.out.println("\n----------- WAITING QUEUE -----------");
        if (waitingQueue.isEmpty()) {
            System.out.println("The queue is now empty.");
            return;
        }
        System.out.printf("%-10s %-22s %-15s %-12s%n", "Position", "Customer Name", "Bill ID", "Amount");
        System.out.println("---------------------------------------------------------------");
        int position = 1;
        for (CustomerBill bill : waitingQueue) {
            System.out.printf("%-10d %-22s %-15s Rs. %-10s%n",
                    position++, bill.getCustomerName(), bill.getBillId(), money(bill.getAmount()));
        }
        System.out.println("\nTotal customers waiting: " + waitingQueue.size());
    }

    private static void searchBill() {
        System.out.println("\n----------- SEARCH BILL -----------");
        System.out.print("Enter Bill ID (example: BL101): ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("Bill ID cannot be empty.");
            return;
        }
        for (CustomerBill bill : allBills) {
            if (bill.getBillId().equalsIgnoreCase(id)) {
                System.out.println("\nBill found!");
                printBill(bill);
                return;
            }
        }
        System.out.println("Bill not found for ID: " + id);
    }

    private static void printBill(CustomerBill bill) {
        System.out.println("------------------------------------------");
        System.out.println("                 SHOP BILL");
        System.out.println("------------------------------------------");
        System.out.println("Bill ID        : " + bill.getBillId());
        System.out.println("Customer Name  : " + bill.getCustomerName());
        System.out.println("Purchase Amount: Rs. " + money(bill.getAmount()));
        if (bill.isPaid()) {
            System.out.println("GST (5%)       : Rs. " + money(bill.getGst()));
            System.out.println("Total Amount   : Rs. " + money(bill.getTotal()));
            System.out.println("Status         : Paid");
        } else {
            System.out.println("GST (5%)       : Not generated yet");
            System.out.println("Total Amount   : Not generated yet");
            System.out.println("Status         : Waiting");
        }
        System.out.println("------------------------------------------");
    }

    private static void loadBillsFromFile() {
        if (!Files.exists(DATA_FILE)) {
            System.out.println("No previous bill file found. A new one will be created.");
            return;
        }
        int loaded = 0;
        try (BufferedReader reader = Files.newBufferedReader(DATA_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\\\\|", -1);
                if (p.length != 6) {
                    System.out.println("Skipping invalid record: " + line);
                    continue;
                }
                try {
                    CustomerBill bill = new CustomerBill(p[0], p[1], new BigDecimal(p[2]));
                    boolean paid = p[5].equalsIgnoreCase("Paid");
                    bill.restore(new BigDecimal(p[3]), new BigDecimal(p[4]), paid);
                    allBills.add(bill);
                    if (!paid) waitingQueue.offer(bill);
                    if (p[0].toUpperCase().startsWith("BL")) {
                        try {
                            int n = Integer.parseInt(p[0].substring(2));
                            if (n >= nextBillNumber) nextBillNumber = n + 1;
                        } catch (NumberFormatException ignored) { }
                    }
                    loaded++;
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid numeric record: " + line);
                }
            }
            System.out.println("Loaded " + loaded + " bill record(s) from bills.txt.");
            System.out.println("Customers waiting: " + waitingQueue.size());
        } catch (IOException e) {
            System.out.println("Could not read bills.txt: " + e.getMessage());
        }
    }

    private static void saveBillsToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(DATA_FILE, StandardCharsets.UTF_8)) {
            for (CustomerBill b : allBills) {
                writer.write(b.getBillId() + "|" + b.getCustomerName() + "|" +
                        b.getAmount().toPlainString() + "|" + b.getGst().toPlainString() + "|" +
                        b.getTotal().toPlainString() + "|" + (b.isPaid() ? "Paid" : "Waiting"));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving bills.txt: " + e.getMessage());
        }
    }

    private static String money(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private static class CustomerBill {
        private final String billId;
        private final String customerName;
        private final BigDecimal amount;
        private BigDecimal gst = new BigDecimal("0.00");
        private BigDecimal total = new BigDecimal("0.00");
        private boolean paid = false;

        CustomerBill(String billId, String customerName, BigDecimal amount) {
            this.billId = billId;
            this.customerName = customerName;
            this.amount = amount;
        }

        void generateBill() {
            gst = amount.multiply(GST_RATE).setScale(2, RoundingMode.HALF_UP);
            total = amount.add(gst).setScale(2, RoundingMode.HALF_UP);
            paid = true;
        }

        void restore(BigDecimal gst, BigDecimal total, boolean paid) {
            this.gst = gst;
            this.total = total;
            this.paid = paid;
        }

        String getBillId() { return billId; }
        String getCustomerName() { return customerName; }
        BigDecimal getAmount() { return amount; }
        BigDecimal getGst() { return gst; }
        BigDecimal getTotal() { return total; }
        boolean isPaid() { return paid; }
    }
}
