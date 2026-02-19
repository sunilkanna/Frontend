<?php
include 'db_connect.php';

$data = json_decode(file_get_contents("php://input"), true);
$appointment_id = $data['appointment_id'] ?? null;
$amount = $data['amount'] ?? null;
$payment_method = $data['payment_method'] ?? 'UPI';

if (!$appointment_id || !$amount) {
    echo json_encode(["status" => "error", "message" => "Appointment ID and Amount required"]);
    exit();
}

$stmt = $conn->prepare("INSERT INTO payments (appointment_id, amount, payment_method, status) VALUES (?, ?, ?, 'Pending')");
$stmt->bind_param("ids", $appointment_id, $amount, $payment_method);

if ($stmt->execute()) {
    $payment_id = $stmt->insert_id;
    echo json_encode(["status" => "success", "payment_id" => $payment_id, "message" => "Payment initiated"]);
} else {
    echo json_encode(["status" => "error", "message" => "Failed to create payment"]);
}

$stmt->close();
$conn->close();
?>
