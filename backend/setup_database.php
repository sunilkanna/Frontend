<?php
$servername = "localhost";
$username = "root";
$password = "";

// Create connection without selecting database
$conn = new mysqli($servername, $username, $password);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Create database if it doesn't exist
$sql = "CREATE DATABASE IF NOT EXISTS genecare_db";
if ($conn->query($sql) === TRUE) {
    echo "Database 'genecare_db' created or already exists.<br>";
} else {
    echo "Error creating database: " . $conn->error . "<br>";
}

// Select the database
$conn->select_db("genecare_db");

// Read the SQL file
$sql = file_get_contents('init_db.sql');

// Remove USE statement if present to avoid confusion since we already selected
// But better to just run it. The file has USE genecare_db; which is fine.

if ($conn->multi_query($sql)) {
    echo "<h1>Tables Created Successfully!</h1>";
    echo "<p>Your server is ready.</p>";
    
    do {
        if ($result = $conn->store_result()) {
            $result->free();
        }
    } while ($conn->more_results() && $conn->next_result());
    
} else {
    echo "Error creating tables: " . $conn->error;
}

$conn->close();
?>
