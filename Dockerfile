# Dockerfile for USPTO API Test Framework
FROM openjdk:11-jdk-slim

# Install dependencies
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Grant execute permission
RUN chmod +x gradlew

# Build the project
RUN ./gradlew clean build -x test

# Default command
CMD ["./gradlew", "clean", "test"]
