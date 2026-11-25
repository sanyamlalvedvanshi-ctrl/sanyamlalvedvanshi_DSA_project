# PROJECT REPORT — CUSTOMER BILLING SYSTEM

**Project Number:** PS No. 49  
**University:** Rungta International Skills University  
**School:** School of Computer Science and Engineering  
**Student Name:** ____________________  
**Roll Number:** ____________________  
**Course / Semester:** ____________________  
**Submission Date:** ____________________

## 1. Introduction
The Customer Billing System is a Java console application for managing customers at a billing counter. It adds customers to a waiting queue, processes bills in arrival order, calculates GST, displays the queue, and searches saved bills.

## 2. Problem Statement
Develop a console application for a billing counter that can add customers to a billing queue, process the next bill with GST, view the waiting queue, and search a bill by its ID. Data must be saved to a file.

## 3. Objectives
- Maintain a FIFO customer queue.
- Generate unique bill IDs, starting at BL101.
- Calculate GST at 5%.
- Generate and display bills.
- View waiting customers and search by Bill ID.
- Save and load records using `bills.txt`.

## 4. Tools and Technologies
- Programming language: Java
- Editor: Visual Studio Code
- JDK: JDK 8 or later
- Storage: Text file (`bills.txt`)
- Repository: GitHub

## 5. Concepts Used
- **Class and Objects:** `CustomerBill` represents a customer bill.
- **Queue:** `LinkedList` maintains waiting customers in FIFO order.
- **ArrayList:** Maintains all bill records, including processed bills.
- **Methods:** Separate methods implement each operation.
- **Searching:** Bill records are searched by ID.
- **File Handling:** `BufferedReader` loads and `BufferedWriter` saves records.
- **Exception Handling:** Invalid input and file errors are handled.

## 6. Working
1. On startup, the program loads existing records from `bills.txt`.
2. The user selects an operation from the menu.
3. Add Customer validates the name and amount, assigns an ID, and adds the record to the queue.
4. Process Next Bill removes the first waiting customer, calculates 5% GST, calculates the total, and marks the bill Paid.
5. View Queue displays only customers still waiting.
6. Search Bill finds a record by its ID, including previously processed bills.
7. Records are saved after changes and when the program exits.

## 7. GST Calculation
GST = Purchase Amount × 5 / 100  
Total Amount = Purchase Amount + GST

Example: Purchase amount = Rs. 1,000; GST = Rs. 50; Total = Rs. 1,050.

## 8. Menu
1. Add Customer to Queue
2. Process Next Bill
3. View Waiting Queue
4. Search Bill by ID
5. Save and Exit

## 9. Test Cases

### Test Case 1 — Normal Input
Input: Add Sanjay with amount 1000.  
Expected: Customer added, ID BL101, amount 1000, position 1.

### Test Case 2 — Process Next Bill
Input: Process Sanjay's bill for 1000.  
Expected: Bill BL101, GST 50, total 1050, status Paid, queue empty.

### Test Case 3 — Invalid Input
Input: Add Rekha with amount -200.  
Expected: `Invalid amount. Amount must be greater than 0.` Customer is not added.

### Test Case 4 — Different Valid Scenario
Input: Add Rekha with amount 500, process Sanjay, then search BL101.  
Expected: Rekha is added; Sanjay's bill is generated; BL101 is found with total 1050 and status Paid.

## 10. Data File
The file stores one record per line in this format:
`BillID|CustomerName|Amount|GST|Total|Status`

Example:
`BL101|Sanjay|1000.00|50.00|1050.00|Paid`

## 11. Conclusion
The project demonstrates the use of Java data structures and file handling to manage a billing counter. It applies FIFO queue processing, GST calculation, searching, and persistent storage.

## 12. Screenshots to Add
Paste your own screenshots of:
1. Main menu
2. Adding a customer
3. Processing a bill
4. Viewing the queue
5. Searching for a bill
6. Invalid amount validation
