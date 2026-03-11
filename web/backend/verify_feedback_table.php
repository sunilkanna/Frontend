<?php
include 'db_connect.php';

echo "<h2>Feedback Table Schema Check</h2>";

$table = "feedback";
$result = $conn->query("SHOW TABLES LIKE '$table'");

if ($result->num_rows == 0) {
    echo "<p style='color: red;'>Table <b>$table</b> is MISSING!</p>";
    echo "<p>Creating table...</p>";
    
    $create = "CREATE TABLE feedback (
        id INT AUTO_INCREMENT PRIMARY KEY,
        appointment_id INT NOT NULL,
        patient_id INT NOT NULL,
        rating INT CHECK (rating BETWEEN 1 AND 5),
        comments TEXT,
        submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (appointment_id) REFERENCES appointments(id),
        FOREIGN KEY (patient_id) REFERENCES users(id)
    )";
    
    if ($conn->query($create)) {
        echo "<p style='color: green;'>Table created successfully.</p>";
    } else {
        echo "<p style='color: red;'>Failed to create table: " . $conn->error . "</p>";
    }
} else {
    echo "<p style='color: green;'>Table <b>$table</b> exists.</p>";
    
    $columns = $conn->query("SHOW COLUMNS FROM $table");
    echo "<h3>Columns:</h3><ul>";
    while($row = $columns->fetch_assoc()) {
        echo "<li>" . $row['Field'] . " (" . $row['Type'] . ")</li>";
    }
    echo "</ul>";
    
    $count = $conn->query("SELECT COUNT(*) as total FROM $table");
    $row = $count->fetch_assoc();
    echo "<p>Total entries: " . $row['total'] . "</p>";
}

$conn->close();
?>
