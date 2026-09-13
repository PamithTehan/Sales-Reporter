# SENG 21222: Software Construction — Assignment 1 (2026)
### Sales Reporter Console Application

---

## 🏛️ Academic Information

- **Institution:** University of Kelaniya – Sri Lanka
- **Faculty:** Faculty of Science
- **Course:** SENG 21222 – Software Construction
- **Lecturer / Instructor:** Eng. Sudam Kalpage
- **Academic Year:** 2026
- **Assignment:** Assignment 1 – Object-Oriented Design, SOLID Principles, and Clean Architecture

### 👥 Group Members & Contributions

| Member | Student Registration No. | Name | Primary Responsibilities |
| :--- | :--- | :--- | :--- |
| **Member 01** | `SE/2023/064` | Rihaz Ramzaan | Input parsing pipeline (`Reader`, `RowSplitter`, `HeaderDetector`, `ProductRowMapper`), CSV exception handling, and input unit testing |
| **Member 02** | `SE/2023/033` | Pamith Tehan | Core application orchestration (`ReportGenerator`), Dependency Inversion Principle (DIP), Formatter Factory (`ReportFormatterFactory`), domain immutability |
| **Member 03** | `SE/2023/036` | Thanish Ahamed | Output management subsystem (`OutputWriter`, `OutputWriterFactory`, `ConsoleWriter`, `FileWriterHandler`, `OutputHandler`), output testing, and sample dataset verification |

---

## 📖 Project Overview

**Sales Reporter** is a robust, modular Java command-line application engineered to ingest, validate, analyze, and report on retail product sales data supplied via CSV (Comma-Separated Values) files.

The application computes business metrics including individual product revenues, category aggregated revenues, the best-selling product by sales volume, the highest revenue-generating product, and the grand total sales revenue. The resulting reports can be emitted either directly to standard console output or saved to a plain text file.

The project is structured according to clean code standards and strict adherence to **SOLID design principles**, **Gang of Four (GoF) design patterns**, and automated unit testing via **JUnit 5**.

---

## ✨ Key Features

- **Robust CSV Ingestion:** Automatically skips blank lines and intelligently detects header rows using heuristic analysis.
- **Resilient Parsing & Data Validation:** Tolerates and skips malformed rows while preserving valid data without crashing the entire run.
- **Accurate Financial Calculations:** Leverages `BigDecimal` arithmetic to prevent floating-point rounding inaccuracies.
- **Sales Analytics & Insights:**
  - Revenue per product (`unit_price * quantity_sold`)
  - Grouped revenue per product category
  - Identification of the best-selling product (by unit volume)
  - Identification of the top revenue-generating product
  - Cumulative grand total revenue
- **Flexible Output Destinations:** Supports output dispatching to the terminal (`console`) or persistence to disk (`file`).
- **Comprehensive Error Handling:** Informative error reporting with standardized system exit codes.
- **Automated Unit Testing:** Full test coverage for core input parsing and output generation components using JUnit 5.

---

## 📐 Software Architecture & Design Principles

The application is structured into decoupled layers, enforcing high cohesion and low coupling:

```
sales.reporter
├── input       # CSV reading, splitting, header detection, row mapping, and exceptions
├── model       # Immutable domain entities (Product)
├── report      # Business logic, metrics calculation, and formatting strategies
└── output      # Destination writers (Console, File) and dispatchers
```

### 1. SOLID Principles Implementation

- **Single Responsibility Principle (SRP):**
  - `Reader`: Coordinates reading lines from a file without coupling to tokenization or mapping rules.
  - `DefaultRowSplitter`: Solely responsible for splitting raw CSV lines into string tokens.
  - `DefaultHeaderDetector`: Solely responsible for inspecting tokens to identify header rows.
  - `DefaultProductRowMapper`: Responsible only for converting string tokens into `Product` models.
  - `SalesCalculator`: Dedicated strictly to financial and statistical calculations.
  - `ConsoleReportFormatter` / `PlainTextReportFormatter`: Dedicated strictly to presentation and layout formatting.
  - `ConsoleWriter` / `FileWriterHandler`: Focus solely on streaming bytes to their respective targets.
- **Open/Closed Principle (OCP):**
  - New report formatting styles (e.g., Markdown, HTML, JSON) can be introduced by implementing `ReportFormatter` without modifying existing formatters.
  - New output media (e.g., Database, Network Socket) can be added by implementing `OutputWriter` and registering it in `OutputWriterFactory`.
- **Liskov Substitution Principle (LSP):**
  - Any subtype of `ReportFormatter`, `OutputWriter`, `ProductRowMapper`, or `RowSplitter` can be substituted into the orchestrator without altering program correctness.
- **Interface Segregation Principle (ISP):**
  - Interfaces are kept minimal and purpose-built (`RowSplitter`, `HeaderDetector`, `ProductRowMapper`, `ReportFormatter`, `OutputWriter`, `SalesCalculatorService`). Clients only depend on methods they invoke.
- **Dependency Inversion Principle (DIP):**
  - High-level orchestration in `ReportGenerator` depends on abstractions (`Reader`, `SalesCalculatorService`, `ReportFormatterFactory`, `OutputWriterFactory`, `OutputHandler`) rather than hardcoded concrete implementations. Dependencies are provided via constructor injection.

