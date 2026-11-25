# Customer Billing System — PS No. 49

A Java console application for a billing counter.

## Features
- Add customers to a FIFO waiting queue.
- Process the next customer and calculate 5% GST.
- View customers still waiting.
- Search saved bills by Bill ID.
- Save records in `bills.txt` and reload them on the next run.

## Concepts used
Java classes and objects, `Queue` with `LinkedList`, `ArrayList`, methods, searching, file handling, and exception handling.

## Requirements
- JDK 8 or later (JDK 17/21 recommended).
- Visual Studio Code with Microsoft's Extension Pack for Java.

## Run in VS Code
Open this folder in VS Code, open Terminal > New Terminal, and run these commands from this folder:

```bash
javac CustomerBillingSystem.java
java CustomerBillingSystem
```

Keep `bills.txt` beside the `.java` file and run from this folder. The program reads and updates it automatically.

## Menu
1. Add Customer to Queue
2. Process Next Bill
3. View Waiting Queue
4. Search Bill by ID
5. Save and Exit

The first bill ID is `BL101`. GST = purchase amount × 5%; total = purchase amount + GST.

## Data format
Each line in `bills.txt` is:
`BillID|CustomerName|Amount|GST|Total|Status`

Example:
`BL101|Sanjay|1000.00|50.00|1050.00|Paid`

## Suggested tests
1. Add Sanjay with amount 1000: expect BL101, position 1.
2. Process Sanjay: expect GST 50 and total 1050; queue becomes empty.
3. Try amount -200: expect validation message and no customer added.
4. Add Rekha for 500, process Sanjay, search BL101: expect Sanjay's paid bill.

## Repository structure
```text
Customer-Billing-System/
├── CustomerBillingSystem.java
├── bills.txt
├── README.md
├── Project-Report.md
├── .gitignore
└── screenshots/
```

## Student details
Name: ____________________
Roll Number: ____________________
Course/Semester: ____________________
