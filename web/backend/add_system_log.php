<?php
function add_system_log($conn, $message, $level = 'INFO', $source = 'System') {
    $stmt = $conn->prepare("INSERT INTO system_logs (message, level, source) VALUES (?, ?, ?)");
    $stmt->bind_param("sss", $message, $level, $source);
    $stmt->execute();
    $stmt->close();
}
?>
