# Landmark Tour Planning Simulation

A Java project that simulates optimal tour planning using dynamic programming and bitmasking, considering multiple constraints and scoring metrics.

## Features
- Reads input from multiple files: landmark connections, personal interest, and visitor load
- Uses dynamic programming with bitmasking to compute the best tour (TSP-like)
- Adjusted scoring with decay factor, personal interest, and visitor load
- Calculates total travel time and total attractiveness score
- Outputs the ordered tour and performance statistics

## Technologies
- Java (OOP, DP, Bitmasking, File I/O)
- Advanced algorithm design

## Input Files
- `landmark_map_data.txt`: defines landmarks and connections
- `personal_interest.txt`: user's personal interest for each landmark
- `visitor_load.txt`: current visitor load at each landmark

## Output
- Prints the optimal tour path, total score, and travel time

## What I Learned
- Dynamic Programming with bitmasking for TSP-like problems
- Realistic simulation modeling with multiple constraints
- Efficient handling of graph data structures in Java
