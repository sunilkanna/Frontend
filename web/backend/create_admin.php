<?php
include 'db_connect.php';

$email = "admin@genecare.com";
$password = "admin123";
$fullName = "System Administrator";
$userType = "Admin";

// Check if admin already exists
$checkStmt = $conn->prepare("SELECT id FROM users WHERE email = ?");
$checkStmt->bind_param("s", $email);
$checkStmt->execute();
$checkStmt->store_result();

if ($checkStmt->num_rows > 0) {
    echo "Admin user ($email) already exists.\n";
} else {
    // Hash the password
    $passwordHash = password_hash($password, PASSWORD_DEFAULT);

    // Insert new admin user
    $insertStmt = $conn->prepare("INSERT INTO users (full_name, email, password_hash, user_type) VALUES (?, ?, ?, ?)");
    $insertStmt->bind_param("ssss", $fullName, $email, $passwordHash, $userType);

    if ($insertStmt->execute()) {
        echo "Admin user created successfully.\n";
        echo "Email: $email\n";
        echo "Password: $password\n";
    } else {
        echo "Error creating admin user: " . $insertStmt->error . "\n";
    }
    $insertStmt->close();
}

$checkStmt->close();
$conn->close();
?>
