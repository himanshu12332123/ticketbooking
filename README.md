Ticket Booking System

🚀 Introduction

The Ticket Booking System is a Java-based application that allows users to book and cancel train tickets seamlessly. Built using Spring Boot, AWS, and Microservices, this project follows a structured and scalable architecture.

🔥 Key Features

User Authentication: Secure sign-up and login system with password hashing.

Train Search: Find available trains based on source and destination.

Seat Booking: Select and book seats with real-time availability updates.

Booking Management: Fetch and cancel booked tickets easily.

Data Persistence: Users and bookings stored in a JSON-based local database.

Validation Fix: Added login validation to prevent unauthorized booking/cancellation.

Bug Fixes & Improvements

Before Fix: Users could book or cancel tickets without logging in, causing unexpected behavior.After Fix: Implemented a validation check to ensure users must log in before performing these actions.
