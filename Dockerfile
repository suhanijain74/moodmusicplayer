# Build Stage for React
FROM node:20-slim AS build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Final Stage: Java Backend + React Frontend
FROM openjdk:21-slim
WORKDIR /app

# Copy built React files
COPY --from=build-stage /app/dist ./dist
# Copy public assets (songs)
COPY --from=build-stage /app/public ./public
# Copy Java source
COPY java-server/src ./src

# Create bin directory and compile Java
RUN mkdir bin
RUN javac -d bin src/*.java

EXPOSE 9090

# Start the Java server
CMD ["java", "-cp", "bin", "MoodSyncServer"]
