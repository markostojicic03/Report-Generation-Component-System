# Report Generation Component System

## Project Overview
This project focuses on the design and implementation of reusable and extensible software components for generating various types of reports. These components are developed as auxiliary libraries intended for integration into diverse software systems. The system supports the creation of both formatted and unformatted reports in multiple output formats including HTML, PDF, Markdown, and plain text.

Beyond simple formatting, the components support data calculations such as COUNT, SUM, AVERAGE, MIN, and MAX to provide analytical insights within the generated reports.

---

## Component Architecture
The system is organized using a modular architecture based on the Service Provider Interface (SPI) concept. This approach ensures a strict separation between the API specification and its concrete implementations.

* API Component: Contains the specification of all reporting functionalities, consisting of abstract operations and common implementations shared across all providers.
* Service Provider Interface (SPI): Defines the mechanisms for loading specific implementations at runtime, allowing the system to be extended with new report formats without modifying the core logic.
* Concrete Implementations: Separate modules responsible for the actual generation of reports in TXT, HTML, PDF, and Markdown formats.

---

## Core Features and Implementation
The project implements several key reporting capabilities through its modular design:

* Multi-Format Support: Generation of reports in plain text, structured HTML, portable PDF documents, and Markdown files.
* Data Processing: Built-in support for statistical functions to aggregate and analyze data during the report generation process.
* Command Line Interface (CLI): A dedicated tool for interacting with the libraries, allowing users to generate reports directly from the terminal.
* Build Automation: Full automation of the build process and dependency management using industry-standard build tools.

---

## Technical Requirements and Build Process
The project emphasizes professional software engineering practices, including automated packaging and documentation.

1. Library Packaging: The API and its providers are packaged as separate modules to be used outside the initial development environment.
2. Build Tools: Automation of the assembly process and dependency resolution is handled through dedicated build scripts.
3. API Documentation: All specification components are fully documented to ensure ease of use for other developers integrating these libraries.

---

## Execution and Usage
To use the report generation system:
1. Build the project using the provided build tool to generate the necessary library artifacts.
2. Integrate the API component into your application or use the provided CLI tool.
3. Configure the desired service provider for the specific output format (HTML, PDF, etc.).
4. Execute the report generation logic by passing the required data and calculation parameters.

---
Developed as a team project for the Software Components course 2024/2025.