### 2. Design Patterns Applied

- **Factory Method Pattern:**
  - `ReportFormatterFactory`: Uses a pre-populated `Map<String, ReportFormatter>` registry to resolve the appropriate `ReportFormatter` by output type key (`console` → `ConsoleReportFormatter`, `file` → `PlainTextReportFormatter`), supporting case-insensitive lookup with trimming.
  - `OutputWriterFactory`: Encapsulates creation of `OutputWriter` instances (`ConsoleWriter`, `FileWriterHandler`), validating required configuration such as file target paths.
- **Strategy Pattern:**
  - Used for report formatting (`ReportFormatter` implementations) and report output writing (`OutputWriter` implementations).
- **Domain Value Objects & Immutability:**
  - `Product` and `SalesSummary` are declared `final` with read-only fields to protect state from unintended mutations during report generation.

---

## 📁 Project Directory Structure

```
Sales-Reporter/
├── pom.xml                                  # Maven project configuration & dependencies
├── sample.csv                               # Sample dataset for demonstration and testing
├── sales_report.txt                         # Generated sample report output
├── src/
│   ├── main/
│   │   └── java/
│   │       └── sales/
│   │           └── reporter/
│   │               ├── ReportGenerator.java                     # Main entry point & CLI orchestrator
│   │               ├── input/
│   │               │   ├── CsvParseException.java               # Custom CSV parsing exception
│   │               │   ├── DefaultHeaderDetector.java           # Header detection logic
│   │               │   ├── DefaultProductRowMapper.java         # Token-to-model mapper
│   │               │   ├── DefaultRowSplitter.java              # CSV line splitting
│   │               │   ├── HeaderDetector.java                  # Header detector interface
│   │               │   ├── ProductRowMapper.java                # Row mapper interface
│   │               │   ├── Reader.java                          # File streaming & ingestion coordinator
│   │               │   └── RowSplitter.java                     # Row splitter interface
│   │               ├── model/
│   │               │   └── Product.java                         # Immutable Product domain model
│   │               ├── output/
│   │               │   ├── ConsoleWriter.java                   # Standard output writer
│   │               │   ├── FileWriterHandler.java               # File system output writer
│   │               │   ├── InvalidOutputMethodException.java    # Output validation exception
│   │               │   ├── OutputHandler.java                   # Output routing handler
│   │               │   ├── OutputWriter.java                    # Output writer interface
│   │               │   └── OutputWriterFactory.java             # Output writer factory
│   │               └── report/
│   │                   ├── SalesCalculator.java                 # Calculation engine implementation
│   │                   ├── SalesCalculatorService.java          # Calculator service interface
│   │                   ├── SalesSummary.java                    # Immutable summary data holder
│   │                   └── formatter/
│   │                       ├── ConsoleReportFormatter.java      # Tabular console formatter
│   │                       ├── PlainTextReportFormatter.java    # Plain text file formatter
│   │                       ├── ReportFormatter.java             # Formatter interface
│   │                       └── ReportFormatterFactory.java      # Formatter factory
│   └── test/
│       └── java/
│           └── sales/
│               └── reporter/
│                   ├── ReportGeneratorTest.java                 # Integration tests for CLI orchestration & exit codes
│                   ├── input/
│                   │   ├── DefaultHeaderDetectorTest.java       # Unit tests for header detection
│                   │   ├── DefaultProductRowMapperTest.java     # Unit tests for row mapping & invalid lines
│                   │   └── ReaderTest.java                      # Unit tests for file ingestion & skipping
│                   ├── output/
│                   │   ├── FileWriterHandlerTest.java           # Unit tests for file output writing
│                   │   └── OutputWriterFactoryTest.java         # Unit tests for writer creation
│                   └── report/
│                       ├── SalesCalculatorTest.java             # Unit tests for calculation engine
│                       └── formatter/
│                           ├── ConsoleReportFormatterTest.java  # Unit tests for console formatting
│                           ├── PlainTextReportFormatterTest.java# Unit tests for plain text formatting
│                           └── ReportFormatterFactoryTest.java  # Unit tests for formatter factory
```

---

## ⚙️ Prerequisites

- **Java Development Kit (JDK):** Version 21 or higher (configured with target Java 26 preview / standard LTS).
- **Apache Maven:** Version 3.8 or higher.
- **Operating System:** Windows / Linux / macOS.

Verify installations:
```bash
java -version
mvn -version
```

---

## 🛠️ Build and Compilation

To compile the source code, run automated tests, and package the application into a JAR file:

```bash
# Clean previous builds and run tests
mvn clean test

# Package the application into target/SalesReporter-1.0-SNAPSHOT.jar
mvn clean package
```

---

## 🚀 Running the Application

### Command-Line Interface Syntax

```bash
java -cp target/classes sales.reporter.ReportGenerator <csv-file-path> <output-method> [output-file-path]
```
*(Or via packaged JAR file if configured with a Main-Class manifest or `-cp target/SalesReporter-1.0-SNAPSHOT.jar`)*

