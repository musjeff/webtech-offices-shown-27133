# webtech-offices-shown-27133
# Library Frontend

A simple React app (built with Vite) that manages **offices** through a Spring Boot backend.

## Features

- **Save** a new office (name and number)
- **Search** offices by name
- **Table** that lists all offices
- **Update** an office from the table
- **Delete** an office from the table

## Requirements

- [Node.js](https://nodejs.org/) (version 18 or newer)
- The Spring Boot backend (`mediclinic`) running on `http://localhost:8082`

## How to run

1. Install the packages:
   ```
   npm install
   ```
2. Start the app:
   ```
   npm run dev
   ```
3. Open `http://localhost:5173` in your browser.

## Notes

- Start the backend first, otherwise the table will be empty and you will get a "Cannot reach the server" message.
- The office page is in `src/assets/components/Office.jsx`.
- The backend address is set at the top of that file (`API_URL`).
- An office that is assigned to a doctor cannot be deleted.

## Built with

React, Vite, Axios
