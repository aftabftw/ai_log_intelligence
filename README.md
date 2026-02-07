# AI Log Intelligence Dashboard

Real-time log monitoring system with AI-powered root cause analysis.

## Features
- Log ingestion (Spring Boot + PostgreSQL)
- Error clustering and ranking
- AI-based error explanation (Ollama local LLM)
- Anomaly detection
- WebSocket real-time updates
- React dashboard with charts

## Tech Stack
Java, Spring Boot, PostgreSQL  
React, Tailwind, Chart.js  
WebSocket, Ollama (Mistral)

## Run Backend
cd backend
mvn spring-boot:run

## Run Frontend
cd frontend
npm install
npm run dev

## Run AI
ollama run mistral
