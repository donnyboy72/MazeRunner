# 🧩 Maze Runner (Java Swing)

A procedurally generated maze game built using Java Swing. Navigate from the start tile to the end tile using keyboard controls while the maze is generated dynamically using a randomized Depth-First Search algorithm.

---

## 🚀 Features

- 🔀 **Random Maze Generation**
  - Uses **Depth-First Search (DFS)** to create a unique maze every run
- 🎮 **Player Movement System**
  - Move using WASD or Arrow Keys
  - Backtracking removes your path visually
- 🧠 **Path Tracking**
  - Stack-based movement system tracks player decisions
- 🎨 **Simple Visual UI**
  - Clean grid-based rendering using Java Swing
- 🏁 **Win Condition**
  - Reach the red tile to win

---

## 🕹️ Controls

| Key | Action |
|-----|--------|
| W / ↑ | Move Up |
| S / ↓ | Move Down |
| A / ← | Move Left |
| D / → | Move Right |

---

## 🧱 How It Works

### Maze Generation
- The maze is built using **Randomized Depth-First Search**
- Each tile starts with all walls intact
- Walls are removed as the algorithm explores unvisited neighbors

### Player Movement
- A custom stack (`PlayerMoves`) tracks movement
- Moving backward:
  - Pops the last move
  - Removes the "played" path visually
- Prevents illegal moves through walls or outside bounds

---

## 📂 Project Structure
├── App.java # Entry point, sets up JFrame
├── MazeRunner.java # Core game logic, rendering, input handling
├── Tile.java # Represents each maze cell

https://github.com/user-attachments/assets/b10266e5-5397-431d-b4c3-a0ff490787e8


https://github.com/user-attachments/assets/134e27af-c91b-410c-bfb6-4876f934bf1d




https://github.com/user-attachments/assets/7badba37-b952-4610-81dd-5435949fa8ee


