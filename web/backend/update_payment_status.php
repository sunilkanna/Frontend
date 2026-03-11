<?php
include 'db_connect.php';

$data = json_decode(file_get_contents("php://input"), true);
$payment_id = $data['payment_id'] ?? null;
$status = $data['status'] ?? null; // 'Completed' or 'Failed'
$transaction_id = $data['transaction_id'] ?? null;

if (!$payment_id || !$status) {
    echo json_encode(["status" => "error", "message" => "Payment ID and Status required"]);
    exit();
}

$stmt = $conn->prepare("UPDATE payments SET status = ?, transaction_id = ? WHERE id = ?");
$stmt->bind_param("ssi", $status, $transaction_id, $payment_id);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Payment status updated"]);
} else {
    echo json_encode(["status" => "error", "message" => "Failed to update payment status"]);
}

$stmt->close();
$conn->close();
?>
