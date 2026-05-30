# LaneRunner - A Bus Dodger Game for Android

A fun and challenging arcade game built in Kotlin for the Android platform.
The main objective of the driver is to avoid picking up passengers at bus stops, keeping the bus as empty as possible throughout the shift.

## 🎮 Game Rules
1. **Continuous Movement:** The bus moves forward constantly (or rather, the bus stations move downward toward the bus).
2. **Dodge the Stations:** You must maneuver the bus left and right to avoid colliding with incoming bus stations.
3. **Lives (Hearts):** The driver starts with 3 hearts. Each collision with a station costs 1 heart.
4. **Endless Gameplay:** Once all hearts are lost, they automatically refresh, allowing the game to continue seamlessly without stopping.
5. **Vibration & Toasts:** Every collision triggers a device vibration alongside a humorous Toast message reminding the driver to stay away from passengers.

## 🛠 Technologies & Architecture
* **Kotlin:** The primary programming language used for development.
* **Singleton Design Pattern:** Centralized management for Toasts and vibrations via the thread-safe `SignalManager`, utilizing `WeakReference` to protect against Android memory leaks.
* **Lifecycle Management:** Smart handling of `onPause()` and `onResume()` states to freeze the game loop when the app goes to the background, saving device battery and CPU resources.
* **Game Loop:** Powered by a decoupled `Handler` and `Runnable` mechanism to handle periodic game ticks securely.
* **Matrix-Based Logic:** High-performance, grid-based game state management (`GameManager`) completely separated from the UI rendering layer (`MainActivity`).

## 🚀 Development Structure
The game follows a strict separation of concerns architecture:
* **The Brain (`GameManager`):** Manages internal matrix tracking, collision detection, and life counting.
* **The Painter (`MainActivity`):** Listens to game ticks, handles user input, and translates the matrix state into visual elements on the screen.