### Command-Line Arguments

| Argument | Requirement | Options / Format | Description |
| :--- | :--- | :--- | :--- |
| `<csv-file-path>` | **Required** | Path to file (e.g. `sample.csv`) | Location of the source CSV data file |
| `<output-method>` | **Required** | `console` or `file` | Target destination for the generated report |
| `[output-file-path]` | **Conditional** | Path to file (e.g. `report.txt`) | Required when `<output-method>` is set to `file` |

---

### Example Commands

#### 1. Display Report on the Console
```bash
java -cp target/classes sales.reporter.ReportGenerator sample.csv console
```

#### 2. Save Report to a Text File
```bash
java -cp target/classes sales.reporter.ReportGenerator sample.csv file sales_report.txt
```

---

## 📊 Sample Input & Output

### Sample Input (`sample.csv`)

```csv
product_id, product_name, category, quantity_sold, unit_price
P001, Wireless Mouse, Electronics, 12, 25.50
P002, Notebook, Stationery, 35, 3.75
P003, USB Hub, Electronics, 8, 18.00
P004, Ballpoint Pen, Stationery, 100, 0.50
P005, HDMI Cable, Electronics, 20, 12.00
```

### Sample Console Output

```text
=====================================================================
                  PRODUCT SALES SUMMARY REPORT
=====================================================================

--- Revenue Per Product ---
P001       Wireless Mouse       Electronics     $306.00
P002       Notebook             Stationery      $131.25
P003       USB Hub              Electronics     $144.00
P004       Ballpoint Pen        Stationery      $50.00
P005       HDMI Cable           Electronics     $240.00

--- Revenue Per Category ---
Electronics          : $690.00
Stationery           : $181.25

--- Highlights ---
Best-Selling Product : Ballpoint Pen (100 units)
Highest Revenue      : Wireless Mouse ($306.00)
Grand Total Revenue  : $871.25
=====================================================================
```

---

## ⚠️ System Exit Codes & Error Handling

The application provides standardized process exit codes to facilitate integration with automation scripts:

| Exit Code | Cause | Description |
| :---: | :--- | :--- |
| `0` | **Success** | Report generated and delivered successfully. |
| `1` | **Missing Arguments** | Insufficient CLI parameters supplied. |
| `2` | **File Not Found** | The specified CSV input file does not exist. |
| `3` | **CSV Parse Error** | Input file contains no valid data rows or unparseable content. |
| `4` | **Invalid Output Method** | Output method is not recognized or target file path is omitted for `file` output. |
| `5` | **I/O Error** | Read/write stream failure encountered during execution. |
| `6` | **Illegal Argument** | Input product collection is empty or invalid for summary calculation. |

---

## 🧪 Testing

Unit and integration tests are written with **JUnit 5 Jupiter (v5.10.2)** and cover all layers of the application:

### Input Layer
- **`DefaultHeaderDetectorTest`** — Verifies header detection (non-numeric vs numeric 4th column) and regression testing against `NumberFormatException`.
- **`DefaultProductRowMapperTest`** — Validates token-to-`Product` mapping and asserts `IOException` is thrown for non-numeric quantities and rows with too few columns.
- **`ReaderTest`** — End-to-end ingestion tests using `@TempDir`: header/blank-line handling, malformed row skipping, `FileNotFoundException` for missing files, and `CsvParseException` when no valid data rows exist.

### Report Layer
- **`SalesCalculatorTest`** — Validates aggregate calculations: grand total revenue, best-selling product by quantity, highest revenue product, category-level revenue, and per-product revenue. Asserts `IllegalArgumentException` for null and empty product lists.
- **`ConsoleReportFormatterTest`** — Verifies formatted console output contains expected section headers, product highlights, and graceful fallback messages (`N/A`, `$0.00`) for empty/null summary metrics.
- **`PlainTextReportFormatterTest`** — Verifies plain text file output contains all expected structural sections (revenue per product, per category, highlights) and asserts `IllegalArgumentException` for null summaries.
- **`ReportFormatterFactoryTest`** — Tests factory resolution of `console` → `ConsoleReportFormatter` and `file` → `PlainTextReportFormatter`, including case-insensitive input handling and `InvalidOutputMethodException` for null/unknown methods.

### Output Layer
- **`FileWriterHandlerTest`** — Verifies report content is correctly written to disk and that subsequent writes overwrite previous content.
- **`OutputWriterFactoryTest`** — Tests correct writer selection for `console` and `file` methods, and correct exception behaviour for null, invalid, and missing/blank file path arguments.

### Integration (Orchestration)
- **`ReportGeneratorTest`** — Tests the full CLI pipeline orchestration using lightweight stub implementations. Validates all six exit code paths (1–6) for missing arguments, `FileNotFoundException`, `CsvParseException`, `InvalidOutputMethodException`, `IOException`, and `IllegalArgumentException`, plus successful console and file output runs (exit code 0).

Execute the complete test suite:
```bash
mvn test
```

---

## 📄 License & Academic Integrity

This project is developed as an academic submission for **SENG 21222 – Software Construction** at the **University of Kelaniya, Sri Lanka**. All rights reserved by the project authors.
