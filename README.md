# Rapyd MNEE Payment Gateway

A Spring Boot-based payment service built on **Rapyd MNEE Payment Gateway** for handling product creation, checkout sessions, and subscription sessions. Developed as part of a hackathon project.

## Hosted Page

You can access the hosted service here: [http://rapydmnee.duckdns.org/](http://rapydmnee.duckdns.org/)

## Features

- Create and manage products
- Create checkout sessions for one-time payments
- Manage subscription sessions for recurring payments
- Fully integrated with Rapyd MNEE Payment Gateway

## Prerequisites

- **Java JDK 17** (Project is built on JDK 17)
- **Maven 3.x** for building the project
- Internet connection to connect to the Rapyd MNEE API

## Installation

1. Clone the repository:

```bash
git clone https://github.com/rapyd-mnee-payment-gateway/paymentservice.git
cd paymentservice
```

2. Run the Spring Boot server 

```bash
mvn spring-boot:run
```

## Usage

Once the server is running, you can access the endpoints for:

* **Products** – Create and manage products
* **Checkout Sessions** – Initiate one-time payments
* **Subscription Sessions** – Manage recurring subscriptions

> For detailed API usage, refer to the code and controller classes.

