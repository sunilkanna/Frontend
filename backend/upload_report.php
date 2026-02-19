<?php
include 'db_connect.php';

$patient_id = $_POST['patient_id'];
$target_dir = "uploads/";
if (!file_exists($target_dir)) {
    mkdir($target_dir, 0777, true);
}

$file_name = basename($_FILES["file"]["name"]);
$target_file = $target_dir . unique_id() . "_" . $file_name;
$uploadOk = 1;

// Check if file is provided
if(isset($_FILES["file"])) {
    if (move_uploaded_file($_FILES["file"]["tmp_name"], $target_file)) {
        $file_url = "http://YOUR_SERVER_IP/genecare/backend/" . $target_file; // Update with actual URL

        $stmt = $conn->prepare("INSERT INTO patient_reports (patient_id, file_name, file_url) VALUES (?, ?, ?)");
        $stmt->bind_param("iss", $patient_id, $file_name, $file_url);
        
        if ($stmt->execute()) {
            echo json_encode(["status" => "success", "message" => "File uploaded"]);
        } else {
             echo json_encode(["status" => "error", "message" => "Database error: " . $stmt->error]);
        }
    } else {
        echo json_encode(["status" => "error", "message" => "Error uploading file"]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "No file received"]);
}

$conn->close();
?>
